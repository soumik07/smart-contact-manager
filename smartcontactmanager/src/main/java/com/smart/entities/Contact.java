package com.smart.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private String nickName; //2ndname
	@Column(unique = true)
	private String email;
	private String work;
	@Column(unique = true)
	private long phone;
	private String imageurl;  //image
	@Column(length = 500)
	private String description;
	
	@ManyToOne
	private User user;

	
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Contact(int cid, String name, String nickName, String email, String work, long phone, String imageurl,
			String description, User user) {
		super();
		this.cid = cid;
		this.name = name;
		this.nickName = nickName;
		this.email = email;
		this.work = work;
		this.phone = phone;
		this.imageurl = imageurl;
		this.description = description;
		this.user = user;
	}


	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", email=" + email + ", work="
				+ work + ", phone=" + phone + ", imageurl=" + imageurl + ", description=" + description + ", user="
				+ user + "]";
	}
	
	

	
	
	
	
}
