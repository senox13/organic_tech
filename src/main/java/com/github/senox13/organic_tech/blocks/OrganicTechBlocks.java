package com.github.senox13.organic_tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.fluids.OrganicTechFluids;
import com.github.senox13.organic_tech.util.OrganicTechTags;

public final class OrganicTechBlocks{
	/*
	 * Fields
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final ToolType SCALPEL_TOOLTYPE = ToolType.get("scalpel");
	public static final RegistryObject<Block> FLESH_BLOCK = BLOCKS.register("flesh_block", () -> new Block(Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> ARTERY = BLOCKS.register("artery", () -> new PipeBlock(OrganicTechTags.ARTERY_CONNECTABLE, Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> VEIN = BLOCKS.register("vein", () -> new PipeBlock(OrganicTechTags.VEIN_CONNECTABLE, Properties.create(Material.ORGANIC, MaterialColor.BLUE).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> COMBUSTIVE_HEART = BLOCKS.register("combustive_heart", () -> new CombustableHeartBlock(Properties.create(Material.ORGANIC, MaterialColor.RED).sound(SoundType.SLIME).notSolid().harvestTool(SCALPEL_TOOLTYPE).hardnessAndResistance(0.5f)));
	public static final RegistryObject<Block> HEART_FURNACE = BLOCKS.register("heart_furnace", () -> new HeartFurnaceBlock(Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.5F)));
	public static final RegistryObject<Block> ARTERIAL_BLOOD = BLOCKS.register("arterial_blood", () -> new FlowingFluidBlock(OrganicTechFluids.ARTERIAL_BLOOD, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));
	public static final RegistryObject<Block> VENOUS_BLOOD = BLOCKS.register("venous_blood", () -> new FlowingFluidBlock(OrganicTechFluids.VENOUS_BLOOD, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));
	
	
	/*
	 * Methods
	 */
	public static void register(IEventBus bus){
		BLOCKS.register(bus);
	}
}
