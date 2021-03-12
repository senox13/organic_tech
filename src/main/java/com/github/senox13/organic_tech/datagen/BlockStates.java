package com.github.senox13.organic_tech.datagen;

import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.PipeBlock;

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
		//Base models
		generatePipeModels();
		
		//BlockStates + block-specific models
		registerFleshBlock();
		registerPipeBlock(OrganicTechBlocks.ARTERY.get(), "artery");
		registerPipeBlock(OrganicTechBlocks.VEIN.get(), "vein");
	}
	
	
	/*
	 * Model generating methods
	 */
	@SuppressWarnings("rawtypes")
	private void generatePipeModels(){
		//Pipe center model
		models().getBuilder(BLOCK_DIR + "/pipe_center")
			.element().from(5, 5, 5).to(11, 11, 11).allFaces((dir, face) -> face.texture("#side")).end();
		
		//X+ facing pipe segment, to be rotated in blockstate json
		ElementBuilder segmentBuilder = models().getBuilder(BLOCK_DIR + "/pipe_segment").texture("particle", "#side")
			.element().from(5, 5, 11).to(11, 11, 16);
		for(Direction dir : Direction.values()){
			if(dir.getAxis() == Direction.Axis.Z)continue;
			segmentBuilder.face(dir).texture("#side");
		}
		
		//Pipe inventory model
		models().withExistingParent("pipe_inventory", BLOCK_DIR + "/block")
			.element().from(5, 0, 5).to(11, 16, 11).allFaces((dir, face) -> {
				face.texture("#side");
			});
	}
	
	
	/*
	 * BlockState generator methods
	 */
	private void registerFleshBlock(){
		getVariantBuilder(OrganicTechBlocks.FLESH_BLOCK.get()).forAllStates(state -> {
			//Generate the set of rotations for a flesh block w/o eyes
			ConfiguredModel[] rotations = ConfiguredModel.allRotations(models().cubeAll("flesh_block", modLoc(BLOCK_DIR + "/flesh_block")), false, 4);
			//then append the rotations for one eye
			rotations = ArrayUtils.addAll(rotations, ConfiguredModel.allRotations(models().cubeAll("flesh_block_1eye", modLoc(BLOCK_DIR + "/flesh_block_1eye")), false, 1));
			//then append the rotations for two eyes and return
			return ArrayUtils.addAll(rotations, ConfiguredModel.allRotations(models().cubeAll("flesh_block_2eye", modLoc(BLOCK_DIR + "/flesh_block_2eye")), false, 1));
		});
	}
	
	private void registerPipeBlock(Block block, String textureName){
		if(!(block instanceof PipeBlock)){
			throw new IllegalArgumentException("Non-PipeBlock passed to registerPipeModel()");
		}
		PipeBlock pipeBlock = (PipeBlock)block;
		
		ResourceLocation textureLocation = modLoc(BLOCK_DIR + "/" + textureName);
		
		//Generate center model
		String centerName = pipeBlock.getRegistryName().getPath() + "_center";
		BlockModelBuilder center = models().withExistingParent(centerName, modLoc(BLOCK_DIR + "/pipe_center"))
			.texture("side", textureLocation)
			.texture("particle", textureLocation);
		
		//Generate segment model
		String segmentName = pipeBlock.getRegistryName().getPath() + "_segment";
		BlockModelBuilder segment = models().withExistingParent(segmentName, modLoc(BLOCK_DIR + "/pipe_segment"))
			.texture("side", textureLocation)
			.texture("particle", textureLocation);
		
		//Generate BlockState JSON
		MultiPartBlockStateBuilder multipart = getMultipartBuilder(pipeBlock);
		multipart.part().modelFile(center).addModel();
		multipart.part().modelFile(segment).uvLock(true).addModel().condition(PipeBlock.SOUTH, true);
		multipart.part().modelFile(segment).uvLock(true).rotationY(90).addModel().condition(PipeBlock.WEST, true);
		multipart.part().modelFile(segment).uvLock(true).rotationY(180).addModel().condition(PipeBlock.NORTH, true);
		multipart.part().modelFile(segment).uvLock(true).rotationY(270).addModel().condition(PipeBlock.EAST, true);
		multipart.part().modelFile(segment).uvLock(true).rotationX(90).addModel().condition(PipeBlock.UP, true);
		multipart.part().modelFile(segment).uvLock(true).rotationX(270).addModel().condition(PipeBlock.DOWN, true);
		
	}
}
