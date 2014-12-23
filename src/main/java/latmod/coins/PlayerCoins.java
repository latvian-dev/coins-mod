package latmod.coins;
import java.util.UUID;

import latmod.core.LMPlayer;

public class PlayerCoins
{
	public static long get(UUID id)
	{
		LMPlayer p = LMPlayer.getPlayer(id);
		return (p == null) ? 0L : p.customData.getLong("Coins");
	}
	
	public static void set(UUID id, long c)
	{
		long c0 = get(id);
		
		if(c0 != c)
		{
			LMPlayer p = LMPlayer.getPlayer(id);
			if(p == null) return;
			p.customData.setLong("Coins", c);
			p.sendUpdate(CoinsEventHandler.CHANNEL);
		}
	}
	
	public static void add(UUID id, long c)
	{ set(id, get(id) + c); }
	
	public static boolean take(UUID id, long c, boolean doTake)
	{
		if(c <= 0) return false;
		
		long c0 = get(id);
		
		if(c0 >= c)
		{
			if(doTake) set(id, c0 - c);
			return true;
		}
		
		return false;
	}
}