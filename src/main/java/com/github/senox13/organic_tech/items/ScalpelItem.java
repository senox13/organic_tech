  package com.github.senox13.organic_tech.items;

import java.util.Collections;
import com.github.senox13.organic_tech.blocks.PipeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;


public class ScalpelItem extends ToolItem{
	/*
	 * Constructor
	 */
	public ScalpelItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builderIn){
		super(attackDamageIn, attackSpeedIn, tier, Collections.emptySet(), builderIn.addToolType(ToolType.get("scalpel"), tier.getHarvestLevel()));
		
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
