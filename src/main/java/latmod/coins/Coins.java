package latmod.coins;
import latmod.coins.commands.*;
import latmod.coins.tile.*;
import latmod.core.LatCoreMC;
import latmod.core.apis.WailaHelper;
import latmod.core.mod.LMMod;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MOD_ID, name = "Coins Mod", version = "@VERSION@", dependencies = "required-after:LatCoreMC")
public class Coins
{
	protected static final String MOD_ID = "CoinsMod";
	
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
		
		LatCoreMC.addGuiHandler(this, proxy);
		
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
		
		addGamerule(e, "coinsDropRarity", "3");
		addGamerule(e, "coinsScaleAll", "1.0");
		addGamerule(e, "coinsScaleNeutral", "1.0");
		addGamerule(e, "coinsScaleHostile", "1.0");
		addGamerule(e, "coinsScaleBaby", "0.5");
		addGamerule(e, "coinsScaleBoss", "5.0");
	}
	
	private void addGamerule(FMLServerStartingEvent e, String s, String s1)
	{
		if(!e.getServer().worldServers[0].getGameRules().hasRule(s))
			e.getServer().worldServers[0].getGameRules().addGameRule(s, s1);
	}
}