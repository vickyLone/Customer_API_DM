package com.pmd.dm.service;

import java.util.Optional;

import javax.mail.MessagingException;

import com.pmd.dm.entity.CustomerEntity;

public interface CustomerService {

	Optional<CustomerEntity> findByEmailAndPassword(CustomerEntity login);

	CustomerEntity getCustomerByPhoneNumber(String phoneNumber);


	Boolean saveCustomer(CustomerEntity customer);


	Boolean forgotPzwd(CustomerEntity forgotPzwd) throws MessagingException;



	/*
	 * Boolean saveCustomer(CustomerEntity customer);
	 */}
