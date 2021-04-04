package com.github.senox13.organic_tech.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import com.github.senox13.organic_tech.blocks.containers.HeartFurnaceContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import static com.github.senox13.organic_tech.OrganicTech.MODID;

public final class HeartFurnaceScreen extends ContainerScreen<HeartFurnaceContainer>{
	/*
	 * Fields
	 */
	private static final ResourceLocation guiTexture = new ResourceLocation(MODID, "textures/gui/heart_furnace.png");
	
	
	/*
	 * Constructor
	 */
	public HeartFurnaceScreen(HeartFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}
	
	
	/*
	 * Private methods
	 */
	public boolean isBurning(){
		return this.container.furnaceData.get(0) > 0;
	}
	
	public int getBurnRemainingScaled(){
		int i = this.container.furnaceData.get(1);
		if(i == 0){
			i = 200;
		}
		return this.container.furnaceData.get(0) * 13 / i;
	}
   
   
	/*
	 * Override methods
	 */
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y){
		this.minecraft.getTextureManager().bindTexture(guiTexture);
        int left = this.guiLeft;
        int top = this.guiTop;
        this.blit(matrixStack, left, top, 0, 0, this.xSize, this.ySize);
        if(this.isBurning()){
        	int k = getBurnRemainingScaled();
    		this.blit(matrixStack, left + 81, top + 49 - k, 176, 12 - k, 14, k + 1);
        }
	}
}
