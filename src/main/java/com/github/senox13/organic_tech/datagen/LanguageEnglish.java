package com.github.senox13.organic_tech.datagen;

import static com.github.senox13.organic_tech.OrganicTech.MODID;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.items.OrganicTechItems;

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
		add("itemGroup." + OrganicTechItems.ITEM_GROUP.getPath(), "Organic Tech");
		add(OrganicTechBlocks.FLESH_BLOCK.get(), "Flesh Block");
		add(OrganicTechBlocks.ARTERY.get(), "Artery");
		add(OrganicTechBlocks.VEIN.get(), "Vein");
		add(OrganicTechItems.SCALPEL.get(), "Scalpel");
		add(OrganicTechItems.COW_STOMACH.get(), "Cow Stomach");
	}
}
