package latmod.coins;
import latmod.core.LatCoreMC;
import latmod.core.mod.net.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerCoins
{
	public static final String COINS_TAG = "LM_Coins";
	public static final String COINS_CHANNEL = Coins.MOD_ID;
	public static final String ACTION_COINS_CHANGED = "changed";
	
	public static long get(EntityPlayer ep)
	{
		if(ep == null) return 0;
		NBTTagCompound tag = ep.getEntityData();
		return tag.getLong(COINS_TAG);
	}
	
	public static void set(EntityPlayer ep, long c)
	{
		if(ep == null) return;
		
		long c0 = get(ep);
		
		if(c0 != c)
		{
			NBTTagCompound tag = ep.getEntityData();
			tag.setLong(COINS_TAG, c);
			
			if(LatCoreMC.canUpdate() && ep instanceof EntityPlayerMP)
			{
				NBTTagCompound tag1 = new NBTTagCompound();
				tag1.setLong("Coins", c);
				LMNetHandler.INSTANCE.sendTo(new MessageCustomServerAction(COINS_CHANNEL, ACTION_COINS_CHANGED, tag1), (EntityPlayerMP)ep);
			}
		}
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