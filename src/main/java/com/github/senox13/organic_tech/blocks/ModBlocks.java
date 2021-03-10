package com.github.senox13.organic_tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class ModBlocks{
	/*
	 * Fields
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block", () -> new Block(Properties.from(Blocks.SLIME_BLOCK)));
	public static final RegistryObject<Block> ARTERY = BLOCKS.register("artery", () -> new PipeBlock(Properties.create(Material.MISCELLANEOUS).notSolid()));
	public static final RegistryObject<Block> VEIN = BLOCKS.register("vein", () -> new PipeBlock(Properties.create(Material.MISCELLANEOUS).notSolid()));
	
	
	/*
	 * Methods
	 */
	public static void register(){
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
