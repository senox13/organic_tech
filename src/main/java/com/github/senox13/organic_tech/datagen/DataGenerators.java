package com.github.senox13.organic_tech.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event){
		DataGenerator generator = event.getGenerator();
		if(event.includeClient()){
			generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
			generator.addProvider(new Items(generator, event.getExistingFileHelper()));
			generator.addProvider(new LanguageEnglish(generator));
		}
		if(event.includeServer()){
			//TODO: Datagen GLMs
			generator.addProvider(new LootTables(generator));
			generator.addProvider(new BlockTags(generator, event.getExistingFileHelper()));
		}
	}
}
