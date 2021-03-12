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
import net.minecraft.item.Item;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.SurvivesExplosion;
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
            Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
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
	            .filter(entity -> Optional.ofNullable(entity.getRegistryName())
	                .filter(registryName -> registryName.getNamespace().equals(MODID)).isPresent()
	            ).collect(Collectors.toList());
	    }
		
		@Override
	    protected void addTables(){
			registerBlockItemDrop(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechItems.FLESH_BLOCK.get());
			registerBlockItemDrop(OrganicTechBlocks.ARTERY.get(), OrganicTechItems.ARTERY.get());
			registerBlockItemDrop(OrganicTechBlocks.VEIN.get(), OrganicTechItems.VEIN.get());
		}
	}
}
