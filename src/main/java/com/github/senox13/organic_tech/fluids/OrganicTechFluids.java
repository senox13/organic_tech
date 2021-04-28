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
	public static final RegistryObject<FlowingFluid> ARTERIAL_BLOOD = RegistryObject.of(modLoc("arterial_blood"), ForgeRegistries.FLUIDS);
	public static final RegistryObject<FlowingFluid> ARTERIAL_BLOOD_FLOWING = RegistryObject.of(modLoc("arterial_blood_flowing"), ForgeRegistries.FLUIDS);
	public static final RegistryObject<FlowingFluid> VENOUS_BLOOD = RegistryObject.of(modLoc("venous_blood"), ForgeRegistries.FLUIDS);
	public static final RegistryObject<FlowingFluid> VENOUS_BLOOD_FLOWING = RegistryObject.of(modLoc("venous_blood_flowing"), ForgeRegistries.FLUIDS);
	
	
	/*
	 * Methods
	 */
	private static ResourceLocation modLoc(String pathIn){
		return new ResourceLocation(MODID, pathIn);
	}
	
	public static void register(RegistryEvent.Register<Fluid> event){
		//Register arterial blood
		//TODO: Values copied from milk, look into what these do
		FluidAttributes.Builder arterialBloodAttributes = FluidAttributes.builder(modLoc("block/arterial_blood_still"), modLoc("block/arterial_blood_flow")).density(1024).viscosity(1024);
		ForgeFlowingFluid.Properties arterialBloodProperties = new ForgeFlowingFluid.Properties(ARTERIAL_BLOOD, ARTERIAL_BLOOD_FLOWING, arterialBloodAttributes).block(() -> (FlowingFluidBlock)OrganicTechBlocks.ARTERIAL_BLOOD.get()).bucket(OrganicTechItems.ARTERIAL_BLOOD_BUCKET);
		event.getRegistry().register(new ForgeFlowingFluid.Source(arterialBloodProperties).setRegistryName(ARTERIAL_BLOOD.getId()));
		event.getRegistry().register(new ForgeFlowingFluid.Flowing(arterialBloodProperties).setRegistryName(ARTERIAL_BLOOD_FLOWING.getId()));
		//Register venous blood
		FluidAttributes.Builder venousBloodAttributes = FluidAttributes.builder(modLoc("block/venous_blood_still"), modLoc("block/venous_blood_flow")).density(1024).viscosity(1024);
		ForgeFlowingFluid.Properties venousBloodProperties = new ForgeFlowingFluid.Properties(VENOUS_BLOOD, VENOUS_BLOOD_FLOWING, venousBloodAttributes).block(() -> (FlowingFluidBlock)OrganicTechBlocks.VENOUS_BLOOD.get()).bucket(OrganicTechItems.VENOUS_BLOOD_BUCKET);
		event.getRegistry().register(new ForgeFlowingFluid.Source(venousBloodProperties).setRegistryName(VENOUS_BLOOD.getId()));
		event.getRegistry().register(new ForgeFlowingFluid.Flowing(venousBloodProperties).setRegistryName(VENOUS_BLOOD_FLOWING.getId()));
	}
}
