package latmod.coins;

import latmod.coins.game.BlockTrade;
import latmod.core.mod.LMMod;

public class CoinsItems
{
	public static BlockTrade b_trade;
	
	public static void init(LMMod mod)
	{
		mod.addBlock(b_trade = new BlockTrade("tradeBlock"));
	}
}