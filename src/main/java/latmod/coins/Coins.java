package latmod.coins;
import latmod.coins.block.BlockTrade;
import latmod.coins.commands.*;
import latmod.core.*;
import latmod.core.recipes.LMRecipes;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MOD_ID, name = "Coins Mod", version = "@VERSION@", dependencies = "required-after:LatCoreMC")
public class Coins
{
	protected static final String MOD_ID = "CoinsMod";
	
	@Mod.Instance(Coins.MOD_ID)
	public static Coins inst;
	
	@SidedProxy(clientSide = "latmod.coins.CoinsClient", serverSide = "latmod.core.LMProxy")
	public static LMProxy proxy;
	
	public static LMMod<CoinsConfig, LMRecipes> mod;
	
	public static BlockTrade b_trade;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		mod = new LMMod<CoinsConfig, LMRecipes>(MOD_ID, new CoinsConfig(e), new LMRecipes(false));
		
		b_trade = new BlockTrade("tradeBlock").register();
		
		mod.onPostLoaded();
		
		MinecraftForge.EVENT_BUS.register(new CoinsEventHandler());
		
		proxy.preInit(e);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init(e);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		mod.loadRecipes();
		proxy.postInit(e);
	}
	
	@Mod.EventHandler()
	public void registerCommands(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CmdCoins());
		e.registerServerCommand(new CmdSetcoins());
		
		String s = "coins";
		LMGamerules.add(s, "dropRarity", "3");
		LMGamerules.add(s, "scaleAll", "1.0");
		LMGamerules.add(s, "scaleNeutral", "1.0");
		LMGamerules.add(s, "scaleHostile", "1.0");
		LMGamerules.add(s, "scaleBaby", "0.5");
		LMGamerules.add(s, "scaleBoss", "5.0");
	}
}