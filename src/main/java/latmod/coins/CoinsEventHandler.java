package latmod.coins;
import latmod.core.*;
import latmod.core.client.LMRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;

public class CoinsEventHandler
{
	public static final String CHANNEL = "Coins";
	
	public int coinsAlpha = 0;
	public long clientCoins = 0L;
	public long prevCoins = 0L;
	
	@SubscribeEvent
	public void onEntityLivingDrops(LivingDeathEvent e)
	{
		if(!e.entity.worldObj.isRemote && e.source != null && e.source instanceof EntityDamageSource && ((EntityDamageSource)e.source).getEntity() instanceof EntityPlayer)
		{
			int rarity = Integer.parseInt(e.entity.worldObj.getGameRules().getGameRuleStringValue("coinsDropRarity"));
			
			if(e.entity.worldObj.rand.nextInt(rarity) != 0) return;
			
			double max = Coins.mod.config().getMaxDroppedCoinsFor(e.entityLiving);
			
			if(max < 1D) return;
			
			int l = e.entity.worldObj.rand.nextInt((int)max) + 1;
			
			if(l > 0) PlayerCoins.add((EntityPlayer)(((EntityDamageSource)e.source).getEntity()), l);
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
	public void dataChanged(LMPlayer.DataChangedEvent e)
	{
		if(e.isChannel(CHANNEL) && e.side.isClient())
		{
			coinsAlpha = 255;
			prevCoins = clientCoins;
			clientCoins = PlayerCoins.get(e.player.uuid);
		}
	}
}