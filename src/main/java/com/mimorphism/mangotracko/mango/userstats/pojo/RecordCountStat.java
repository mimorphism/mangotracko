package com.mimorphism.mangotracko.mango.userstats.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordCountStat {
	private int backlog;
	private int ctlyReading;
	private int finished;

}
