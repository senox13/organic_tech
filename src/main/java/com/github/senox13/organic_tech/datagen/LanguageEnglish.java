package com.github.senox13.organic_tech.datagen;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.ModBlocks;
import com.github.senox13.organic_tech.items.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class LanguageEnglish extends LanguageProvider{
	/*
	 * Constructor
	 */
	public LanguageEnglish(DataGenerator gen){
		super(gen, MODID, "en_us");
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void addTranslations(){
		add("itemGroup." + ModItems.ITEM_GROUP.getPath(), "Organic Tech");
		add(ModBlocks.FLESH_BLOCK.get(), "Flesh Block");
		add(ModBlocks.ARTERY.get(), "Artery");
		add(ModBlocks.VEIN.get(), "Vein");
	}
}
