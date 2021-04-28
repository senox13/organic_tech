package com.github.senox13.organic_tech.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.fluids.OrganicTechFluids;

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
	//TODO: Custom BlockItem subclass for these that bridge connections between exisiting pipes
	public static final RegistryObject<Item> ARTERY = ITEMS.register("artery", () -> new BlockItem(OrganicTechBlocks.ARTERY.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> VEIN = ITEMS.register("vein", () -> new BlockItem(OrganicTechBlocks.VEIN.get(), new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> COMBUSTIVE_HEART = ITEMS.register("combustive_heart", () -> new BlockItem(OrganicTechBlocks.COMBUSTIVE_HEART.get(), new Item.Properties().group(ITEM_GROUP)));
	
	
	/*
	 * Items
	 */
	public static final RegistryObject<Item> SCALPEL = ITEMS.register("scalpel", () -> new ScalpelItem(ItemTier.IRON, 3, -1.8f, new Item.Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> COW_STOMACH = ITEMS.register("cow_stomach", () -> new Item(new Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> COW_HEART = ITEMS.register("cow_heart", () -> new Item(new Properties().group(ITEM_GROUP)));
	public static final RegistryObject<Item> ARTERIAL_BLOOD_BUCKET = ITEMS.register("arterial_blood_bucket", () -> new BucketItem(OrganicTechFluids.ARTERIAL_BLOOD, new Properties().group(ITEM_GROUP).maxStackSize(1)));
	public static final RegistryObject<Item> VENOUS_BLOOD_BUCKET = ITEMS.register("venous_blood_bucket", () -> new BucketItem(OrganicTechFluids.VENOUS_BLOOD, new Properties().group(ITEM_GROUP).maxStackSize(1)));
	
	/*
	 * Methods
	 */
	public static void register(IEventBus bus){
		ITEMS.register(bus);
	}
}
