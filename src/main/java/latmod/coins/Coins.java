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
	
	@SidedProxy(clientSide = "latmod.coins.CoinsClient", serverSide = "latmod.core.LMProxy")
	public static LMProxy proxy;
	
	public static LMMod mod;
	
	public static BlockTrade b_trade;
	
	public static final String RULE_GROUP = "coins";
	public static LMGamerules.RuleID RULE_DROP_RARITY = new LMGamerules.RuleID(RULE_GROUP, "dropRarity");
	public static LMGamerules.RuleID RULE_SCALE_ALL = new LMGamerules.RuleID(RULE_GROUP, "scaleAll");
	public static LMGamerules.RuleID RULE_SCALE_NEUTRAL = new LMGamerules.RuleID(RULE_GROUP, "scaleNeutral");
	public static LMGamerules.RuleID RULE_SCALE_HOSTILE = new LMGamerules.RuleID(RULE_GROUP, "scaleHostile");
	public static LMGamerules.RuleID RULE_SCALE_BABY = new LMGamerules.RuleID(RULE_GROUP, "scaleBaby");
	public static LMGamerules.RuleID RULE_SCALE_BOSS = new LMGamerules.RuleID(RULE_GROUP, "scaleBoss");
	public static LMGamerules.RuleID RULE_INIT_COINS = new LMGamerules.RuleID(RULE_GROUP, "initCoins");
	
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
		mod.loadRecipes();
		proxy.postInit(e);
		
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