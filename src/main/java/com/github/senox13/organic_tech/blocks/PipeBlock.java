package com.github.senox13.organic_tech.blocks;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PipeBlock extends SixWayBlock{ //TODO: This should eventually be waterloggable
	/*
	 * Fields
	 */
	public static final float APOTHEM = 3f/16f;
	public final INamedTag<Block> connectableTag;
	
	
	/*
	 * Constructor
	 */
	public PipeBlock(INamedTag<Block> connectableTag, Properties properties){
		super(APOTHEM, properties);
		this.connectableTag = connectableTag;
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(NORTH, Boolean.valueOf(false))
			.with(EAST, Boolean.valueOf(false))
			.with(SOUTH, Boolean.valueOf(false))
			.with(WEST, Boolean.valueOf(false))
			.with(UP, Boolean.valueOf(false))
			.with(DOWN, Boolean.valueOf(false)));
	}
	
	
	/*
	 * Public methods
	 */
	public boolean canConnectToBlock(Block blockIn){
		return connectableTag.contains(blockIn);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){ //TODO: Automatic connection to clicked face for vertical placement, maybe only for connectable blocks
		Direction connectionDir = context.getPlacementHorizontalFacing();
		BlockPos adjacentPos = context.getPos().offset(connectionDir);
		Block adjacentBlock = context.getWorld().getBlockState(adjacentPos).getBlock();
		if(canConnectToBlock(adjacentBlock)){
			return this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir), true);
		}
		return this.getDefaultState();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack){
		for(Direction dir : Direction.values()){
			if(state.get(FACING_TO_PROPERTY_MAP.get(dir))){
				BlockPos adjacentPos = pos.offset(dir);
				BlockState adjacentState = worldIn.getBlockState(adjacentPos);
				Block adjacentBlock = adjacentState.getBlock();
				if(adjacentBlock instanceof PipeBlock && canConnectToBlock(adjacentBlock)){ //TODO: Hardcoded for now
					worldIn.setBlockState(adjacentPos, adjacentState.with(FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true));
				}
			}
		}
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
    	if(stateIn.get(FACING_TO_PROPERTY_MAP.get(facing))){
			if(!canConnectToBlock(facingState.getBlock())){
				return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), false);
			}
		}
    	
    	if(facingState.getBlock() instanceof PipeBlock && facingState.get(FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))){
    		PipeBlock pipe = (PipeBlock)facingState.getBlock();
    		if(pipe.canConnectToBlock(stateIn.getBlock())){
				return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), true);
			}
		}
		return stateIn;
	}
}
