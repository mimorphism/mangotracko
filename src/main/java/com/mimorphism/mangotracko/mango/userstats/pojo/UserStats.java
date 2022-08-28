package com.mimorphism.mangotracko.mango.userstats.pojo;
/**
 * User stats class
 *  
 * @author mimo
 * @version 1.0
 * @since Aug 27, 2022
 */

import java.util.HashMap;
import java.util.Map;

import com.mimorphism.mangotracko.mango.Backlog;
import com.mimorphism.mangotracko.mango.CurrentlyReading;
import com.mimorphism.mangotracko.mango.Finished;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {
	
	
	private GeneralStats generalStats;
	private MostRecentUpdatesStats mostRecentUpdateStats;
	private Map<String, RecordCountStat> dailyStats;
	
}
