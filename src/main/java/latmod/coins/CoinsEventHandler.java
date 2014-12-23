package latmod.coins;
import latmod.core.*;
import latmod.core.client.LMRenderHelper;
import latmod.core.event.LMPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;

public class CoinsEventHandler
{
	public static final CoinsEventHandler instance = new CoinsEventHandler();
	
	public static final String CHANNEL = "Coins";
	
	public int coinsAlpha = 0;
	public long clientCoins = 0L;
	public long prevCoins = 0L;
	
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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderCoinsIngame(RenderGameOverlayEvent.Pre e)
	{
		if(coinsAlpha > 0 && e.type == RenderGameOverlayEvent.ElementType.CHAT)
		{
			GL11.glColor4f(1F, 1F, 1F, 1F);
			
			Minecraft mc = Minecraft.getMinecraft();
			
			coinsAlpha--;
			
			int col1 = LMRenderHelper.getColor((clientCoins < prevCoins) ? EnumDyeColor.RED.color : EnumDyeColor.LIME.color, coinsAlpha);
			
			mc.fontRenderer.drawString("Coins: " + clientCoins, 4, e.resolution.getScaledHeight() - 12, col1);
		}
	}
	
	@SubscribeEvent
	public void dataChanged(LMPlayerEvent.DataChanged e)
	{
		if(e.isChannel(CHANNEL) && e.side.isClient())
		{
			coinsAlpha = 255;
			prevCoins = clientCoins;
			clientCoins = PlayerCoins.get(e.player.uuid);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(LMPlayerEvent.LoggedIn e)
	{
		if(e.firstTime && LatCoreMC.isServer())
		{
			long c = LMGamerules.get(Coins.RULE_INIT_COINS).getNum().longValue();
			PlayerCoins.set(e.player.uuid, c);
		}
	}
}