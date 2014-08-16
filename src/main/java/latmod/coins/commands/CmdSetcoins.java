package latmod.coins.commands;
import latmod.coins.*;
import latmod.core.LatCore;
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
	
	public void processCommand(ICommandSender ics, String[] s)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		
		if(s != null && s.length == 1)
		{
			boolean rel = s[0].startsWith("~");
			long i0 = PlayerCoins.get(ep);
			
			int i = parseInt(ics, rel ? (s[0].length() == 1 ? "0" : s[0].substring(1)) : s[0]);
			PlayerCoins.set(ep, rel ? (i0 + i) : i);
			
			if(rel) LatCore.printChat(ics, "Added " + i + " coins. You now have " + (i + i0) + " coins");
			else LatCore.printChat(ics, "Coins set to: " + i);
		}
	}
}