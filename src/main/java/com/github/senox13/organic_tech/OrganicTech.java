package com.github.senox13.organic_tech;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.items.OrganicTechItems;
import com.github.senox13.organic_tech.loot.OrganicTechLootModifiers;

import net.minecraftforge.fml.common.Mod;

@Mod(OrganicTech.MODID)
public final class OrganicTech{
	/*
	 * Fields
	 */
	public static final String MODID = "organic_tech";
    
	
	/*
	 * Constructor
	 */
    public OrganicTech(){
    	OrganicTechBlocks.register();
    	OrganicTechItems.register();
    	OrganicTechLootModifiers.register();
    }
}
