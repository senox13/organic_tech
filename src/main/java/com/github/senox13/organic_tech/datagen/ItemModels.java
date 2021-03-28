package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.senox13.organic_tech.items.OrganicTechItems;

import static com.github.senox13.organic_tech.OrganicTech.MODID;
import static com.github.senox13.organic_tech.datagen.BlockStates.BLOCK_DIR;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;

public final class ItemModels extends ItemModelProvider{
	/*
	 * Fields
	 */
	public static final String ITEM_DIR = "item";
	
	
	/*
	 * Constructor
	 */
	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper){
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
		withExistingParent(OrganicTechItems.COMBUSTIVE_HEART.get().getRegistryName().getPath(), modLoc(BLOCK_DIR + "/" + OrganicTechBlocks.COMBUSTIVE_HEART.get().getRegistryName().getPath()));
		
		/*
		 * Item models
		 */
		withExistingParent(OrganicTechItems.SCALPEL.get().getRegistryName().getPath(), mcLoc(ITEM_DIR + "/handheld"))
			.texture("layer0", modLoc(ITEM_DIR + "/scalpel"));
		withExistingParent(OrganicTechItems.COW_STOMACH.get().getRegistryName().getPath(), mcLoc(ITEM_DIR + "/generated"))
			.texture("layer0", modLoc(ITEM_DIR + "/cow_stomach"));
		withExistingParent(OrganicTechItems.COW_HEART.get().getRegistryName().getPath(), mcLoc(ITEM_DIR + "/generated"))
			.texture("layer0", modLoc(ITEM_DIR + "/cow_heart"));
	}

}
