package com.github.senox13.organic_tech.datagen;


import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.items.OrganicTechItems;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import java.util.Map;
import java.util.Optional;

public final class LootTables extends LootTableProvider{
	/*
	 * Constructor
	 */
	public LootTables(DataGenerator dataGeneratorIn){
		super(dataGeneratorIn);
	}
	
	
	/*
	 * Override methods
	 */
	@Override
    public String getName(){
        return super.getName() + "<" + MODID + ">";
    }
	
	@Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables(){
        return Lists.newArrayList(
            Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK),
            Pair.of(ModEntityLootTables::new, LootParameterSets.ENTITY)
        );
    }
	
	//TODO: Find out what happens when this is removed
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker){}
	
	
	/*
	 * Nested classes
	 */
	private class ModBlockLootTables extends BlockLootTables{
		/*
		 * Private methods
		 */
		private void registerBlockItemDrop(Block block, Item item){
			this.registerLootTable(block, LootTable.builder()
				.addLootPool(LootPool.builder() 
					.rolls(ConstantRange.of(1))
					.addEntry(ItemLootEntry.builder(item))
					.acceptCondition(SurvivesExplosion.builder())
				)
			);
		}
		
		
		/*
		 * Override methods
		 */
		@Override
	    protected Iterable<Block> getKnownBlocks(){
	        return ForgeRegistries.BLOCKS.getValues().stream()
	            .filter(block -> Optional.ofNullable(block.getRegistryName())
	                .filter(registryName -> registryName.getNamespace().equals(MODID)).isPresent()
	            ).collect(Collectors.toList());
	    }
		
		@Override
	    protected void addTables(){
			registerBlockItemDrop(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechItems.FLESH_BLOCK.get());
			registerBlockItemDrop(OrganicTechBlocks.ARTERY.get(), OrganicTechItems.ARTERY.get());
			registerBlockItemDrop(OrganicTechBlocks.VEIN.get(), OrganicTechItems.VEIN.get());
			registerBlockItemDrop(OrganicTechBlocks.COMBUSTIVE_HEART.get(), OrganicTechItems.COMBUSTIVE_HEART.get());
			registerBlockItemDrop(OrganicTechBlocks.HEART_FURNACE.get(), Items.FURNACE);
		}
	}
	
	private class ModEntityLootTables extends EntityLootTables{
		@Override
	    protected Iterable<EntityType<?>> getKnownEntities(){
	        return ForgeRegistries.ENTITIES.getValues().stream()
	            .filter(entity -> Optional.ofNullable(entity.getRegistryName())
	                .filter(registryName -> registryName.getNamespace().equals(MODID)).isPresent()
	            ).collect(Collectors.toList());
	    }
		
		@Override
	    protected void addTables(){
			registerLootTable(new ResourceLocation(MODID, "entities/cow_organs"), LootTable.builder()
				.addLootPool(LootPool.builder() 
					.rolls(ConstantRange.of(1))
					.addEntry(
						ItemLootEntry.builder(OrganicTechItems.COW_STOMACH.get())
							.acceptFunction(SetCount.builder(RandomValueRange.of(0, 1)))
							.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
					)
				).addLootPool(LootPool.builder()
					.rolls(ConstantRange.of(1))
					.addEntry(ItemLootEntry.builder(OrganicTechItems.COW_HEART.get()))
				)
			);
		}
	}
}
