package com.github.senox13.organic_tech.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechTileEntities{
	/*
	 * Fields
	 */
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
	public static final RegistryObject<TileEntityType<?>> HEART_FURNACE = TILE_ENTITIES.register("heart_furnace", () -> {
		return TileEntityType.Builder.create(HeartFurnaceTileEntity::new, OrganicTechBlocks.HEART_FURNACE.get()).build(null);
	});
	
	
	/*
	 * Methods
	 */
	public static void register(){
		TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
