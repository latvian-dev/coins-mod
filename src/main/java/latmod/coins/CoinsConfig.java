package latmod.coins;
import latmod.core.LMConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
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
	
	private double getGameRuleD(World w, String s)
	{ return Double.parseDouble(w.getGameRules().getGameRuleStringValue(s)); }
	
	public double getMaxDroppedCoinsFor(EntityLivingBase e)
	{
		if(e == null || e instanceof EntityPlayer) return 0D;
		
		double l = e.getMaxHealth();
		
		l *= getGameRuleD(e.worldObj, "coinsScaleAll");
		
		if(e instanceof IMob)
			l *= getGameRuleD(e.worldObj, "coinsScaleHostile");
		else
			l *= getGameRuleD(e.worldObj, "coinsScaleNeutral");
		
		if(e instanceof IBossDisplayData)
			l *= getGameRuleD(e.worldObj, "coinsScaleBoss");
		
		if(e.getAge() < 0)
			l *= getGameRuleD(e.worldObj, "coinsScaleBaby");
		
		return (int)l;
	}
}