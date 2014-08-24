package latmod.coins;

import net.minecraft.item.ItemStack;
import latmod.coins.game.*;
import latmod.core.mod.LMMod;

public class CoinsItems
{
	public static BlockTrade b_trade;
	public static ItemCoins i_coins;
	
	public static ItemStack COINS_1;
	public static ItemStack COINS_10;
	public static ItemStack COINS_100;
	public static ItemStack COINS_1000;
	public static ItemStack COINS_10000;
	
	public static void init(LMMod mod)
	{
		mod.addBlock(b_trade = new BlockTrade("tradeBlock"));
		
		mod.addItem(i_coins = new ItemCoins("coins"));
	}
}