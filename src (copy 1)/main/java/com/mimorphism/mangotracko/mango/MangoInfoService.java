package com.mimorphism.mangotracko.mango;

import java.net.UnknownHostException;

import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;

public interface MangoInfoService {
	
	MangoInfo getMangoInfoByTitle(String mangoTitle) throws AnilistMangoNotFoundException;
	
	MangoInfo getMangoInfoByAnilistId(Long anilistId) throws AnilistMangoNotFoundException;


	
}
