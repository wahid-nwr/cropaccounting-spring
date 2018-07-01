package com.cropaccounting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cropaccounting.models.ExpenceItem;

public interface ExpenceItemRepository extends CrudRepository<ExpenceItem, Long>  {
	@Query("SELECT e FROM ExpenceItem e ORDER BY e.getId() DESC")
	public List<ExpenceItem> findOrderedList();
}
