package latmod.coins.commands;
import latmod.coins.Coins;
import latmod.coins.game.TileTrade;
import latmod.core.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

public class CmdSetTrade extends CommandBase
{
	public String getCommandName()
	{ return "settrade"; }
	
	public int getRequiredPermissionLevel()
	{ return 0; }
	
	public boolean canCommandSenderUseCommand(ICommandSender ics)
	{ return true; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.settrade"; }
	
	public void processCommand(ICommandSender ics, String[] args)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		if(ep.worldObj.isRemote) return;
		
		if(args == null || args.length == 0)
		{
			LatCoreMC.printChat(ics, "/settrade <price> <canSell> [canBuy]");
			return;
		}
		
		if(ep.capabilities.isCreativeMode)
		{
			if(args.length >= 2)
			{
				int price = parseInt(ics, args[0]);
				boolean canSell = parseBoolean(ics, args[1]);
				boolean canBuy = (args.length >= 3) ? parseBoolean(ics, args[2]) : true;
				ItemStack item = ep.getHeldItem();
				
				if(item != null)
				{
					MovingObjectPosition mop = LatCoreMC.rayTrace(ep, 4D);
					
					if(mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					{
						TileEntity te = ep.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
						
						if(te != null && !te.isInvalid() && te instanceof TileTrade)
						{
							TileTrade t = (TileTrade)te;
							
							t.tradeItem = item.copy();
							t.price = price;
							t.canSell = canSell;
							t.canBuy = canBuy;
							t.markDirty();
							
							LatCoreMC.printChat(ics, "Trade item set");
						}
						else LatCoreMC.printChat(ics, "Tile is not Trading Block!");
					}
					else LatCoreMC.printChat(ics, "Tile can't be null!");
				}
				else LatCoreMC.printChat(ics, "Item can't be null!");
			}
			else processCommand(ics, null);
		}
		else LatCoreMC.printChat(ics, "You must be in Creative mode!");
	}
}