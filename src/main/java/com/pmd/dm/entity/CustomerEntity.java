package com.pmd.dm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_Customer")
public class CustomerEntity {
	    @Id
	    @Column(name = "Customer_Id")
	    @GeneratedValue(strategy  = GenerationType.IDENTITY)
	    private Integer customerId; 
	    private String name;
	    private String email;
	    private String pzwd;
	    private String role;
	    
	    private String phoneNumber;
	    private String address;
	    
	   
}
