package latmod.coins;
import latmod.core.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CoinsConfig extends LMConfig
{
	public General general;
	
	public CoinsConfig(FMLPreInitializationEvent e)
	{
		super(e, "/LatMod/CoinsMod.cfg");
		general = new General();
		save();
	}

	public class General extends Category
	{
		public General()
		{
			super("general");
		}
	}
	
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
}