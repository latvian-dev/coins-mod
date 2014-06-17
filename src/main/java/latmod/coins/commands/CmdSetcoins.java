package latmod.coins.commands;
import latmod.coins.Coins;
import latmod.coins.PlayerCoins;
import latmod.core.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdSetcoins extends CommandBase
{
	public int getRequiredPermissionLevel()
	{ return PermLevel.OP.level; }
	
	public String getCommandName()
	{ return "setcoins"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.setcoins"; }
	
	public void processCommand(ICommandSender ics, String[] s)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		
		LatCore.printChat(ics, "Coins: " + PlayerCoins.get(ep));
	}
}