package com.pmd.dm.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pmd.dm.entity.CustomerEntity;
import com.pmd.dm.repo.CustomerRepository;
import com.pmd.dm.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;


	@Autowired
	private CustomerRepository customerRepository;
	/*
	 * @Autowired private FindByIndexNameSessionRepository<MapSession>
	 * sessionRepository;
	 * 
	 * @Autowired private SpringSessionBackedSessionRegistry<MapSession>
	 * sessionRegistry;
	 */

	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody CustomerEntity customer) {


		if (customerService.saveCustomer(customer)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
		}
	}

	@GetMapping(value = "/customer/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerEntity> getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
		CustomerEntity customer = customerService.getCustomerByPhoneNumber(phoneNumber);
		return ResponseEntity.ok(customer);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> loginCustomer(@RequestBody CustomerEntity login,
			HttpSession httpSession) {
		Optional<CustomerEntity> customer = customerService.findByEmailAndPassword(login);

		if (customer.isPresent()) {
			// Set user details in the session
			httpSession.setAttribute("email", customer.get().getEmail());
			httpSession.setAttribute("role", customer.get().getRole());
			httpSession.setAttribute("name", customer.get().getName());

			// Create a map to hold the user data
			Map<String, String> userData = new HashMap<>();
			userData.put("email", customer.get().getEmail());
			userData.put("role", customer.get().getRole());
			userData.put("name", customer.get().getName());

			// Return the user data in the response
			return ResponseEntity.ok(userData);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "Login failed"));
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<String> logoutCustomer(HttpSession httpSession) {
		httpSession.invalidate();
		return ResponseEntity.ok("Logged out successfully");
	}
	/*
	 * @GetMapping("/user") public ResponseEntity<String> getCurrentUser() {
	 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 * String username = auth.getName(); return ResponseEntity.ok("Current user: " +
	 * username); }
	 */

	@GetMapping("/allCustomers")
	List<CustomerEntity> getAllCustomers() {
		return customerRepository.findAll();

	}

	@PostMapping("/forgotPzwd")
	public ResponseEntity<String> forgotPazzword(@RequestBody CustomerEntity forgotPzwd) {
		try {
			boolean passwordResetSuccessful = customerService.forgotPzwd(forgotPzwd);

			if (passwordResetSuccessful) {
				return ResponseEntity.ok("Password reset successfully. Check your email for the temporary password.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with the provided email not found");
			}
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error sending email. Please try again later.");
		}
	}
}
