package latmod.coins.commands;
import latmod.coins.*;
import latmod.core.LatCoreMC;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;

public class CmdCoins extends CommandBase
{
	public String getCommandName()
	{ return "coins"; }
	
	public int getRequiredPermissionLevel()
	{ return 0; }
	
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
	{ return true; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return Coins.mod.assets + "command.usage.coins"; }
	
	public void processCommand(ICommandSender ics, String[] s)
	{
		if(!(ics instanceof EntityPlayer)) return;
		EntityPlayer ep = (EntityPlayer)ics;
		
		LatCoreMC.printChat(ics, "Coins: " + PlayerCoins.get(ep));
	}
}