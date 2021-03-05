package com.github.senox13.organic_tech.datagen;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.ModBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class BlockStates extends BlockStateProvider{
	/*
	 * Constructor
	 */
	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, MODID, exFileHelper);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void registerStatesAndModels(){
		simpleBlock(ModBlocks.FLESH_BLOCK.get());
		simpleBlock(ModBlocks.FLESH_BLOCK_EYE.get());
	}

}
