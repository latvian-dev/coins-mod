package latmod.coins;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.player.*;
import cpw.mods.fml.common.event.*;
import latmod.core.base.*;

public class CoinsConfig extends LMConfig
{
	public General general;
	
	public CoinsConfig(FMLPreInitializationEvent e)
	{
		super(e, "/LatMod/Coins.cfg");
		
		add(general = new General());
	}

	public class General extends Category
	{
		public boolean mobsDropCoins;
		public boolean combineCoins;
		public float coinDropMultiplayer;
		public float bossMultiplier;
		public float babyMultiplier;
		public int coinDropRarity;
		
		public General()
		{
			super("general");
			
			mobsDropCoins = getBool("mobsDropCoins", true);
			
			combineCoins = getBool("combineCoins", true);
			
			coinDropMultiplayer = (float)getDouble("coinDropMultiplayer", 1D);
			bossMultiplier = (float)getDouble("bossMultiplier", 5D);
			babyMultiplier = (float)getDouble("babyMultiplier", 0.5D);
			
			coinDropRarity = getInt("coinDropRarity", 2, 1, 100,
					"Bigger number [1 - 100] - smaller chance of getting coins",
					"1 - Coins are always dropped");
		}
	}
	
	public int getMaxDroppedCoinsFor(EntityLivingBase e)
	{
		if(!general.mobsDropCoins || e == null || e instanceof EntityPlayer) return 0;
		
		float l = e.getMaxHealth();
		
		l *= general.coinDropMultiplayer;
		
		if(e instanceof IBossDisplayData)
			l *= general.bossMultiplier;
		
		if(e.getAge() < 0)
			l *= general.babyMultiplier;
		
		return (int)l;
	}
}