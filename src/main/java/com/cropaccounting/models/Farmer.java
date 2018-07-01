package com.cropaccounting.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class Farmer {
	@Id
	private long id;
	@NotNull
	private String name;
	private String mobileNo;
	private String nid;
	private long village;
	private long para;
	private int block;
	private String landDagNo;
	/*
	 * @ManyToMany(cascade = {CascadeType.ALL}) public List<DocDescType>
	 * listOfDocDescType;
	 */
}
