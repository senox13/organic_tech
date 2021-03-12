package com.github.senox13.organic_tech.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class OrganicTechItems{
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
	
	
	/*
	 * BlockItems
	 */
	public static final RegistryObject<Item> FLESH_BLOCK = ITEMS.register("flesh_block", () -> new BlockItem(OrganicTechBlocks.FLESH_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> ARTERY = ITEMS.register("artery", () -> new BlockItem(OrganicTechBlocks.ARTERY.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> VEIN = ITEMS.register("vein", () -> new BlockItem(OrganicTechBlocks.VEIN.get(), new Item.Properties().group(ITEM_GROUP)));
	
	
	/*
	 * Items
	 */
	public static final ToolType SCALPEL_TOOLTYPE = ToolType.get("scalpel");
	//TODO: This needs to damage over time, a max stack size of 1, and a better hand position
	public static final RegistryObject<Item> SCALPEL = ITEMS.register("scalpel", () -> new Item(new Item.Properties().group(ITEM_GROUP).addToolType(SCALPEL_TOOLTYPE, 2)));
	
	
	/*
	 * Methods
	 */
	public static void register(){
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
