package com.github.senox13.organic_tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechBlocks{
	/*
	 * Fields
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final ToolType SCALPEL_TOOLTYPE = ToolType.get("scalpel");
	public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block", () -> new Block(Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> ARTERY = BLOCKS.register("artery", () -> new PipeBlock("artery_connectable", Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> VEIN = BLOCKS.register("vein", () -> new PipeBlock("vein_connectable", Properties.create(Material.ORGANIC, MaterialColor.BLUE).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> COMBUSTIVE_HEART = BLOCKS.register("combustive_heart", () -> new CombustableHeartBlock(Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> HEART_FURNACE = BLOCKS.register("heart_furnace", () -> new HeartFurnaceBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.5F)));
	
	
	/*
	 * Methods
	 */
	public static void register(){
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
