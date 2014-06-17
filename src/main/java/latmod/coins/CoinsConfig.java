package latmod.coins;
import java.io.*;
import java.util.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;

import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.event.*;
import latmod.core.FastMap;
import latmod.core.LMUtils;
import latmod.core.base.*;

public class CoinsConfig extends LMConfig
{
	public General general;
	
	private File coinDropsFile;
	private FastMap<Class<? extends EntityLivingBase>, Integer> coinDrops;
	
	public CoinsConfig(FMLPreInitializationEvent e)
	{
		super(e, "/LatMod/Coins.cfg");
		
		add(general = new General());
		
		coinDropsFile = new File(e.getModConfigurationDirectory(), "/LatMod/Coins_drops.json");
		loadCoinDrops();
	}

	public class General extends Category
	{
		public boolean mobsDropCoins;
		
		public General()
		{
			super("general");
			mobsDropCoins = getBool("mobsDropCoins", true);
		}
	}
	
	private void clearMap()
	{
		if(coinDrops != null) coinDrops.clear(); else
		coinDrops = new FastMap<Class<? extends EntityLivingBase>, Integer>();
	}
	
	@SuppressWarnings("unchecked")
	public void loadCoinDrops()
	{
		clearMap();
		
		try
		{
			if(!coinDropsFile.exists())
			{
				setDefaultCoinDrops();
				saveCoinDrops();
				return;
			}
			
			FileInputStream fis = new FileInputStream(coinDropsFile);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			String json = new String(b);
			fis.close();
			
			java.lang.reflect.Type typeOfMap = new TypeToken<Map<String,Integer>>(){}.getType();
			Map<String, Integer> coinDropsJSON = LMUtils.getJson(json, typeOfMap);
			
			for(String s : coinDropsJSON.keySet())
			{
				try { coinDrops.put((Class<? extends EntityLivingBase>)Class.forName(s), coinDropsJSON.get(s)); }
				catch(Exception e) { e.printStackTrace(); }
			}
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public void saveCoinDrops()
	{
		try
		{
			HashMap<String, Integer> coinDropsJSON = new HashMap<String, Integer>();
			
			for(int i = 0; i < coinDrops.size(); i++)
				coinDropsJSON.put((coinDrops.keys.get(i) + "").split(" ")[1], coinDrops.values.get(i));
			
			String json = LMUtils.toJson(coinDropsJSON, true);
			
			FileOutputStream fos = new FileOutputStream(coinDropsFile);
			fos.write(json.getBytes());
			fos.close();
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public void setDefaultCoinDrops()
	{
		coinDrops.put(EntityLivingBase.class, 20);
	}
	
	public int getDroppedCoinsFor(EntityLivingBase e)
	{
		if(e == null || e instanceof EntityPlayer) return 0;
		return coinDrops.get(e.getClass());
	}
}