package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.senox13.organic_tech.blocks.ModBlocks;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class BlockStates extends BlockStateProvider{
	/*
	 * Fields
	 */
	public static final String BLOCK_DIR = ModelProvider.BLOCK_FOLDER;
	
	
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
		registerFleshBlock();
	}
	
	
	/*
	 * Private methods
	 */
	private void registerFleshBlock(){
		getVariantBuilder(ModBlocks.FLESH_BLOCK.get()).forAllStates(state -> {
			return ConfiguredModel.allRotations(models().cubeAll("flesh_block", modLoc(BLOCK_DIR + "/flesh_block")), false);
		});
	}
}
