package latmod.coins;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;

public class PlayerCoins
{
	public static final String COINS_TAG = "LM_Coins";
	
	public static long get(EntityPlayer ep)
	{
		if(ep == null) return 0;
		NBTTagCompound tag = ep.getEntityData();
		return tag.getLong(COINS_TAG);
	}
	
	public static void set(EntityPlayer ep, long c)
	{
		if(ep == null) return;
		NBTTagCompound tag = ep.getEntityData();
		tag.setLong(COINS_TAG, c);
	}
	
	public static boolean take(EntityPlayer ep, long c, boolean doTake)
	{
		if(ep == null || c <= 0) return false;
		
		long c0 = get(ep);
		
		if(c0 >= c)
		{
			if(doTake)
				set(ep, c0 - c);
			
			return true;
		}
		
		return false;
	}
}