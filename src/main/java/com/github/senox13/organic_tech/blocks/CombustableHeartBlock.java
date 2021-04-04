package com.github.senox13.organic_tech.blocks;


import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import com.github.senox13.organic_tech.blocks.properties.HeartConnectionType;

//TODO: This generates a huge number of BlockStates. Should probably look into TERs as an alternative
public class CombustableHeartBlock extends Block{
	/*
	 * Fields
	 */
	private static final Direction[] FACING_VALUES = Direction.values(); //DOWN, UP, NORTH, SOUTH, EAST, WEST
	public static final EnumProperty<HeartConnectionType> NORTH = EnumProperty.create("north", HeartConnectionType.class);
	public static final EnumProperty<HeartConnectionType> EAST = EnumProperty.create("east", HeartConnectionType.class);
	public static final EnumProperty<HeartConnectionType> SOUTH = EnumProperty.create("south", HeartConnectionType.class);
	public static final EnumProperty<HeartConnectionType> WEST = EnumProperty.create("west", HeartConnectionType.class);
	public static final EnumProperty<HeartConnectionType> UP = EnumProperty.create("up", HeartConnectionType.class);
	public static final EnumProperty<HeartConnectionType> DOWN = EnumProperty.create("down", HeartConnectionType.class);
	public static final Map<Direction, EnumProperty<HeartConnectionType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
	      directions.put(Direction.NORTH, NORTH);
	      directions.put(Direction.EAST, EAST);
	      directions.put(Direction.SOUTH, SOUTH);
	      directions.put(Direction.WEST, WEST);
	      directions.put(Direction.UP, UP);
	      directions.put(Direction.DOWN, DOWN);
	   });
	public static final float APOTHEM = 5f/16f;
	protected final VoxelShape[] shapes;
	
	
	/*
	 * Constructor
	 */
	public CombustableHeartBlock(Properties properties){
		super(properties);
		this.shapes = this.makeShapes(PipeBlock.APOTHEM, CombustableHeartBlock.APOTHEM);
		this.setDefaultState(getStateContainer().getBaseState()
			.with(NORTH, HeartConnectionType.NONE)
			.with(EAST, HeartConnectionType.NONE)
			.with(SOUTH, HeartConnectionType.NONE)
			.with(WEST, HeartConnectionType.NONE)
			.with(UP, HeartConnectionType.NONE)
			.with(DOWN, HeartConnectionType.NONE)
		);
	}
	
	
	/*
	 * Private methods
	 */
	private VoxelShape[] makeShapes(float pipeApothem, float centerApothem){
		float centerApothemNeg = 0.5F - centerApothem;
		float centerApothemPos= 0.5F + centerApothem;
		VoxelShape centerCube = Block.makeCuboidShape(
			(double) (centerApothemNeg * 16.0F), (double) (centerApothemNeg * 16.0F), (double) (centerApothemNeg * 16.0F),
			(double) (centerApothemPos * 16.0F), (double) (centerApothemPos * 16.0F), (double) (centerApothemPos * 16.0F)
		);
		VoxelShape[] directionShapes = new VoxelShape[FACING_VALUES.length];
		for (int facingIndex = 0; facingIndex < FACING_VALUES.length; ++facingIndex){
			Direction direction = FACING_VALUES[facingIndex];
			directionShapes[facingIndex] = VoxelShapes.create(
				0.5D + Math.min((double) (-pipeApothem), (double) direction.getXOffset() * 0.5D),
				0.5D + Math.min((double) (-pipeApothem), (double) direction.getYOffset() * 0.5D),
				0.5D + Math.min((double) (-pipeApothem), (double) direction.getZOffset() * 0.5D),
				0.5D + Math.max((double) pipeApothem, (double) direction.getXOffset() * 0.5D),
				0.5D + Math.max((double) pipeApothem, (double) direction.getYOffset() * 0.5D),
				0.5D + Math.max((double) pipeApothem, (double) direction.getZOffset() * 0.5D)
			);
		}
		VoxelShape[] generatedShapes = new VoxelShape[64];
		for(int shapeIndex = 0; shapeIndex < 64; ++shapeIndex){ //0b000000 through 0b111111, each bit representing a direction in FACING_VALUES
			VoxelShape newShape = centerCube;
			for(int facingBit = 0; facingBit < FACING_VALUES.length; ++facingBit){
				if((shapeIndex & 1 << facingBit) != 0){ //If this direction's bit is set, add its shape to the sum
					newShape = VoxelShapes.or(newShape, directionShapes[facingBit]);
				}
			}
			generatedShapes[shapeIndex] = newShape;
		}
		return generatedShapes;
	}
	
	
	/*
	 * Public methods
	 */
	public boolean hasFurnaceConnection(BlockState state){
		for(Direction facing : Direction.values()){
			if(state.get(FACING_TO_PROPERTY_MAP.get(facing)) == HeartConnectionType.FURNACE){
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * Override methods
	 */
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		int index = 0;
		for (int facingBitIndex = 0; facingBitIndex < FACING_VALUES.length; ++facingBitIndex){
			if(state.get(FACING_TO_PROPERTY_MAP.get(FACING_VALUES[facingBitIndex])) != HeartConnectionType.NONE){
				index |= 1 << facingBitIndex;
			}
		}
		return this.shapes[index];
	}
	
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
		return false;
	}
	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type){
		return false;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		Direction connectionDir = context.getPlacementHorizontalFacing();
		BlockPos adjacentPos = context.getPos().offset(connectionDir);
		Block adjacentBlock = context.getWorld().getBlockState(adjacentPos).getBlock();
		if(adjacentBlock == OrganicTechBlocks.VEIN.get()){
			return this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir), HeartConnectionType.VEIN);
		}else if(adjacentBlock == OrganicTechBlocks.ARTERY.get()){
			return this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir), HeartConnectionType.ARTERY);
		}
		return this.getDefaultState();
	}
	
	//TODO: A lot of spaghetti from this point down. This whole class needs a refactor
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
		EnumProperty<HeartConnectionType> facingProp = FACING_TO_PROPERTY_MAP.get(facing);
		HeartConnectionType existingConnection = stateIn.get(facingProp);
		if(facingState.getBlock() == OrganicTechBlocks.VEIN.get() && facingState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))){
			//Attach to adjacent veins with an existing connection
			if(existingConnection != HeartConnectionType.VEIN){
				return stateIn.with(facingProp, HeartConnectionType.VEIN);
			}
    	}else if(facingState.getBlock() == OrganicTechBlocks.ARTERY.get() && facingState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))){
    		//Attach to adjacent arteries with an adjacent connection 
    		if(existingConnection != HeartConnectionType.ARTERY){
    			return stateIn.with(facingProp, HeartConnectionType.ARTERY);
    		}
    	}else if(facingState.getBlock() != OrganicTechBlocks.HEART_FURNACE.get() && existingConnection != HeartConnectionType.NONE){
    		return stateIn.with(facingProp, HeartConnectionType.NONE);
    	}else if(facingState.getBlock() == Blocks.FURNACE && !hasFurnaceConnection(stateIn)){
    		//Switch out furnace block for overlay version
    		BlockState furnaceState = OrganicTechBlocks.HEART_FURNACE.get().getDefaultState()
				.with(HeartFurnaceBlock.CONNECTION_FACING, facing.getOpposite())
				.with(HeartFurnaceBlock.FACING, facingState.get(FurnaceBlock.FACING));
    		//TODO: This currently doesn't check if anything is in the existing furnace, which will need to be fixed
    		worldIn.setBlockState(facingPos, furnaceState, 1);
    		return stateIn.with(facingProp, HeartConnectionType.FURNACE);
    	}
		return stateIn;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
		BlockState newState = state;
		for(Direction dir : Direction.values()){
			HeartConnectionType currentConnection = state.get(FACING_TO_PROPERTY_MAP.get(dir));
			BlockPos adjacentPos = pos.offset(dir);
			BlockState adjacentState = worldIn.getBlockState(adjacentPos);
			Block adjacentBlock = adjacentState.getBlock();
			if(currentConnection == HeartConnectionType.VEIN){ //If this side is supposed to be a vein connection
				//If there isn't a vein to connect to, set this side to no connection and continue
				if(adjacentBlock != OrganicTechBlocks.VEIN.get()){
					newState = newState.with(FACING_TO_PROPERTY_MAP.get(dir), HeartConnectionType.NONE);
					continue;
				}
				//Vein block is present, change its state if needed
				if(!adjacentState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()))){
					worldIn.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true));
				}
			}else if(currentConnection == HeartConnectionType.ARTERY){ //If this side is supposed to be an artery connection
				//If there isn't an artery to connect to, set this side to no connection and continue
				if(adjacentBlock != OrganicTechBlocks.ARTERY.get()){
					newState = newState.with(FACING_TO_PROPERTY_MAP.get(dir), HeartConnectionType.NONE);
					continue;
				}
				//Artery block is present, change its state if needed
				if(!adjacentState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()))){
					worldIn.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true));
				}
			}
			
			if(adjacentBlock == Blocks.FURNACE && !hasFurnaceConnection(newState)){
				BlockState furnaceState = OrganicTechBlocks.HEART_FURNACE.get().getDefaultState()
					.with(HeartFurnaceBlock.FACING, adjacentState.get(FurnaceBlock.FACING))
					.with(HeartFurnaceBlock.CONNECTION_FACING, dir.getOpposite());
				worldIn.setBlockState(adjacentPos, furnaceState, 1);
				newState = newState.with(FACING_TO_PROPERTY_MAP.get(dir), HeartConnectionType.FURNACE);
			}	
		}
		if(newState != state){
			worldIn.setBlockState(pos, newState);
		}
	}
}
