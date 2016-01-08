package net.einsteinsci.betterbeginnings.blocks;

import java.util.Random;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNetherBrickOven extends BlockSpecializedFurnace
{
	public BlockNetherBrickOven(boolean lit)
	{
		super(Material.rock);

		if (lit)
		{
			setLightLevel(0.875F);
		}
		else
		{
			setLightLevel(0F);
			setCreativeTab(ModMain.tabBetterBeginnings);
		}

		setUnlocalizedName(getName());

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

		setHardness(4.0f);
		setResistance(10.0f);

		isLit = lit;
	}

	@Override
	public String getName()
	{
		return isLit ? "netherBrickOvenLit" : "netherBrickOven";
	}

	public static void updateBlockState(boolean lit, World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);

		if (lit)
		{
			world.setBlockState(pos, RegisterBlocks.netherBrickOvenLit.getDefaultState()
					.withProperty(FACING, state.getValue(FACING)), 3);
		}
		else
		{
			world.setBlockState(pos, RegisterBlocks.netherBrickOven.getDefaultState()
					.withProperty(FACING, state.getValue(FACING)), 3);
		}

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.netherBrickOven);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.NETHERBRICKOVEN_ID, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(RegisterBlocks.netherBrickOven);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityNetherBrickOven();
	}
}
