package com.github.senox13.organic_tech.blocks.containers;

import com.github.senox13.organic_tech.blocks.OrganicTechBlocks;
import com.github.senox13.organic_tech.tileentity.HeartFurnaceTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public final class HeartFurnaceContainer extends Container{
	/*
	 * Fields
	 */
	private TileEntity tileEntity;
	public IIntArray furnaceData;
	private PlayerEntity player;
	private IItemHandler playerInventory;
	
	
	/*
	 * Constructor
	 */
	public HeartFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player){
		super(OrganicTechContainers.HEART_FURNACE.get(), windowId);
		this.tileEntity = world.getTileEntity(pos);
		this.furnaceData = ((HeartFurnaceTileEntity)tileEntity).furnaceData;
		this.player = player;
		this.playerInventory = new InvWrapper(playerInv);
		if(this.tileEntity != null){
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
				addSlot(new SlotItemHandler(itemHandler, 0, 80, 53));
			});
		}
		addPlayerInventorySlots(8, 84);
		trackIntArray(furnaceData);
	}
	
	
	/*
	 * Private methods
	 */
	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx){
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy){
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }
	
	private void addPlayerInventorySlots(int leftCol, int topRow){
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
	
	
	/*
	 * Override methods
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn){
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), this.player, OrganicTechBlocks.HEART_FURNACE.get());
	}
	
	//TODO: Comment everything that happens in this method
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index){
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()){
			ItemStack stack = slot.getStack();
			itemStack = stack.copy();
			if(index == 0){
				if(!this.mergeItemStack(stack, 1, 37, true)){
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, itemStack);
			}else{ //Slot clicked was in player inventory
				//If the clicked slot containes a fuel item, attempt to move it to the fuel slot
				if(ForgeHooks.getBurnTime(stack) > 0){
					if(!this.mergeItemStack(stack, 0, 1, false)){
                        return ItemStack.EMPTY;
                    }
                }else if (index < 28){
                    if(!this.mergeItemStack(stack, 28, 37, false)){
                        return ItemStack.EMPTY;
                    }
                }else if(index < 37 && !this.mergeItemStack(stack, 1, 28, false)){
                    return ItemStack.EMPTY;
                }
			}
			if(stack.isEmpty()){
				slot.putStack(ItemStack.EMPTY);
			}else{
				slot.onSlotChanged();
			}
			if(stack.getCount() == itemStack.getCount()){
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, stack);
		}
		return itemStack;
	}
}
