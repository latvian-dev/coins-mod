package latmod.coins;
import latmod.coins.commands.*;
import latmod.coins.tile.*;
import latmod.core.LatCoreMC;
import latmod.core.apis.WailaHelper;
import latmod.core.mod.LMMod;
import latmod.core.mod.recipes.LMRecipes;
import net.minecraftforge.common.MinecraftForge;
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
	
	public static LMMod<CoinsConfig, LMRecipes> mod;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		mod = new LMMod<CoinsConfig, LMRecipes>(MOD_ID, new CoinsConfig(e), new LMRecipes(false));
		
		CoinsItems.init();
		mod.onPostLoaded();
		
		MinecraftForge.EVENT_BUS.register(new CoinsEventHandler());
		
		LatCoreMC.addGuiHandler(this, proxy);
		
		proxy.preInit();
		mod.config().save();
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
		
		LatCoreMC.addGamerule(e, "coinsDropRarity", "3");
		LatCoreMC.addGamerule(e, "coinsScaleAll", "1.0");
		LatCoreMC.addGamerule(e, "coinsScaleNeutral", "1.0");
		LatCoreMC.addGamerule(e, "coinsScaleHostile", "1.0");
		LatCoreMC.addGamerule(e, "coinsScaleBaby", "0.5");
		LatCoreMC.addGamerule(e, "coinsScaleBoss", "5.0");
	}
}