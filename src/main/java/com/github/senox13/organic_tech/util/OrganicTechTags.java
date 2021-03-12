package com.github.senox13.organic_tech.util;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechTags{
	/*
	 * Block tags
	 */
	public static final INamedTag<Block> ARTERY_CONNECTABLE = blockTag("artery_connectable");
	public static final INamedTag<Block> VEIN_CONNECTABLE = blockTag("vein_connectable");
	
	
	/*
	 * Private methods
	 */
	private static INamedTag<Block> blockTag(String tagName){
		ResourceLocation tagLocation = new ResourceLocation(MODID, tagName);
		return BlockTags.makeWrapperTag(tagLocation.toString());
	}
}
