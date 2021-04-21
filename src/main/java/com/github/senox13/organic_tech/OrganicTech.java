package com.github.senox13.organic_tech;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.containers.OrganicTechContainers;
import com.github.senox13.organic_tech.events.ClientEventHandlers;
import com.github.senox13.organic_tech.fluids.OrganicTechFluids;
import com.github.senox13.organic_tech.items.OrganicTechItems;
import com.github.senox13.organic_tech.loot.OrganicTechLootModifiers;
import com.github.senox13.organic_tech.tileentity.OrganicTechTileEntities;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
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
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	//Register registry event listeners
    	OrganicTechBlocks.register(bus);
    	OrganicTechItems.register(bus);
    	OrganicTechTileEntities.register(bus);
    	OrganicTechContainers.register(bus);
    	OrganicTechLootModifiers.register(bus);
    	bus.addGenericListener(Fluid.class, OrganicTechFluids::register);
    	//Register other event handlers
    	bus.addListener(ClientEventHandlers::ClientSetup);
    }
}
