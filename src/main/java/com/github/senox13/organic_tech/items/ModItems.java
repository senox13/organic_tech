package com.github.senox13.organic_tech.items;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.ModBlocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems{
	/*
	 * Fields
	 */
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID){
		@Override
		public ItemStack createIcon(){
			return new ItemStack(FLESH_BLOCK.get());
		}
	};
	public static final RegistryObject<Item> FLESH_BLOCK = ITEMS.register("flesh_block", () -> new BlockItem(ModBlocks.FLESH_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> ARTERY = ITEMS.register("artery", () -> new BlockItem(ModBlocks.ARTERY.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> VEIN = ITEMS.register("vein", () -> new BlockItem(ModBlocks.VEIN.get(), new Item.Properties().group(ITEM_GROUP)));
	
	
	/*
	 * Methods
	 */
	public static void register(){
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
