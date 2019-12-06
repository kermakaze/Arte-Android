package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;


public class GenericResponse{

	@SerializedName("success")
	private boolean success;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"GenericResponse{" + 
			"success = '" + success + '\'' + 
			"}";
		}
}