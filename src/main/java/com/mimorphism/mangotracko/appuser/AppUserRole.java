package com.mimorphism.mangotracko.appuser;

public enum AppUserRole {
    USER("USER"),
    ADMIN("ADMIN");
    
    public final String role;
	
	private AppUserRole(String role) {
		this.role = role;
	}
}