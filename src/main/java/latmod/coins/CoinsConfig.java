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
	
	public double getGameRuleD(String s)
	{ return LMGamerules.get("coins", s, 0D).doubleValue(); }
	
	public double getMaxDroppedCoinsFor(EntityLivingBase e)
	{
		if(e == null || e instanceof EntityPlayer) return 0D;
		
		double l = e.getMaxHealth();
		
		l *= getGameRuleD("scaleAll");
		
		if(e instanceof IMob)
			l *= getGameRuleD("scaleHostile");
		else
			l *= getGameRuleD("scaleNeutral");
		
		if(e instanceof IBossDisplayData)
			l *= getGameRuleD("scaleBoss");
		
		if(e.getAge() < 0)
			l *= getGameRuleD("scaleBaby");
		
		return (int)l;
	}
}