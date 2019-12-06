package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;


public class PaymentLinkResponse{

	@SerializedName("link")
	private String link;

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	@Override
 	public String toString(){
		return 
			"PaymentLinkResponse{" + 
			"link = '" + link + '\'' + 
			"}";
		}
}