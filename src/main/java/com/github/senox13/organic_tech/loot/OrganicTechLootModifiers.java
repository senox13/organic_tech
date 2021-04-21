package com.github.senox13.organic_tech.loot;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechLootModifiers{
	/*
	 * Fields
	 */
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);
	public static final RegistryObject<GlobalLootModifierSerializer<?>> APPEND_LOOT = LOOT_MODIFIER_SERIALIZERS.register("append_loot", () -> new AppendTableLootModifier.Serializer());
	
	
	/*
	 * Methods
	 */
	public static void register(IEventBus bus){
		LOOT_MODIFIER_SERIALIZERS.register(bus);
	}
}
