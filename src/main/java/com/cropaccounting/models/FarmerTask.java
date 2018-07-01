package com.cropaccounting.models;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class FarmerTask {
	@Id
	private long id;
	@ManyToOne
	private CropActivity cropActivity;
	@ManyToOne
	private CropActivityType cropActivityType;
	@ManyToOne
	private CropActivityItem cropActivityItem;

	private float itemExpence;
	private float labourExpence;
	private String taskName;
	private String taskDateStr;
	private Date dateOfUpload;
	private LocalDateTime start;
	private LocalDateTime end;

	public FarmerTask(CropActivity cropActivity, CropActivityType cropActivityType, CropActivityItem cropActivityItem,
			float itemExpence, float labourExpence, String taskName, String taskDateStr, Date dateOfUpload,
			LocalDateTime start, LocalDateTime end) {
		this.cropActivity = cropActivity;
		this.cropActivityType = cropActivityType;
		this.cropActivityItem = cropActivityItem;

		this.itemExpence = itemExpence;
		this.labourExpence = labourExpence;
		this.taskName = taskName;
		this.taskDateStr = taskDateStr;
		this.dateOfUpload = dateOfUpload;
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "{id:'',cropActivity_id:'',cropActivity_name:" + this.cropActivity.getName() + ","
				+ "cropActivityType_id:'',cropActivityType_name:" + this.cropActivityType.getName() + ","
				+ "cropActivityItem_id:'',cropActivityItem_name:" + this.cropActivityItem.getName() + ","
				+ "itemExpence:" + this.itemExpence + ", labourExpence:" + this.labourExpence + "," + "taskName:"
				+ this.taskName + ", taskDateStr:" + this.taskDateStr + "}";
	}
}
