package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;


public class AuthResponse {

	@SerializedName("id")
	private String id;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"AuthResponse{" +
			"id = '" + id + '\'' + 
			"}";
		}
}