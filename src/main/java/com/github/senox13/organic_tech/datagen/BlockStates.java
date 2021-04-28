package com.github.senox13.organic_tech.datagen;

import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Orientation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import com.github.senox13.organic_tech.blocks.CombustableHeartBlock;
import com.github.senox13.organic_tech.blocks.HeartFurnaceBlock;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.PipeBlock;
import com.github.senox13.organic_tech.blocks.properties.HeartConnectionType;
import static com.github.senox13.organic_tech.OrganicTech.MODID;

import java.util.HashMap;
import java.util.Map;

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
		registerCombustiveHeart();
		registerHeartFurnace();
		registerFluid(OrganicTechBlocks.ARTERIAL_BLOOD.get(),modLoc(BLOCK_DIR + "/arterial_blood_still"));
		registerFluid(OrganicTechBlocks.VENOUS_BLOOD.get(),modLoc(BLOCK_DIR + "/venous_blood_still"));
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
		for(Direction dir : Direction.values()){  //TODO: This adds an extra face inside the center model
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
	
	private void registerCombustiveHeart(){
		Block heartBlock = OrganicTechBlocks.COMBUSTIVE_HEART.get();
		
		//Generate center model
		BlockModelBuilder center = models().withExistingParent(BLOCK_DIR + "/" + heartBlock.getRegistryName().getPath(), BLOCK_DIR + "/block")
			.element().from(3, 3, 3).to(13, 13, 13).allFaces((dir, face) -> face.texture("#side")).end()
			.texture("side", BLOCK_DIR + "/combustive_heart")
			.texture("particle", BLOCK_DIR + "/combustive_heart");
		
		//Get existing vein/artery pipe segments
		String arterySegmentPath = BLOCK_DIR + "/" + OrganicTechBlocks.ARTERY.get().getRegistryName().getPath() + "_segment";
		ExistingModelFile arterySegment = models().getExistingFile(modLoc(arterySegmentPath));
		
		String veinSegmentPath = BLOCK_DIR + "/" + OrganicTechBlocks.VEIN.get().getRegistryName().getPath() + "_segment";
		ExistingModelFile veinSegment = models().getExistingFile(modLoc(veinSegmentPath));
		
		//Generate furnace connection component
		BlockModelBuilder furnaceSegment = models().getBuilder(BLOCK_DIR + "/heart_furnace_connection")
			.element().from(5, 5, 11).to(11, 11, 15)
				.allFaces((dir, face) -> face.texture("#side")).end()
			.element().from(4, 4, 15).to(12, 12, 16)
				.allFaces((dir, face) -> face.texture("#side")).end() //TODO: This adds an extra face where it will always be culled
			.texture("side", modLoc(BLOCK_DIR + "/artery"))
			.texture("particle", modLoc(BLOCK_DIR + "/artery"));
		
		//Generate BlockState JSON
		MultiPartBlockStateBuilder multipart = getMultipartBuilder(heartBlock);
		multipart.part().modelFile(center).addModel();
		//Add artery segments
		multipart.part().modelFile(arterySegment).uvLock(true).addModel().condition(CombustableHeartBlock.SOUTH, HeartConnectionType.ARTERY);
		multipart.part().modelFile(arterySegment).uvLock(true).rotationY(90).addModel().condition(CombustableHeartBlock.WEST, HeartConnectionType.ARTERY);
		multipart.part().modelFile(arterySegment).uvLock(true).rotationY(180).addModel().condition(CombustableHeartBlock.NORTH, HeartConnectionType.ARTERY);
		multipart.part().modelFile(arterySegment).uvLock(true).rotationY(270).addModel().condition(CombustableHeartBlock.EAST, HeartConnectionType.ARTERY);
		multipart.part().modelFile(arterySegment).uvLock(true).rotationX(90).addModel().condition(CombustableHeartBlock.UP, HeartConnectionType.ARTERY);
		multipart.part().modelFile(arterySegment).uvLock(true).rotationX(270).addModel().condition(CombustableHeartBlock.DOWN, HeartConnectionType.ARTERY);
		//Add vein segments
		multipart.part().modelFile(veinSegment).uvLock(true).addModel().condition(CombustableHeartBlock.SOUTH, HeartConnectionType.VEIN);
		multipart.part().modelFile(veinSegment).uvLock(true).rotationY(90).addModel().condition(CombustableHeartBlock.WEST, HeartConnectionType.VEIN);
		multipart.part().modelFile(veinSegment).uvLock(true).rotationY(180).addModel().condition(CombustableHeartBlock.NORTH, HeartConnectionType.VEIN);
		multipart.part().modelFile(veinSegment).uvLock(true).rotationY(270).addModel().condition(CombustableHeartBlock.EAST, HeartConnectionType.VEIN);
		multipart.part().modelFile(veinSegment).uvLock(true).rotationX(90).addModel().condition(CombustableHeartBlock.UP, HeartConnectionType.VEIN);
		multipart.part().modelFile(veinSegment).uvLock(true).rotationX(270).addModel().condition(CombustableHeartBlock.DOWN, HeartConnectionType.VEIN);
		//Add furnace connection segments
		multipart.part().modelFile(furnaceSegment).uvLock(true).addModel().condition(CombustableHeartBlock.SOUTH, HeartConnectionType.FURNACE);
		multipart.part().modelFile(furnaceSegment).uvLock(true).rotationY(90).addModel().condition(CombustableHeartBlock.WEST, HeartConnectionType.FURNACE);
		multipart.part().modelFile(furnaceSegment).uvLock(true).rotationY(180).addModel().condition(CombustableHeartBlock.NORTH, HeartConnectionType.FURNACE);
		multipart.part().modelFile(furnaceSegment).uvLock(true).rotationY(270).addModel().condition(CombustableHeartBlock.EAST, HeartConnectionType.FURNACE);
		multipart.part().modelFile(furnaceSegment).uvLock(true).rotationX(90).addModel().condition(CombustableHeartBlock.UP, HeartConnectionType.FURNACE);
		multipart.part().modelFile(furnaceSegment).uvLock(true).rotationX(270).addModel().condition(CombustableHeartBlock.DOWN, HeartConnectionType.FURNACE);
	}
	
	@SuppressWarnings("rawtypes")
	private void registerHeartFurnace(){
		//Generate north-oriented furnace models with overlay on each direction
		Map<Direction, BlockModelBuilder> models = new HashMap<Direction, BlockModelBuilder>();
		Map<Direction, BlockModelBuilder> litModels = new HashMap<Direction, BlockModelBuilder>();
		for(boolean lit : new boolean[]{true, false}){
			for(Direction dir : Direction.values()){
				String modelName = "heart_furnace_" + dir.toString();
				if(lit){
					modelName += "_on";
				}
				BlockModelBuilder model = models().withExistingParent(modelName, mcLoc(BLOCK_DIR + "/block"));
				//Create furnace element
				model.element().from(0, 0, 0).to(16, 16, 16).allFaces((faceDir, faceBuilder) -> {
					switch(faceDir){
						case NORTH:
							faceBuilder.cullface(faceDir).texture("#front");
							break;
						case UP:
						case DOWN:
							faceBuilder.cullface(faceDir).texture("#top");
							break;
						default:
							faceBuilder.cullface(faceDir).texture("#side");
					}
				});
				//Create overlay element
				ElementBuilder overlayElement = model.element().from(0, 0, 0).to(16, 16, 16);
				switch(dir){
					case UP:
						overlayElement.face(Direction.UP).cullface(Direction.UP).texture("#overlay_top").end()
						.face(Direction.NORTH).cullface(Direction.NORTH).texture("#overlay_side").end()
						.face(Direction.EAST).cullface(Direction.EAST).texture("#overlay_side").end()
						.face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#overlay_side").end()
						.face(Direction.WEST).cullface(Direction.WEST).texture("#overlay_side").end();
						break;
					case DOWN:
						overlayElement.face(Direction.DOWN).cullface(Direction.DOWN).texture("#overlay_top").end()
						.face(Direction.NORTH).cullface(Direction.NORTH).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end()
						.face(Direction.EAST).cullface(Direction.EAST).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end()
						.face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end()
						.face(Direction.WEST).cullface(Direction.WEST).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end();
						break;
					case EAST:
						overlayElement.face(Direction.EAST).cullface(Direction.EAST).texture("#overlay_top").end()
						.face(Direction.UP).cullface(Direction.UP).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end()
						.face(Direction.NORTH).cullface(Direction.NORTH).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
						.face(Direction.DOWN).cullface(Direction.DOWN).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end()
						.face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end();
						break;
					case NORTH:
						overlayElement.face(Direction.NORTH).cullface(Direction.NORTH).texture("#overlay_top").end()
						.face(Direction.UP).cullface(Direction.UP).texture("#overlay_side").rotation(FaceRotation.ZERO).end()
						.face(Direction.EAST).cullface(Direction.EAST).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end()
						.face(Direction.DOWN).cullface(Direction.DOWN).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end()
						.face(Direction.WEST).cullface(Direction.WEST).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
						break;
					case SOUTH:
						overlayElement.face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#overlay_top").end()
						.face(Direction.UP).cullface(Direction.UP).texture("#overlay_side").rotation(FaceRotation.UPSIDE_DOWN).end()
						.face(Direction.EAST).cullface(Direction.EAST).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
						.face(Direction.DOWN).cullface(Direction.DOWN).texture("#overlay_side").rotation(FaceRotation.ZERO).end()
						.face(Direction.WEST).cullface(Direction.WEST).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end();
						break;
					case WEST:
						overlayElement.face(Direction.WEST).cullface(Direction.WEST).texture("#overlay_top").end()
						.face(Direction.UP).cullface(Direction.UP).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
						.face(Direction.NORTH).cullface(Direction.NORTH).texture("#overlay_side").rotation(FaceRotation.CLOCKWISE_90).end()
						.face(Direction.DOWN).cullface(Direction.DOWN).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
						.face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#overlay_side").rotation(FaceRotation.COUNTERCLOCKWISE_90).end();
						break;
				}
				//Assign textures
				model.texture("front", mcLoc(BLOCK_DIR + (lit ? "/furnace_front_on" : "/furnace_front")))
					.texture("top", mcLoc(BLOCK_DIR + "/furnace_top"))
					.texture("side", mcLoc(BLOCK_DIR + "/furnace_side"))
					.texture("overlay_top", modLoc(BLOCK_DIR + "/flesh_block")) //TODO: Placeholder texture
					.texture("overlay_side", modLoc(BLOCK_DIR + "/furnace_overlay"))
					.texture("particle", mcLoc(BLOCK_DIR + "/furnace_front"));
				//Add to respective map
				if(lit){
					litModels.put(dir, model);
				}else{
					models.put(dir, model);
				}
			}
		}
		//Create map of directions for offsetting rotations
		Map<Direction, Orientation> yRotationOffsets = new HashMap<Direction, Orientation>();
		yRotationOffsets.put(Direction.NORTH, Orientation.IDENTITY);
		yRotationOffsets.put(Direction.EAST, Orientation.ROT_90_Y_POS);
		yRotationOffsets.put(Direction.SOUTH, Orientation.ROT_180_FACE_XZ);
		yRotationOffsets.put(Direction.WEST, Orientation.ROT_90_Y_NEG);
		//Build multipart json
		getVariantBuilder(OrganicTechBlocks.HEART_FURNACE.get()).forAllStates(state -> {
			//Get BlockState property values
			Direction connectionDir = state.get(HeartFurnaceBlock.CONNECTION_FACING);
			Direction facingDir = state.get(HeartFurnaceBlock.FACING);
			boolean lit = state.get(HeartFurnaceBlock.LIT);
			//Get blockstate JSON builder
			ConfiguredModel.Builder builder = ConfiguredModel.builder();
			switch(connectionDir){
				case UP:
				case DOWN:
					builder.modelFile((lit ? litModels.get(connectionDir) : models.get(connectionDir)))
						.rotationY(getYRotationFromFacing(facingDir));
					break;
				default:
					Direction adjustedDir = yRotationOffsets.get(facingDir).func_235530_a_(connectionDir);
					builder.modelFile((lit ? litModels.get(adjustedDir) : models.get(adjustedDir)))
						.rotationY(getYRotationFromFacing(facingDir));
			}
			return builder.build();
		});
	}
	
	private void registerFluid(Block fluidBlock, ResourceLocation particle){ //TODO: args
		ModelFile model = models().getBuilder(BLOCK_DIR + "/" + fluidBlock.getRegistryName().getPath())
			.texture("particle", particle);
		getVariantBuilder(fluidBlock).forAllStates(state -> {
			return ConfiguredModel.builder().modelFile(model).build();
		});
	}
	
	
	/*
	 * Utility methods
	 */
	private static int getYRotationFromFacing(Direction facing){
		switch(facing){
		case NORTH:
			return 0;
		case EAST:
			return 90;
		case SOUTH:
			return 180;
		case WEST:
			return 270;
		default:
			return 0;
		}
	}
}
