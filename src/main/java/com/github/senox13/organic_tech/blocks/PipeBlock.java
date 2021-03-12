package com.github.senox13.organic_tech.blocks;

import com.github.senox13.organic_tech.items.OrganicTechItems;
import com.github.senox13.organic_tech.items.ScalpelItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public class PipeBlock extends SixWayBlock{ //TODO: This should eventually be waterloggable
	/*
	 * Fields
	 */
	public static final float APOTHEM = 3f/16f;
	public final String connectableTagName;
	
	
	/*
	 * Constructor
	 */
	public PipeBlock(String connectableTagNameIn, Properties properties){
		super(APOTHEM, properties);
		if(connectableTagNameIn.indexOf(':') == -1){
			connectableTagNameIn = MODID + ":" + connectableTagNameIn;
		}
		this.connectableTagName = connectableTagNameIn;
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(NORTH, Boolean.valueOf(false))
			.with(EAST, Boolean.valueOf(false))
			.with(SOUTH, Boolean.valueOf(false))
			.with(WEST, Boolean.valueOf(false))
			.with(UP, Boolean.valueOf(false))
			.with(DOWN, Boolean.valueOf(false)));
	}
	
	
	/*
	 * Private/protected methods
	 */
	private boolean canConnectToBlock(Block blockIn){
		ITag<Block> tag = BlockTags.getCollection().get(new ResourceLocation(connectableTagName)); //TODO: Use cached tag
		if(tag == null){
			return false;
		}
		return tag.contains(blockIn);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		Direction connectionDir = context.getFace();
		BlockPos adjacentPos = context.getPos().offset(connectionDir.getOpposite());
		Block adjacentBlock = context.getWorld().getBlockState(adjacentPos).getBlock();
		boolean doConnection = canConnectToBlock(adjacentBlock);
		if(adjacentBlock instanceof PipeBlock){
			BlockState newState = context.getWorld().getBlockState(adjacentPos).with(FACING_TO_PROPERTY_MAP.get(connectionDir), true);
			context.getWorld().setBlockState(adjacentPos, newState);
		}
		BlockState state = this.getDefaultState().with(FACING_TO_PROPERTY_MAP.get(connectionDir.getOpposite()), doConnection);
		return state;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
    	if(stateIn.get(FACING_TO_PROPERTY_MAP.get(facing))){
			if(!canConnectToBlock(facingState.getBlock())){
				return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), false);
			}
		}
		return stateIn;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote() && player.getHeldItem(handIn).getItem() instanceof ScalpelItem){
			if(player.isSneaking()){
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
				serverPlayer.interactionManager.tryHarvestBlock(pos);
				ItemStack heldItem = player.getHeldItem(handIn);
				heldItem.attemptDamageItem(1, player.getRNG(), serverPlayer);
				worldIn.playSound(null, pos, SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
			}else{
				Direction hitFace = hit.getFace();
				if(!state.get(FACING_TO_PROPERTY_MAP.get(hitFace))){
					BlockPos adjacentPos = pos.offset(hitFace);
					BlockState adjacentState = worldIn.getBlockState(adjacentPos);
					if(canConnectToBlock(adjacentState.getBlock())){
						worldIn.setBlockState(pos, state.with(FACING_TO_PROPERTY_MAP.get(hitFace), true));
						if(adjacentState.getBlock() instanceof PipeBlock){
							worldIn.setBlockState(adjacentPos, adjacentState.with(FACING_TO_PROPERTY_MAP.get(hitFace.getOpposite()), true));
						}
						ItemStack heldItem = player.getHeldItem(handIn);
						heldItem.attemptDamageItem(1, player.getRNG(), (ServerPlayerEntity)player);
						worldIn.playSound(player, pos, SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
					}
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
