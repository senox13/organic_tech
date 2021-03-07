package com.github.senox13.organic_tech.blocks;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ModBlocks{
	/*
	 * Fields
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block", () -> new Block(Properties.from(Blocks.SLIME_BLOCK)));
	
	
	/*
	 * Methods
	 */
	public static void register(){
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
