package com.github.senox13.organic_tech.items;

import java.util.Set;

import com.github.senox13.organic_tech.blocks.CombustableHeartBlock;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.PipeBlock;
import com.github.senox13.organic_tech.blocks.properties.BloodConnectionType;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ScalpelItem extends ToolItem{
	/*
	 * Fields
	 */
	private static final Set<Block> EFFECTIVE_ON_BLOCKS = Sets.newHashSet(
		OrganicTechBlocks.FLESH_BLOCK.get(), //TODO: I don't like how hardcoded this is, replace with getDestroySpeed and a block tag
		OrganicTechBlocks.ARTERY.get(),
		OrganicTechBlocks.VEIN.get(),
		OrganicTechBlocks.COMBUSTIVE_HEART.get()
	);
	
	
	/*
	 * Constructor
	 */
	public ScalpelItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builderIn){
		super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON_BLOCKS, builderIn.addToolType(ToolType.get("scalpel"), tier.getHarvestLevel()));
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	public ActionResultType onItemUse(ItemUseContext context){Direction hitFace = context.getFace();
		World world = context.getWorld();
		BlockState hitState = world.getBlockState(context.getPos());
		if(hitState.getBlock() instanceof PipeBlock){
			PipeBlock pipe = (PipeBlock)hitState.getBlock();
			if(!hitState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(hitFace))){
				BlockPos adjacentPos = context.getPos().offset(hitFace);
				BlockState adjacentState = world.getBlockState(adjacentPos);
				if(pipe.canConnectToBlock(adjacentState.getBlock())){
					world.setBlockState(context.getPos(), hitState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(hitFace), true)); //Make connection
					/*if(adjacentState.getBlock() instanceof PipeBlock){
						world.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(hitFace.getOpposite()), true));
					}*/
					world.playSound(context.getPlayer(), context.getPos(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
					return ActionResultType.SUCCESS;
				}
			}
			return ActionResultType.PASS;
		}else{ //If the hit block is not a connectable type, check if the adjacent block is and can be connected
			BlockPos adjacentPos = context.getPos().offset(hitFace);
			BlockState adjacentState = world.getBlockState(adjacentPos);
			if(adjacentState.getBlock() instanceof PipeBlock){
				PipeBlock pipe = (PipeBlock)adjacentState.getBlock();
				if(!adjacentState.get(PipeBlock.FACING_TO_PROPERTY_MAP.get(hitFace.getOpposite()))){
					if(pipe.canConnectToBlock(hitState.getBlock())){
						world.setBlockState(adjacentPos, adjacentState.with(PipeBlock.FACING_TO_PROPERTY_MAP.get(hitFace.getOpposite()), true));
						world.playSound(context.getPlayer(), context.getPos(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}
}
