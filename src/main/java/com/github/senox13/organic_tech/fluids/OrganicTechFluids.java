package com.github.senox13.organic_tech.fluids;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.items.OrganicTechItems;


public final class OrganicTechFluids{
	/*
	 * Fields
	 */
	public static final RegistryObject<FlowingFluid> BLOOD = RegistryObject.of(modLoc("blood"), ForgeRegistries.FLUIDS);
	public static final RegistryObject<FlowingFluid> BLOOD_FLOWING = RegistryObject.of(modLoc("blood_flowing"), ForgeRegistries.FLUIDS);
	
	
	/*
	 * Methods
	 */
	private static ResourceLocation modLoc(String pathIn){
		return new ResourceLocation(MODID, pathIn);
	}
	
	public static void register(RegistryEvent.Register<Fluid> event){
		//TODO: Values copied from milk, look into what these do
		FluidAttributes.Builder bloodAttributes = FluidAttributes.builder(modLoc("block/blood_still"), modLoc("block/blood_flow")).density(1024).viscosity(1024);
		ForgeFlowingFluid.Properties bloodProperties = new ForgeFlowingFluid.Properties(BLOOD, BLOOD_FLOWING, bloodAttributes).block(() -> (FlowingFluidBlock)OrganicTechBlocks.BLOOD.get()).bucket(OrganicTechItems.BLOOD_BUCKET);
		event.getRegistry().register(new ForgeFlowingFluid.Source(bloodProperties).setRegistryName(BLOOD.getId()));
		event.getRegistry().register(new ForgeFlowingFluid.Flowing(bloodProperties).setRegistryName(BLOOD_FLOWING.getId()));
	}
}
