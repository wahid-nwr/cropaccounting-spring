package com.cropaccounting.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class CropCalenderTask {
	@Id
	private long id;
	private String taskName;
	private String taskDateStr;
	private Date dateOfUpload;

	@ManyToOne
	private CropActivity cropActivity;

	@ManyToOne
	private CropActivityType cropActivityType;

	private String comments;
}
