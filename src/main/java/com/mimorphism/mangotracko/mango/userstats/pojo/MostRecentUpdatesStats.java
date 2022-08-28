package com.mimorphism.mangotracko.mango.userstats.pojo;

import com.mimorphism.mangotracko.mango.Backlog;
import com.mimorphism.mangotracko.mango.CurrentlyReading;
import com.mimorphism.mangotracko.mango.Finished;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostRecentUpdatesStats {
	private Backlog mostRecentBacklog;
	private CurrentlyReading mostRecentCurrentlyReading;
	private Finished mostRecentFinished;

}
