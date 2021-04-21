package com.github.senox13.organic_tech.blocks.containers;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechContainers{
	/*
	 * Fields
	 */
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
	public static final RegistryObject<ContainerType<?>> HEART_FURNACE = CONTAINERS.register("heart_furnace", () -> IForgeContainerType.create((windowId, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		World world = inv.player.getEntityWorld();
		return new HeartFurnaceContainer(windowId, world, pos, inv, inv.player);
	}));
	
	
	/*
	 * Methods
	 */
	public static void register(IEventBus bus){
		CONTAINERS.register(bus);
	}
}
