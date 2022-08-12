package com.mimorphism.mangotracko.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mimorphism.mangotracko.mango.Mango;
import com.mimorphism.mangotracko.mango.MangoRepo;

public final class MangoUtil {
	

	public static final boolean isMangoExist(MangoRepo repo, String title) 
	{
		Mango existing = repo.findByMangoTitle(title);
		
		return existing != null;
		
	}
	
	/**
	 * Convert the name to the proper order of Asian name (LastName FirstName)
	 * @param name
	 * @return
	 */
	public static String convertProperName(String name) 
	{
		List<String> nameOrder = new ArrayList<String>(Arrays.asList(name.split("\\s+")));
		String lastName = nameOrder.get(nameOrder.size() - 1);
		nameOrder.remove(nameOrder.size()-1);
		String firstName = String.join("", nameOrder);
		return lastName + " " + firstName;
	}

}
