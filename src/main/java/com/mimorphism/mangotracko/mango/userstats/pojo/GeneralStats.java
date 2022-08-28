package com.mimorphism.mangotracko.mango.userstats.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralStats {
	private String days;
	private int totalMangoes;
	private int totalChapters;
	private int totalBacklog;
	private int totalCurrentlyReading;
	private int totalFinished;

}
