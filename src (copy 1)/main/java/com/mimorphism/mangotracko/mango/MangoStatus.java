package com.mimorphism.mangotracko.mango;

public enum MangoStatus {
	
	/**
	 * Finished publication.Not to be confused
	 * with a user's finished status i.e a user has finished a book
	 */
	FINISHED("FINISHED"),
	RELEASING("RELEASING"),
	CANCELLED("CANCELLED");
//	HIATUS("HIATUS"),
	
	
	public final String status;
	
	private MangoStatus(String status) {
		this.status = status;
	}
	
}
