package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.senox13.organic_tech.blocks.ModBlocks;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import org.apache.commons.lang3.ArrayUtils;

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
			//Generate the set of rotations for a flesh block w/o eyes
			ConfiguredModel[] rotations = ConfiguredModel.allRotations(models().cubeAll("flesh_block", modLoc(BLOCK_DIR + "/flesh_block")), false, 4);
			//then append the rotations for one eye
			rotations = ArrayUtils.addAll(rotations, ConfiguredModel.allRotations(models().cubeAll("flesh_block_1eye", modLoc(BLOCK_DIR + "/flesh_block_1eye")), false, 1));
			//then append the rotations for two eyes and return
			return ArrayUtils.addAll(rotations, ConfiguredModel.allRotations(models().cubeAll("flesh_block_2eye", modLoc(BLOCK_DIR + "/flesh_block_2eye")), false, 1));
		});
	}
}
