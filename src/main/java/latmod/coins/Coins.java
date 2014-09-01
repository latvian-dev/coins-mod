package latmod.coins;
import latmod.coins.commands.*;
import latmod.coins.game.*;
import latmod.core.apis.WailaHelper;
import latmod.core.mod.LMMod;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MOD_ID, name = "Coins Mod", version = Coins.MOD_VERSION, dependencies = "required-after:LatCoreMC")
public class Coins
{
	public static final String MOD_ID = "CoinsMod";
	public static final String MOD_VERSION = "@VERSION@";
	
	@Mod.Instance(Coins.MOD_ID)
	public static Coins inst;
	
	@SidedProxy(clientSide = "latmod.coins.CoinsClient", serverSide = "latmod.coins.CoinsCommon")
	public static CoinsCommon proxy;
	
	public static LMMod mod;
	public static CoinsConfig config;
	public static Logger logger = LogManager.getLogger("CoinsMod");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger.info("Loading Coins mod...");
		
		mod = new LMMod(MOD_ID);
		config = new CoinsConfig(e);
		
		CoinsItems.init(mod);
		mod.onPostLoaded();
		
		MinecraftForge.EVENT_BUS.register(new CoinsEventHandler());
		
		proxy.preInit();
		config.save();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		try { WailaHelper.registerDataProvider(TileTrade.class, new WailaTrade()); }
		catch(Exception ex) {}
		
		mod.loadRecipes();
		proxy.postInit();
	}
	
	@Mod.EventHandler()
	public void registerCommands(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CmdCoins());
		e.registerServerCommand(new CmdSetcoins());
		e.registerServerCommand(new CmdSetTrade());
	}
}