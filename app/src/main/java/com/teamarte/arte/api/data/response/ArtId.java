package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;


public class ArtId{

	@SerializedName("sellerId")
	private String sellerId;

	@SerializedName("fullResUrl")
	private String fullResUrl;

	@SerializedName("price")
	private String price;

	@SerializedName("__v")
	private int V;

	@SerializedName("description")
	private String description;

	@SerializedName("_id")
	private String id;

	public void setSellerId(String sellerId){
		this.sellerId = sellerId;
	}

	public String getSellerId(){
		return sellerId;
	}

	public void setFullResUrl(String fullResUrl){
		this.fullResUrl = fullResUrl;
	}

	public String getFullResUrl(){
		return fullResUrl;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ArtId{" + 
			"sellerId = '" + sellerId + '\'' + 
			",fullResUrl = '" + fullResUrl + '\'' + 
			",price = '" + price + '\'' + 
			",__v = '" + V + '\'' + 
			",description = '" + description + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}