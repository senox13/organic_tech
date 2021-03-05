package com.github.senox13.organic_tech.datagen;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.items.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class Items extends ItemModelProvider{
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
		withExistingParent(ModItems.FLESH_BLOCK.get().getRegistryName().getPath(), new ResourceLocation(MODID, "block/flesh_block"));
		withExistingParent(ModItems.FLESH_BLOCK_EYE.get().getRegistryName().getPath(), new ResourceLocation(MODID, "block/flesh_block_eye"));
	}

}
