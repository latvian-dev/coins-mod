package latmod.coins;
import latmod.coins.block.BlockTrade;
import latmod.coins.commands.*;
import latmod.core.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Coins.MOD_ID, name = "Coins Mod", version = "@VERSION@", dependencies = "required-after:LatCoreMC")
public class Coins
{
	protected static final String MOD_ID = "CoinsMod";
	
	@Mod.Instance(Coins.MOD_ID)
	public static Coins inst;
	
	@SidedProxy(clientSide = "latmod.coins.client.CoinsClient", serverSide = "latmod.core.LMProxy")
	public static LMProxy proxy;
	
	public static LMMod mod;
	
	public static BlockTrade b_trade;
	
	private static LMGamerules.RuleID create(String s)
	{ return new LMGamerules.RuleID("coins", s); }
	
	public static LMGamerules.RuleID RULE_DROP_RARITY = create("dropRarity");
	public static LMGamerules.RuleID RULE_SCALE_ALL = create("scaleAll");
	public static LMGamerules.RuleID RULE_SCALE_NEUTRAL = create("scaleNeutral");
	public static LMGamerules.RuleID RULE_SCALE_HOSTILE = create("scaleHostile");
	public static LMGamerules.RuleID RULE_SCALE_BABY = create("scaleBaby");
	public static LMGamerules.RuleID RULE_SCALE_BOSS = create("scaleBoss");
	public static LMGamerules.RuleID RULE_INIT_COINS = create("initCoins");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		mod = new LMMod(MOD_ID, null, null);
		
		b_trade = new BlockTrade("tradeBlock").register();
		
		mod.onPostLoaded();
		
		LatCoreMC.addEventHandler(CoinsEventHandler.instance, true, false, true);
		
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
		proxy.postInit(e);
		
		mod.loadRecipes();
		
		LMGamerules.register(RULE_DROP_RARITY, 3);
		LMGamerules.register(RULE_SCALE_ALL, 1D);
		LMGamerules.register(RULE_SCALE_NEUTRAL, 1D);
		LMGamerules.register(RULE_SCALE_HOSTILE, 1D);
		LMGamerules.register(RULE_SCALE_BABY, 0.5D);
		LMGamerules.register(RULE_SCALE_BOSS, 5D);
		LMGamerules.register(RULE_INIT_COINS, 0);
	}
	
	@Mod.EventHandler
	public void registerCommands(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CmdCoins());
		e.registerServerCommand(new CmdSetcoins());
	}
}