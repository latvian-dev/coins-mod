package latmod.coins;
import latmod.core.LMGamerules;
import latmod.core.event.LMPlayerEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CoinsEventHandler
{
	public static final CoinsEventHandler instance = new CoinsEventHandler();
	
	public static final String CHANNEL = "Coins";
	
	public double getMaxDroppedCoinsFor(EntityLivingBase e)
	{
		if(e == null || e instanceof EntityPlayer) return 0D;
		
		double l = e.getMaxHealth();
		
		l *= LMGamerules.get(Coins.RULE_SCALE_ALL).getNum().doubleValue();
		
		if(e instanceof IMob)
			l *= LMGamerules.get(Coins.RULE_SCALE_HOSTILE).getNum().doubleValue();
		else
			l *= LMGamerules.get(Coins.RULE_SCALE_NEUTRAL).getNum().doubleValue();
		
		if(e instanceof IBossDisplayData)
			l *= LMGamerules.get(Coins.RULE_SCALE_BOSS).getNum().doubleValue();
		
		if(e.getAge() < 0)
			l *= LMGamerules.get(Coins.RULE_SCALE_BABY).getNum().doubleValue();
		
		return (int)l;
	}
	
	@SubscribeEvent
	public void onEntityLivingDrops(LivingDeathEvent e)
	{
		if(!e.entity.worldObj.isRemote && e.source != null && e.source instanceof EntityDamageSource && ((EntityDamageSource)e.source).getEntity() instanceof EntityPlayer)
		{
			int rarity = LMGamerules.get(Coins.RULE_DROP_RARITY).getNum().intValue();
			
			if(e.entity.worldObj.rand.nextInt(rarity) != 0) return;
			
			double max = getMaxDroppedCoinsFor(e.entityLiving);
			
			if(max < 1D) return;
			
			int l = e.entity.worldObj.rand.nextInt((int)max) + 1;
			
			if(l > 0) PlayerCoins.add(((EntityDamageSource)e.source).getEntity().getUniqueID(), l);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(LMPlayerEvent.LoggedIn e)
	{
		if(e.firstTime && e.side.isServer())
		{
			long c = LMGamerules.get(Coins.RULE_INIT_COINS).getNum().longValue();
			PlayerCoins.set(e.player.uuid, c);
		}
	}
}