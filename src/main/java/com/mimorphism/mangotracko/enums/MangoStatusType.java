package com.mimorphism.mangotracko.enums;

public enum MangoStatusType {
	
	COMPLETED("COMPLETED"),
	ONGOING("ONGOING");

	private String status;

	private MangoStatusType(String status) {
		this.status = status;
	}

	public String getStatus() 
	{
		return this.status;	
	}
	
}
