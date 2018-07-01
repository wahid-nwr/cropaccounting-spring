package com.cropaccounting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cropaccounting.models.CropIncomeList;

public interface CropIncomeListRepository extends CrudRepository<CropIncomeList, Long> {

	@Query("SELECT c FROM CropIncomeList c WHERE c.type = :type AND c.crop = :crop AND c.varity = :varity")
	public List<CropIncomeList> find(@Param("type") String type, @Param("crop") long crop, @Param("varity") long varity);
	
	default Optional<CropIncomeList> findOne(@Param("type") String type, @Param("crop") long crop, @Param("varity") long varity) {
		return find(type, crop, varity).stream().findFirst();
	}
}
