package com.cropaccounting.models;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
/*import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
*/import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;

import lombok.Data;
import lombok.NonNull;

@Entity
@PersistenceUnit(name = "default")
@Data
public class Crop {
	@Id
	private long id;
	@NonNull
	private String name;

	private String customerId;

	private long portalid;

	private String type;
	private long crop;
	private long varity;

	private long cropLifeCyle;
	private long cropUnit;
	private int cropLandQuantity;
	private String cropCast;
	private String groupId;
	private String agreementId;
	private String description;
	private LocalDate startDate;
	private String locations;
	@ManyToOne(cascade = { CascadeType.ALL })
	private Farmer farmer;
	/*
	 * @ManyToMany(cascade = {CascadeType.ALL}) private List<DocDescType>
	 * listOfDocDescType;
	 */
}
