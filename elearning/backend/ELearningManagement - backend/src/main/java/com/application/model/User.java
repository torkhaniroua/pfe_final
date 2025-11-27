package com.application.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class User 
{
	@Id
	private String email;
	private String username;
	private String userid;
	private String mobile;
	private String gender;
	private String profession;
	private boolean isPremuim = false;
	private String address;
	private String password;
	private String role = "User";
	private String avatarUrl;
	private String authProvider;
	@Column(name = "email_verified")
	private boolean emailVerified = false;
	
	public User() 
	{
		super();
	}

	public User(String email, String username, String userid, String mobile, String gender, String profession, String address, String password, String role, boolean isPremuim, String avatarUrl, String authProvider)
	{
		super();
		this.email = email;
		this.username = username;
		this.userid = userid;
		this.mobile = mobile;
		this.gender = gender;
		this.profession = profession;
		this.address = address;
		this.password = password;
		this.role = role;
		this.isPremuim = isPremuim;
		this.avatarUrl = avatarUrl;
		this.authProvider = authProvider;
	}

	public boolean getIsPremuim() 
	{
		return isPremuim;
	}

	public void setIsPremuim(boolean isPremuim) 
	{
		this.isPremuim = isPremuim;
	} 

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getUserid() 
	{
		return userid;
	}

	public void setUserid(String userid) 
	{
		this.userid = userid;
	}

	public String getMobile() 
	{
		return mobile;
	}

	public void setMobile(String mobile) 
	{
		this.mobile = mobile;
	}

	public String getGender() 
	{
		return gender;
	}

	public void setGender(String gender) 
	{
		this.gender = gender;
	}

	public String getProfession() 
	{
		return profession;
	}

	public void setProfession(String profession) 
	{
		this.profession = profession;
	}

	public String getAddress() 
	{
		return address;
	}

	public void setAddress(String address) 
	{
		this.address = address;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
}
