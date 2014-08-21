package latmod.coins;
import latmod.coins.commands.*;
import latmod.coins.game.*;
import latmod.core.apis.WailaHelper;
import latmod.core.mod.*;
import latmod.core.mod.net.*;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MOD_ID, name = "Coins Mod", version = Coins.MOD_VERSION, dependencies="required-after:LatCoreMC")
public class Coins
{
	public static final String MOD_ID = "CoinsMod";
	public static final String MOD_VERSION = "1.0.3";
	
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
		
		mod.addBlock(BlockTrade.inst = new BlockTrade("tradeBlock"));
		
		mod.addItem(ItemCoins.inst = new ItemCoins("coins"));
		
		mod.onPostLoaded();
		
		ICustomActionHandler h = new CoinsEventHandlers();
		MinecraftForge.EVENT_BUS.register(h);
		LMNetHandler.customHandlers.put(PlayerCoins.COINS_CHANNEL, h);
		
		proxy.preInit();
		config.save();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		LC.versionsToCheck.put(MOD_ID, MOD_VERSION);
		
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