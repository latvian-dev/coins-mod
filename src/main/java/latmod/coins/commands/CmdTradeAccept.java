package latmod.coins.commands;
import latmod.coins.*;
import latmod.core.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdTradeAccept extends CommandBase
{
	public int getRequiredPermissionLevel()
	{ return PermLevel.PLAYER.level; }
	
	public String getCommandName()
	{ return "trade_accept"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.trade_accept"; }
	
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