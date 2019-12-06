package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SellerId implements Serializable {

	@SerializedName("googleId")
	private String googleId;

	@SerializedName("__v")
	private int V;

	@SerializedName("_id")
	private String id;

	@SerializedName("username")
	private String username;

	@SerializedName("profilePhotoUrl")
	private String profilePhotoUrl;

	public void setGoogleId(String googleId){
		this.googleId = googleId;
	}

	public String getGoogleId(){
		return googleId;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setProfilePhotoUrl(String profilePhotoUrl){
		this.profilePhotoUrl = profilePhotoUrl;
	}

	public String getProfilePhotoUrl(){
		return profilePhotoUrl;
	}

	@Override
 	public String toString(){
		return 
			"SellerId{" + 
			"googleId = '" + googleId + '\'' + 
			",__v = '" + V + '\'' + 
			",_id = '" + id + '\'' + 
			",username = '" + username + '\'' + 
			",profilePhotoUrl = '" + profilePhotoUrl + '\'' + 
			"}";
		}
}