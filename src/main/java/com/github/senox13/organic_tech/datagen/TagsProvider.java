package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.fluids.OrganicTechFluids;
import com.github.senox13.organic_tech.util.OrganicTechTags;

public final class TagsProvider{
	/*
	 * Methods
	 */
	public static void addProviders(DataGenerator generator, ExistingFileHelper existingFileHelper){
		generator.addProvider(new BlockTags(generator, existingFileHelper));
		generator.addProvider(new FluidTags(generator, existingFileHelper));
	}
	
	
	/*
	 * Nested types
	 */
	public static final class BlockTags extends BlockTagsProvider{ //TODO: Add FluidTags class, make both inner classes of Tags class
		/*
		 * Constructor
		 */
		public BlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper){
			super(generatorIn, MODID, existingFileHelper);
		}
		
		
		/*
		 * Override methods
		 */
		@Override
		public String getName(){
			return super.getName() + "<" + MODID + ">";
		}
		
		@Override
		protected void registerTags(){
			getOrCreateBuilder(OrganicTechTags.ARTERY_CONNECTABLE)
				.add(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechBlocks.ARTERY.get(), OrganicTechBlocks.COMBUSTIVE_HEART.get());
			getOrCreateBuilder(OrganicTechTags.VEIN_CONNECTABLE)
				.add(OrganicTechBlocks.FLESH_BLOCK.get(), OrganicTechBlocks.VEIN.get(), OrganicTechBlocks.COMBUSTIVE_HEART.get());
		}
	}
	
	public static final class FluidTags extends FluidTagsProvider{
		/*
		 * Constructor
		 */
		public FluidTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper){
			super(generatorIn, MODID, existingFileHelper);
		}
		
		
		/*
		 * Override methods
		 */
		@Override
		public String getName(){
			return super.getName() + "<" + MODID + ">";
		}
		
		@Override
		protected void registerTags(){
			this.getOrCreateBuilder(net.minecraft.tags.FluidTags.WATER).add(OrganicTechFluids.BLOOD.get(), OrganicTechFluids.BLOOD_FLOWING.get());
		}
	}
}
