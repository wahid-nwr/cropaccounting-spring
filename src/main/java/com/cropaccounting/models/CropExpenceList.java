package com.cropaccounting.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class CropExpenceList {
	@Id
	private long id;
	private String type;
	private long crop;
	private long varity;
	@ManyToOne(cascade = CascadeType.ALL)
	private CropTaskMap cropTaskMap;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<ExpenceItemValue> expenceItemValueList = new ArrayList<ExpenceItemValue>();
}
