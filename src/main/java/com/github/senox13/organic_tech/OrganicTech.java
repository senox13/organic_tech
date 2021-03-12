package com.github.senox13.organic_tech;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.items.OrganicTechItems;

import net.minecraftforge.fml.common.Mod;

@Mod(OrganicTech.MODID)
public final class OrganicTech{
	/*
	 * Fields
	 */
	public static final String MODID = "organic_tech";
    
	
	/*
	 * Methods
	 */
    public OrganicTech(){
    	OrganicTechBlocks.register();
    	OrganicTechItems.register();
    }
}
