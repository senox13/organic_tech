package com.github.senox13.organic_tech.tileentity;

import com.github.senox13.organic_tech.blocks.HeartFurnaceBlock;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public final class HeartFurnaceTileEntity extends TileEntity implements ITickableTileEntity{
	/*
	 * Fields
	 */
	private static final String TAG_NAME_INVENTORY = "inventory";
	private static final String TAG_NAME_TIME_REMAINING = "burnTimeRemaining";
	private static final String TAG_NAME_TIME_INITIAL= "burnTimeInitial";
	private ItemStackHandler itemHandler = createHandler();
	private LazyOptional<IItemHandler> itemHandlerOptional = LazyOptional.of(() -> itemHandler);
	private int burnTimeInitial;
	private int burnTimeRemaining;
	public final IIntArray furnaceData = new IIntArray(){
		@Override
		public int size(){
			return 2;
		}
		
		@Override
		public void set(int index, int value){
			switch(index){
			case 0:
				burnTimeRemaining = value;
				break;
			case 1:
				burnTimeInitial = value;
				break;
			default:
				throw new IndexOutOfBoundsException(index);
			}
		}
		
		@Override
		public int get(int index){
			switch(index){
			case 0:
				return burnTimeRemaining;
			case 1:
				return burnTimeInitial;
			}
			throw new IndexOutOfBoundsException(index);
		}
	};
	
	
	/*
	 * Constructor
	 */
	public HeartFurnaceTileEntity(){
		super(OrganicTechTileEntities.HEART_FURNACE.get());
	}
	
	
	/*
	 * Private methods
	 */
	private ItemStackHandler createHandler(){
		return new ItemStackHandler(1){
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
			
			@Override
			public boolean isItemValid(int slot, ItemStack stack){
				return ForgeHooks.getBurnTime(stack) > 0;
			}
			
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
				if(ForgeHooks.getBurnTime(stack) == 0){
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
		};
	}
	
	
	/*
	 * Public methods
	 */
	public boolean isBurning(){
		return this.burnTimeRemaining > 0;
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	public void read(BlockState state, CompoundNBT nbt){
		itemHandler.deserializeNBT(nbt.getCompound(TAG_NAME_INVENTORY));
		burnTimeInitial = nbt.getInt(TAG_NAME_TIME_INITIAL);
		burnTimeRemaining = nbt.getInt(TAG_NAME_TIME_REMAINING);
		super.read(state, nbt);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		compound.put(TAG_NAME_INVENTORY, itemHandler.serializeNBT());
		compound.putInt(TAG_NAME_TIME_INITIAL, burnTimeInitial);
		compound.putInt(TAG_NAME_TIME_INITIAL, burnTimeRemaining);
		return super.write(compound);
	}
	
	@Override
	public void remove(){
		super.remove();
		itemHandlerOptional.invalidate();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap){
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return itemHandlerOptional.cast();
		}
		return super.getCapability(cap);
	}
	
	@Override
	public void tick(){
		boolean wasBurningInitially = this.isBurning();
		boolean hasChanged = false;
		//Decrement burn time
		if(isBurning()){
			burnTimeRemaining--;
		}
		if(!world.isRemote()){ //TODO: This could still use some refactoring and comments
			ItemStack fuelStack = this.itemHandler.getStackInSlot(0);
			if(this.isBurning() || !fuelStack.isEmpty()){
				int stackBurnTime = ForgeHooks.getBurnTime(fuelStack);
				if(!this.isBurning()){
					this.burnTimeInitial = stackBurnTime;
					this.burnTimeRemaining = stackBurnTime;
					if(this.isBurning()){
						hasChanged = true;
						if(fuelStack.hasContainerItem()){
							this.itemHandler.setStackInSlot(0, fuelStack.getContainerItem());
						}else if(!fuelStack.isEmpty()){
							fuelStack.shrink(1);
							if(fuelStack.isEmpty()){
								this.itemHandler.setStackInSlot(0, fuelStack.getContainerItem());
							}
						}
					}
				}
			}
			if(wasBurningInitially != this.isBurning()){
				hasChanged = true;
				BlockState newState = this.world.getBlockState(this.pos).with(HeartFurnaceBlock.LIT, this.isBurning());
				this.world.setBlockState(this.pos, newState, BlockFlags.DEFAULT);
			}
		}
		if(hasChanged){
			markDirty();
		}
		//TODO: Make the rest of the vascular network work
	}
}
