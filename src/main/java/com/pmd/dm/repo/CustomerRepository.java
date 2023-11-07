package com.pmd.dm.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pmd.dm.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>{

	Optional<CustomerEntity> findByEmail(String email);

	Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);

}
