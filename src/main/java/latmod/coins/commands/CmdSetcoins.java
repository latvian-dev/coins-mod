package latmod.coins.commands;
import latmod.coins.*;
import latmod.core.LatCoreMC;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdSetcoins extends CommandBase
{
	public String getCommandName()
	{ return "setcoins"; }
	
	public int getRequiredPermissionLevel()
	{ return 2; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.setcoins"; }
	
	public void processCommand(ICommandSender ics, String[] args)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		
		if(args != null && args.length >= 1)
		{
			if(args.length >= 2) ep = getPlayer(ics, args[1]);
			
			boolean rel = args[0].startsWith("~");
			long i0 = PlayerCoins.get(ep);
			
			int i = parseInt(ics, rel ? (args[0].length() == 1 ? "0" : args[0].substring(1)) : args[0]);
			PlayerCoins.set(ep, rel ? (i0 + i) : i);
			
			if(rel) LatCoreMC.printChat(ics, "Added " + i + " coins. " + ep.getCommandSenderName() + " now has " + (i + i0) + " coins");
			else LatCoreMC.printChat(ics, "Coins set to: " + i);
		}
	}
}