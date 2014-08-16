package latmod.coins;
import latmod.coins.commands.*;
import latmod.coins.game.*;
import latmod.core.mod.LMMod;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MODID, name = Coins.MODNAME, version = Coins.MODVERSION, dependencies="required-after:LatCoreMC")
public class Coins
{
	public static final String MODID = "CoinsMod";
	public static final String MODNAME = "Coins Mod";
	public static final String MODVERSION = "1.0.0";
	
	@Mod.Instance(Coins.MODID)
	public static Coins inst;
	
	@SidedProxy(clientSide = "latmod.coins.CoinsClient", serverSide = "latmod.coins.CoinsCommon")
	public static CoinsCommon proxy; // CoinsClient
	
	public static LMMod mod;
	public static CoinsConfig config;
	public static Logger logger = LogManager.getLogger("CoinsMod");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger.info("Loading Coins mod...");
		
		mod = new LMMod(MODID);
		config = new CoinsConfig(e);
		
		mod.addBlock(BlockTrade.inst = new BlockTrade("tradeBlock"));
		
		mod.addItem(ItemCoins.inst = new ItemCoins("coins"));
		
		mod.onPostLoaded();
		
		MinecraftForge.EVENT_BUS.register(new CoinsEventHandlers());
		
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