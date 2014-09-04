package latmod.coins;

import latmod.coins.block.BlockTrade;

public class CoinsItems
{
	public static BlockTrade b_trade;
	
	public static void init()
	{
		Coins.mod.addBlock(b_trade = new BlockTrade("tradeBlock"));
	}
}