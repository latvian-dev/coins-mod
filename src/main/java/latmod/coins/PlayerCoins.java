package latmod.coins;
import java.util.UUID;

import latmod.core.LMPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerCoins
{
	public static long get(UUID id)
	{
		LMPlayer p = LMPlayer.getPlayer(id);
		if(p == null || !p.hasCustomData()) return 0L;
		return p.customData().getLong("Coins");
	}
	
	public static void set(EntityPlayer ep, long c)
	{
		long c0 = get(ep.getUniqueID());
		
		if(c0 != c)
		{
			LMPlayer p = LMPlayer.getPlayer(ep.getUniqueID());
			if(p == null) return;
			p.customData().setLong("Coins", c);
			p.sendUpdate(ep.worldObj, CoinsEventHandler.CHANNEL);
		}
	}
	
	public static void add(EntityPlayer ep, long c)
	{ set(ep, get(ep.getUniqueID()) + c); }
	
	public static boolean take(EntityPlayer ep, long c, boolean doTake)
	{
		if(ep == null || c <= 0) return false;
		
		long c0 = get(ep.getUniqueID());
		
		if(c0 >= c)
		{
			if(doTake) set(ep, c0 - c);
			return true;
		}
		
		return false;
	}
}