package net.einsteinsci.betterbeginnings.network;

import io.netty.buffer.ByteBuf;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketNetherBrickOvenFuelLevel implements IMessage
{
	BlockPos pos;

	FluidStack fluid;

	public static class PacketHandler implements IMessageHandler<PacketNetherBrickOvenFuelLevel, IMessage>
	{
		@Override
		public IMessage onMessage(PacketNetherBrickOvenFuelLevel message, MessageContext ctx)
		{
			EntityPlayer player = ModMain.proxy.getPlayerFromMessageContext(ctx);

			TileEntityNetherBrickOven oven = (TileEntityNetherBrickOven)player.worldObj.getTileEntity(message.pos);
			oven.setFuelLevel(message.fluid);

			return null;
		}
	}

	public PacketNetherBrickOvenFuelLevel()
	{
		pos = BlockPos.ORIGIN;

		fluid = null;
	}

	public PacketNetherBrickOvenFuelLevel(BlockPos loc, FluidStack fuel)
	{
		pos = loc;

		fluid = fuel;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

		int level = buf.readInt();
		int fluidId = buf.readInt();

		if (level != 0)
		{
			fluid = new FluidStack(FluidRegistry.getFluid(fluidId), level);
		}
		else
		{
			fluid = null;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		if (fluid != null)
		{
			buf.writeInt(fluid.amount);
			buf.writeInt(fluid.getFluidID());
		}
		else
		{
			buf.writeInt(0);
			buf.writeBytes(("").getBytes());
		}
	}
}
