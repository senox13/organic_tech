package com.github.senox13.organic_tech.items;

import java.util.Set;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class ScalpelItem extends ToolItem{
	/*
	 * Fields
	 */
	private static final Set<Block> EFFECTIVE_ON_BLOCKS = Sets.newHashSet(
		OrganicTechBlocks.FLESH_BLOCK.get(),
		OrganicTechBlocks.ARTERY.get(),
		OrganicTechBlocks.VEIN.get()
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
	public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player){
        return true;
    }
}
