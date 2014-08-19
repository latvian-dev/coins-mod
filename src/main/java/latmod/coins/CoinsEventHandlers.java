package latmod.coins;
import latmod.coins.game.ItemCoins;
import latmod.core.*;
import latmod.core.client.LMRenderer;
import latmod.core.mod.net.ICustomActionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.relauncher.*;

public class CoinsEventHandlers implements ICustomActionHandler
{
	public int coinsAlpha = 0;
	public long clientCoins = 0L;
	public long prevCoins = 0L;
	
	@SubscribeEvent
	public void onEntityLivingDrops(LivingDeathEvent e)
	{
		if(Coins.config.general.mobsDropCoins && !e.entity.worldObj.isRemote)
		{
			if(e.source != null && e.source instanceof EntityDamageSource && ((EntityDamageSource)e.source).getEntity() instanceof EntityPlayer)
			{
				if(e.entity.worldObj.rand.nextInt(Coins.config.general.coinDropRarity) != 0) return;
				
				int max = Coins.config.getMaxDroppedCoinsFor(e.entityLiving);
				
				if(max <= 0) return;
				
				int l = e.entity.worldObj.rand.nextInt(max) + 1;
				
				if(l <= 0) return;
				
				LMUtils.dropItem(e.entity, ItemCoins.inst.create(l));
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent e)
	{
		if(Coins.config.general.combineCoins && !e.entity.worldObj.isRemote)
		{
			ItemStack is = e.item.getEntityItem();
			
			if(is != null && InvUtils.itemsEquals(is, ItemCoins.COINS_1, false, false))
			{
				int i = InvUtils.getFirstIndexWithItem(e.entityPlayer.inventory, ItemCoins.COINS_1, null, false, false);
				
				if(i != -1)
				{
					ItemStack is1 = e.entityPlayer.inventory.getStackInSlot(i);
					
					if(is1 != null)
					{
						long c = ItemCoins.inst.getCoins(is1);
						long c1 = ItemCoins.inst.getCoins(is);
						
						e.entityPlayer.inventory.setInventorySlotContents(i, ItemCoins.inst.create(c + c1));
						e.entity.worldObj.playSoundAtEntity(e.entity, "random.orb", 0.1F, 0.5F * ((e.entity.worldObj.rand.nextFloat() - e.entity.worldObj.rand.nextFloat()) * 0.7F + 1.8F));
						
						e.item.setDead();
						e.setResult(Event.Result.DENY);
						e.setCanceled(true);
					}
				}
			}
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
			
			int col1 = LMRenderer.getColor((clientCoins > prevCoins) ? EnumDyeColor.LIME.color : EnumDyeColor.RED.color, coinsAlpha);
			
			mc.fontRenderer.drawString(EnumChatFormatting.BOLD + "Coins: " + clientCoins, 4, e.resolution.getScaledHeight() - 12, col1);
		}
	}

	public void onAction(EntityPlayer ep, String channel, String action, NBTTagCompound extraData, Side s)
	{
		if(action.equals(PlayerCoins.ACTION_COINS_CHANGED))
		{
			coinsAlpha = 255;
			prevCoins = clientCoins;
			clientCoins = extraData.getLong("Coins");
		}
	}
}