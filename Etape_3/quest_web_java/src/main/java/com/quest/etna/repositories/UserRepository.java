package com.quest.etna.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.quest.etna.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	@Query(value="SELECT * FROM user u WHERE u.username LIKE :username", nativeQuery=true)
	public Optional<User> findByUsername (String username);
}
