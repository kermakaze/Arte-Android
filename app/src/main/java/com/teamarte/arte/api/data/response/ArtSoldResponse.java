package com.teamarte.arte.api.data.response;


import com.google.gson.annotations.SerializedName;


public class ArtSoldResponse{

	@SerializedName("artId")
	private ArtId artId;

	@SerializedName("referenceNumber")
	private String referenceNumber;

	@SerializedName("__v")
	private int V;



	@SerializedName("id")
	private String id;

	@SerializedName("buyerId")
	private String buyerId;

	@SerializedName("status")
	private String status;

	public void setArtId(ArtId artId){
		this.artId = artId;
	}

	public ArtId getArtId(){
		return artId;
	}

	public void setReferenceNumber(String referenceNumber){
		this.referenceNumber = referenceNumber;
	}

	public String getReferenceNumber(){
		return referenceNumber;
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

	public void setBuyerId(String buyerId){
		this.buyerId = buyerId;
	}

	public String getBuyerId(){
		return buyerId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ArtSoldResponse{" + 
			"artId = '" + artId + '\'' + 
			",referenceNumber = '" + referenceNumber + '\'' + 
			",__v = '" + V + '\'' + 
			",_id = '" + id + '\'' + 
			",id = '" + id + '\'' + 
			",buyerId = '" + buyerId + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}