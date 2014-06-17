package latmod.coins.commands;
import latmod.coins.Coins;
import latmod.coins.PlayerCoins;
import latmod.core.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdCoins extends CommandBase
{
	public int getRequiredPermissionLevel()
	{ return PermLevel.PLAYER.level; }
	
	public String getCommandName()
	{ return "coins"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.coins"; }
	
	public void processCommand(ICommandSender ics, String[] s)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		
		LatCore.printChat(ics, "Coins: " + PlayerCoins.get(ep));
	}
}