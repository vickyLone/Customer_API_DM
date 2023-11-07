package com.pmd.dm.serviceImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pmd.dm.entity.CustomerEntity;
import com.pmd.dm.repo.CustomerRepository;
import com.pmd.dm.service.CustomerService;
import com.pmd.dm.utils.EmailUtils;
import com.pmd.dm.utils.PzwdUtils;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmailUtils emailOtp;

	@Autowired
	private PzwdUtils pzwdUtils;

	@Override
	public Optional<CustomerEntity> findByEmailAndPassword(CustomerEntity login) {
		Optional<CustomerEntity> customer = customerRepository.findByEmail(login.getEmail());

		if (customer.isPresent()) {
			if (passwordEncoder.matches(login.getPzwd(), customer.get().getPzwd())) {
				String role = customer.get().getRole();

				if ("Customer".equals(role) || "Admin".equals(role)) {
					return customer;
				}
			}
		}

		return Optional.empty();
	}

	@Override
	public CustomerEntity getCustomerByPhoneNumber(String phoneNumber) {
		Optional<CustomerEntity> customer = customerRepository.findByPhoneNumber(phoneNumber);

		if (customer.isPresent()) {
			return customer.get();
		} else {
			throw new NoSuchElementException("Customer not found with phone number: " + phoneNumber);
		}
	}

	@Override
	public Boolean saveCustomer(CustomerEntity customer) {
		// Check if a customer with the same email already exists
		Optional<CustomerEntity> existingCustomer = customerRepository.findByEmail(customer.getEmail());

		if (existingCustomer.isPresent()) {
			// A customer with the same email already exists
			return false;
		}

		// Hash the password
		String hashedPassword = passwordEncoder.encode(customer.getPzwd());
		customer.setPzwd(hashedPassword);

		try {
			customerRepository.save(customer);
			return true; // Entity saved successfully
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Error occurred while saving
		}
	}

	@Override
	public Boolean forgotPzwd(CustomerEntity forgotPzwd) throws MessagingException {
		Optional<CustomerEntity> customerEntity = customerRepository.findByEmail(forgotPzwd.getEmail());

		if (customerEntity.isEmpty()) {
			return false;
		}

		// Generate a new temporary password
		String temporaryPassword = pzwdUtils.generatePzwd();

		// Encode the temporary password with the password encoder
		String encodedTemporaryPassword = passwordEncoder.encode(temporaryPassword);

		// Update the user's password in the database
		CustomerEntity customerToUpdate = customerEntity.get();
		customerToUpdate.setPzwd(encodedTemporaryPassword);
		customerRepository.save(customerToUpdate);

		String to = customerToUpdate.getEmail();
		String subject = "Recover Your Password";

		StringBuilder body = new StringBuilder();

		body.append(
				"<h2>Hi, You have a New Password</h2> </br> <h3>Your Temporary Password is: </h3>" + temporaryPassword);

		emailOtp.generateEmail(to, subject, body.toString());

		return true;
	}
	
	

}
