package com.github.senox13.organic_tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

import com.github.senox13.organic_tech.blocks.containers.HeartFurnaceContainer;
import com.github.senox13.organic_tech.tileentity.HeartFurnaceTileEntity;
import com.github.senox13.organic_tech.tileentity.OrganicTechTileEntities;

public class HeartFurnaceBlock extends Block{
	/*
	 * Fields
	 */
	public static final DirectionProperty FACING = AbstractFurnaceBlock.FACING;
	public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;
	public static final DirectionProperty CONNECTION_FACING = DirectionProperty.create("connection", Direction.values());
	
	
	/*
	 * Constructor
	 */
	public HeartFurnaceBlock(Properties properties){
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(LIT, false));
	}
	
	
	/*
	 * Override methods
	 */
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder){
		builder.add(FACING, LIT, CONNECTION_FACING);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player){
		return new ItemStack(Items.FURNACE);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockPos heartPos = currentPos.offset(stateIn.get(CONNECTION_FACING));
		if(worldIn.getBlockState(heartPos).getBlock() != OrganicTechBlocks.COMBUSTIVE_HEART.get()){
			return Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, stateIn.get(FACING));
		}
		return stateIn;
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(worldIn.isRemote()){
			return ActionResultType.SUCCESS;
		}
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(!(tileEntity instanceof HeartFurnaceTileEntity)){
			throw new IllegalStateException("Got unexpected TE type: " + tileEntity.getType().getRegistryName());
		}
		INamedContainerProvider containerProvider = new INamedContainerProvider(){
			@Override
			public ITextComponent getDisplayName(){
				return new TranslationTextComponent("container.furnace");
			}
			
			@Override
			public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player){
				return new HeartFurnaceContainer(windowId, player.getEntityWorld(), pos, playerInv, player);
			}
		};
		NetworkHooks.openGui((ServerPlayerEntity)player, containerProvider, pos);
		return ActionResultType.CONSUME;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return OrganicTechTileEntities.HEART_FURNACE.get().create();
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(LIT) ? 13 : 0;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if(stateIn.get(LIT)){
	         double d0 = (double)pos.getX() + 0.5D;
	         double d1 = (double)pos.getY();
	         double d2 = (double)pos.getZ() + 0.5D;
	         if(rand.nextDouble() < 0.1D){
	            worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
	         }
	         Direction direction = stateIn.get(FACING);
	         Direction.Axis direction$axis = direction.getAxis();
	         double d4 = rand.nextDouble() * 0.6D - 0.3D;
	         double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
	         double d6 = rand.nextDouble() * 6.0D / 16.0D;
	         double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;
	         worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
	         worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
	      }
	}
}
