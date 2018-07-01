package com.cropaccounting.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;

import lombok.Data;
import lombok.NonNull;

@Entity
@PersistenceUnit(name = "default")
@Data
public class CropActivityItem {
	@Id
	private long id;
	@NonNull
	private String name;
}
