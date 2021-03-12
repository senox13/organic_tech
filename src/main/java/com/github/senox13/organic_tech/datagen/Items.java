package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.senox13.organic_tech.items.OrganicTechItems;

import static com.github.senox13.organic_tech.OrganicTech.MODID;
import static com.github.senox13.organic_tech.datagen.BlockStates.BLOCK_DIR;

public final class Items extends ItemModelProvider{
	/*
	 * Fields
	 */
	public static final String ITEM_DIR = "item";
	
	
	/*
	 * Constructor
	 */
	public Items(DataGenerator generator, ExistingFileHelper existingFileHelper){
		super(generator, MODID, existingFileHelper);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void registerModels(){
		/*
		 * BlockItem models
		 */
		withExistingParent(OrganicTechItems.FLESH_BLOCK.get().getRegistryName().getPath(), modLoc(BLOCK_DIR + "/flesh_block"));
		withExistingParent(OrganicTechItems.ARTERY.get().getRegistryName().getPath(), modLoc(BLOCK_DIR + "/pipe_inventory"))
			.texture("side", BLOCK_DIR + "/artery");
		withExistingParent(OrganicTechItems.VEIN.get().getRegistryName().getPath(), modLoc(BLOCK_DIR + "/pipe_inventory"))
			.texture("side", BLOCK_DIR + "/vein");
		
		/*
		 * Item models
		 */
		withExistingParent(OrganicTechItems.SCALPEL.get().getRegistryName().getPath(), mcLoc(ITEM_DIR + "/handheld"))
			.texture("layer0", modLoc(ITEM_DIR + "/scalpel"));
	}

}
