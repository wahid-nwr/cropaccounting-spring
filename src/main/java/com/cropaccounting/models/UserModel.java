/*
 * Copyright (C) 2011 mPower Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cropaccounting.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import lombok.Data;

/**
 * User Model - Contains user data.
 */

@Entity
// @Table(name="UserModel",schema="cropaccounting")
@PersistenceUnit(name = "default")
@Data
public class UserModel /* extends Model implements RoleHolder */ {
	@Id
	private long id;
	
	@NotNull
	@Column(unique = true)
	@Email
	public String email;

	// @Required
	@Column(unique = true, nullable = false)
	@Size(min = 4, max = 15)
	// @Match(value="^\\w*$", message="Not a valid username or Please give Number")
	@NotNull(message = "Not a valid username or Please give Number")
	public String name;

	// @Password
	@Column(nullable = false)
	@Size(min = 5, max = 15)
	public String password;

	@Transient
	// @Equals(value="password", message="Password doesn't match")
	// @Password
	@Column(nullable = false)
	@Size(min = 5, max = 15)
	public String confirmPassword;

	@Size(max = 100)
	public String displayName;

	@Size(min = 11, max = 11)
	public String mobileNumber;

	// @Required
	@ManyToOne
	public Role role;

	/*
	 * @ManyToMany
	 * 
	 * @OrderBy("geoPSUId ASC") public Set<GeoPSU> geoPSUs = new TreeSet<GeoPSU>();
	 */
	/*
	 * @ManyToMany public List<User> enumerator;
	 */

	/**
	 * @param email
	 * @param password
	 */
	public UserModel(String email, String password) {
		this.email = email;
		this.password = password;
		// this.geoPSUs = new TreeSet<GeoPSU>();
	}

	/**
	 * @param email
	 * @param password
	 * @param name
	 */
	public UserModel(String email, String password, String name) {
		this(email, password);
		this.name = name;
	}

	public UserModel(String email, String password, String name, Role role) {
		this(email, password, name);
		this.role = role;
	}

	/*
	 * // From RoleHolder Interface
	 * 
	 * @Override public List<? extends Role> getRoles() { List<Role> list = new
	 * ArrayList<Role>(); list.add(this.role); return list; }
	 * 
	 *//**
		 * Authenticate.
		 *
		 * @param username
		 *            the username
		 * @param password
		 *            the password
		 * @return the user
		 */
	/*
	 * public static UserModel authenticate(String username, String password) {
	 * UserModel authenticatedUser = UserModel.find("byNameAndPassword", username,
	 * Crypto.passwordHash(password, HashType.SHA512)).first(); return
	 * authenticatedUser; //String u = "%" + username + "%"; //return
	 * User.find("BINARY name like %?% and password = ? ", username,
	 * Crypto.passwordHash(password, HashType.SHA512)).first(); //Query query =
	 * JPA.em().
	 * createQuery("select id, created_at, displayName, email, name, password, updated_at, role_id from mpower_lms2.User  where BINARY name like %"
	 * + username +"% and password = "+ Crypto.passwordHash(password,
	 * HashType.SHA512)); //User user = (User) query.getResultList().get(0);
	 * //return User.find("byNameAndPassword", username,
	 * Crypto.passwordHash(password, HashType.SHA512)).first(); //return User.
	 * find("select u from User u where BINARY u.name like ? and u.password =? ", u,
	 * Crypto.passwordHash(password, HashType.SHA512)).first(); //return
	 * User.find("select u from User u where u.name = ? and u.password =? ", u,
	 * Crypto.passwordHash(password, HashType.SHA512)).first();
	 * 
	 * //return User.find("select u from User u where BINARY u.name =? ",
	 * "%Admin%").first(); }
	 * 
	 *//**
		 * Find User by name
		 */
	/*
	 * public static UserModel findByName(String name) { return
	 * UserModel.find("byName", name).first(); }
	 * 
	 *//**
		 * Before save a User
		 */
	/*
	 * @PrePersist
	 * 
	 * @PreUpdate public void beforeSave() { updatePassword(); }
	 * 
	 *//**
		 * Update password by its hash value
		 */
	/*
	 * private void updatePassword() { Logger.info("password set for " + this);
	 * this.password = Crypto.passwordHash(this.password, HashType.SHA512); }
	 * 
	 *//**
		 * Return list of User with given role
		 *//*
			 * public static List<UserModel> getRoleUser(Role role) { return
			 * UserModel.find("role = ?", role).fetch(); }
			 * 
			 * @Override public String toString() { return this.id + " " + this.name + " " +
			 * this.email + " " + this.role.id; }
			 */
	/*
	 * public static List<GeoPSU> getAssignableGeoPSU() { List<GeoPSU>
	 * alreadyAssignedPSU =
	 * GeoPSU.find("SELECT psu FROM User as u LEFT JOIN u.geoPSUs as psu").fetch();
	 * List<GeoPSU> assignablePSU = GeoPSU.findAll();
	 * assignablePSU.removeAll(alreadyAssignedPSU); return assignablePSU; }
	 * 
	 * public static List<User> getUserbyPSU(Long id){
	 * 
	 * Query query = JPA.em().
	 * createNativeQuery("Select User_id from User_GeoPSU where geoPSUs_id = " +
	 * id); List<BigInteger> user_ids = query.getResultList();
	 * 
	 * Logger.info("user list" + user_ids); List<User> users = new
	 * ArrayList<User>();
	 * 
	 * for(BigInteger user_id : user_ids){
	 * 
	 * Long l = Long.parseLong(user_id.toString()); Logger.info("in loop " + l);
	 * 
	 * User user = User.findById(l); Logger.info("User: " + user.name);
	 * users.add(user); }
	 * 
	 * return users; }
	 * 
	 * 
	 * public static List<User> getUserListbyPSUList(List<GeoPSU> listPSU){
	 * 
	 * List<User> userlist = new ArrayList<User>();
	 * 
	 * for(GeoPSU psu : listPSU){ userlist.addAll(getUserbyPSU(psu.id)); }
	 * 
	 * return userlist; }
	 */
}
