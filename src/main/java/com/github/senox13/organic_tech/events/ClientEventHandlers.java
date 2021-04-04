package com.github.senox13.organic_tech.events;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.blocks.containers.HeartFurnaceContainer;
import com.github.senox13.organic_tech.blocks.containers.OrganicTechContainers;
import com.github.senox13.organic_tech.gui.HeartFurnaceScreen;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public final class ClientEventHandlers{
    @SuppressWarnings("unchecked")
	public static void ClientSetup(FMLClientSetupEvent event){
    	event.enqueueWork(() -> {
    		RenderTypeLookup.setRenderLayer(OrganicTechBlocks.HEART_FURNACE.get(), RenderType.getCutoutMipped());
    	});
    	ScreenManager.registerFactory((ContainerType<HeartFurnaceContainer>)OrganicTechContainers.HEART_FURNACE.get(), HeartFurnaceScreen::new);
    }
}
