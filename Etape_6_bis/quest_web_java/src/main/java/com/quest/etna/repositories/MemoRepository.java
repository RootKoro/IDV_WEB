package com.quest.etna.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Memo;

@Repository
public interface MemoRepository extends CrudRepository<Memo, Integer>{
	@Query(value="SELECT * FROM recipe where owner_id like :userId", nativeQuery=true)
	public Iterable<Memo> findByUserId(Integer userId);
}
