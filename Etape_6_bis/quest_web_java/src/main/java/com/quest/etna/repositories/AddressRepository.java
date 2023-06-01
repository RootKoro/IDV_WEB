package com.quest.etna.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer>{
	@Query(value="SELECT * FROM address where user_id like :userId", nativeQuery=true)
	public Iterable<Address> findByUserId(Integer userId);
}
