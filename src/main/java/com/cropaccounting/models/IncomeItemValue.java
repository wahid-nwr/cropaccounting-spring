package com.cropaccounting.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;

import lombok.Data;

@Entity
@PersistenceUnit(name = "default")
@Data
public class IncomeItemValue {
	@Id
	private long id;
	@ManyToOne
	private CropIncome cropIncome;
	private String type;
	private int day;
	private float amount;
	private float totValue;

	@Override
	public String toString() {
		return "{id:" + this.id + ",cropIncome:" + this.cropIncome.getId() + ",type:" + this.type + "," + "day:"
				+ this.day + ",amount:" + this.amount + ",totValue:" + totValue + "}";
	}
}
