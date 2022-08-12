package com.mimorphism.mangotracko.mango;

public enum RecordType {
	BACKLOG("BACKLOG"),
	CURRENTLY_READING("CURRENTLY_READING"),
	FINISHED("FINISHED");

	
public final String type;
	
	private RecordType(String type) {
		this.type = type;
	}
}
