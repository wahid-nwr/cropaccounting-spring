package com.cropaccounting.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUnit;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class CropIncomeList {
	@Id
	private long id;
	private String type;
	private long crop;
	private String cropName;
	private long varity;
	private String varityName;
	// @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)

	// @OneToMany(cascade = { CascadeType.ALL })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IncomeItemValue> incomeItemValueList = new ArrayList<IncomeItemValue>();
}
