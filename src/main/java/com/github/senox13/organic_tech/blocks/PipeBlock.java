package com.github.senox13.organic_tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class PipeBlock extends SixWayBlock{ //TODO: This should eventually be waterloggable
	/*
	 * Fields
	 */
	public static final float APOTHEM = 3f/16f;
	
	
	/*
	 * Constructor
	 */
	public PipeBlock(Properties properties){
		super(APOTHEM, properties);
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(NORTH, Boolean.valueOf(false))
			.with(EAST, Boolean.valueOf(false))
			.with(SOUTH, Boolean.valueOf(false))
			.with(WEST, Boolean.valueOf(false))
			.with(UP, Boolean.valueOf(false))
			.with(DOWN, Boolean.valueOf(false)));
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.makeConnections(context.getWorld(), context.getPos());
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
    	boolean connectSide = facingState.getBlock() == this; //TODO: Use a block tag here
        return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(connectSide));
	}
	
	
	/*
	 * Private methods
	 */
	public BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
		Block blockDown = blockReader.getBlockState(pos.down()).getBlock();
		Block blockUp = blockReader.getBlockState(pos.up()).getBlock();
		Block blockNorth = blockReader.getBlockState(pos.north()).getBlock();
		Block blockEast = blockReader.getBlockState(pos.east()).getBlock();
		Block blockSouth = blockReader.getBlockState(pos.south()).getBlock();
		Block blockWest = blockReader.getBlockState(pos.west()).getBlock();
		return this.getDefaultState() //TODO: Use block tag for this
			.with(DOWN, Boolean.valueOf(blockDown == this))
			.with(UP, Boolean.valueOf(blockUp == this))
			.with(NORTH, Boolean.valueOf(blockNorth == this))
			.with(EAST, Boolean.valueOf(blockEast == this))
			.with(SOUTH, Boolean.valueOf(blockSouth == this))
			.with(WEST, Boolean.valueOf(blockWest == this));
	}
	
}
