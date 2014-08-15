package latmod.coins.commands;
import latmod.coins.Coins;
import latmod.core.LatCore;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdTrade extends CommandBase
{
	public int getRequiredPermissionLevel()
	{ return 0; }
	
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
	{ return true; }
	
	public String getCommandName()
	{ return "trade"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.trade"; }
	
	public void processCommand(ICommandSender ics, String[] s)
	{
		if(!(ics instanceof EntityPlayer)) return;
		
		if(s != null && s.length >= 1)
		{
			EntityPlayer ep1 = getPlayer(ics, s[0]);
			
			if(ep1 == null) LatCore.printChat(ics, "Player '" + s[0] + "' not found");
			else
			{
				LatCore.printChat(ics, "Trade request sent!");
			}
		}
	}
}