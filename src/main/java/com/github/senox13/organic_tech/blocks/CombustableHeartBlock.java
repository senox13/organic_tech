package com.github.senox13.organic_tech.blocks;


import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

import com.github.senox13.organic_tech.blocks.properties.BloodConnectionType;

public class CombustableHeartBlock extends Block{
	/*
	 * Fields
	 */
	private static final Direction[] FACING_VALUES = Direction.values(); //DOWN, UP, NORTH, SOUTH, EAST, WEST
	public static final EnumProperty<BloodConnectionType> NORTH = EnumProperty.create("north", BloodConnectionType.class);
	public static final EnumProperty<BloodConnectionType> EAST = EnumProperty.create("east", BloodConnectionType.class);
	public static final EnumProperty<BloodConnectionType> SOUTH = EnumProperty.create("south", BloodConnectionType.class);
	public static final EnumProperty<BloodConnectionType> WEST = EnumProperty.create("west", BloodConnectionType.class);
	public static final EnumProperty<BloodConnectionType> UP = EnumProperty.create("up", BloodConnectionType.class);
	public static final EnumProperty<BloodConnectionType> DOWN = EnumProperty.create("down", BloodConnectionType.class);
	public static final Map<Direction, EnumProperty<BloodConnectionType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
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
			.with(NORTH, BloodConnectionType.NONE)
			.with(EAST, BloodConnectionType.NONE)
			.with(SOUTH, BloodConnectionType.NONE)
			.with(WEST, BloodConnectionType.NONE)
			.with(UP, BloodConnectionType.NONE)
			.with(DOWN, BloodConnectionType.NONE)
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
	 * Override methods
	 */
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		int index = 0;
		for (int facingBitIndex = 0; facingBitIndex < FACING_VALUES.length; ++facingBitIndex){
			if(state.get(FACING_TO_PROPERTY_MAP.get(FACING_VALUES[facingBitIndex])) != BloodConnectionType.NONE){
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
			return this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir), BloodConnectionType.VEIN);
		}else if(adjacentBlock == OrganicTechBlocks.ARTERY.get()){
			return this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir), BloodConnectionType.ARTERY);
		}
		return this.getDefaultState();
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
		EnumProperty<BloodConnectionType> facingProp = FACING_TO_PROPERTY_MAP.get(facing);
		BloodConnectionType existingConnection = stateIn.get(facingProp);
		if(facingState.getBlock() == OrganicTechBlocks.VEIN.get() && facingState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))){
			if(existingConnection != BloodConnectionType.VEIN){
				return stateIn.with(facingProp, BloodConnectionType.VEIN);
			}
    	}else if(facingState.getBlock() == OrganicTechBlocks.ARTERY.get() && facingState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))){
    		if(existingConnection != BloodConnectionType.ARTERY){
    			return stateIn.with(facingProp, BloodConnectionType.ARTERY);
    		}
    	}else if(existingConnection != BloodConnectionType.NONE){
    		return stateIn.with(facingProp, BloodConnectionType.NONE);
    	}
		return stateIn;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
		for(Direction dir : Direction.values()){
			BloodConnectionType currentConnection = state.get(FACING_TO_PROPERTY_MAP.get(dir));
			BlockPos adjacentPos = pos.offset(dir);
			BlockState adjacentState = worldIn.getBlockState(adjacentPos);
			Block adjacentBlock = adjacentState.getBlock();
			
			if(currentConnection == BloodConnectionType.VEIN){ //If this side is supposed to be a vein connection
				if(adjacentBlock != OrganicTechBlocks.VEIN.get()){ //If there isn't a vein to connect to, set this side to no connection and continue
					worldIn.setBlockState(pos, state.with(FACING_TO_PROPERTY_MAP.get(dir), BloodConnectionType.NONE));
					continue;
				}
				//Vein block is present, change its state if needed
				if(!adjacentState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()))){
					worldIn.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true));
				}
			}else if(currentConnection == BloodConnectionType.ARTERY){ //If this side is supposed to be an artery connection
				if(adjacentBlock != OrganicTechBlocks.ARTERY.get()){ //If there isn't an artery to connect to, set this side to no connection and continue
					worldIn.setBlockState(pos, state.with(FACING_TO_PROPERTY_MAP.get(dir), BloodConnectionType.NONE));
					continue;
				}
				//Artery block is present, change its state if needed
				if(!adjacentState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()))){
					worldIn.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true));
				}
			}
		}
	}
}
