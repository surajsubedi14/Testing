package com.suraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suraj.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
