package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.util.OrganicTechTags;

public final class BlockTags extends BlockTagsProvider{
	/*
	 * Constructor
	 */
	public BlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper){
		super(generatorIn, MODID, existingFileHelper);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	public String getName(){
		return super.getName() + "<" + MODID + ">";
	}
	
	@Override
	protected void registerTags(){
		getOrCreateBuilder(OrganicTechTags.ARTERY_CONNECTABLE)
			.add(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechBlocks.ARTERY.get());
		getOrCreateBuilder(OrganicTechTags.VEIN_CONNECTABLE)
			.add(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechBlocks.VEIN.get());
	}
}
