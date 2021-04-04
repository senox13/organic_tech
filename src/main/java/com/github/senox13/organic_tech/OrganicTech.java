package com.github.senox13.organic_tech;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.containers.OrganicTechContainers;
import com.github.senox13.organic_tech.events.ClientEventHandlers;
import com.github.senox13.organic_tech.items.OrganicTechItems;
import com.github.senox13.organic_tech.loot.OrganicTechLootModifiers;
import com.github.senox13.organic_tech.tileentity.OrganicTechTileEntities;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
    	//Register DeferredRegisters
    	OrganicTechBlocks.register();
    	OrganicTechItems.register();
    	OrganicTechTileEntities.register();
    	OrganicTechContainers.register();
    	OrganicTechLootModifiers.register();
    	//Register other event handlers
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandlers::ClientSetup);
    }
}
