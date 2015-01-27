package latmod.coins.client;
import latmod.coins.*;
import latmod.core.EnumDyeColor;
import latmod.core.client.LMRenderHelper;
import latmod.core.event.LMPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CoinsClientEventHandler
{
	public static final CoinsClientEventHandler instance = new CoinsClientEventHandler();
	
	public int coinsAlpha = 0;
	public long clientCoins = 0L;
	public long prevCoins = 0L;
	
	@SubscribeEvent
	public void renderCoinsIngame(RenderGameOverlayEvent.Pre e)
	{
		if(coinsAlpha > 0 && e.type == RenderGameOverlayEvent.ElementType.CHAT)
		{
			GL11.glColor4f(1F, 1F, 1F, 1F);
			
			Minecraft mc = Minecraft.getMinecraft();
			
			coinsAlpha--;
			
			int col1 = LMRenderHelper.getColor((clientCoins < prevCoins) ? EnumDyeColor.RED.color.getRGB() : EnumDyeColor.LIME.color.getRGB(), coinsAlpha);
			mc.fontRenderer.drawString("Coins: " + clientCoins, 4, e.resolution.getScaledHeight() - 12, col1);
		}
	}
	
	@SubscribeEvent
	public void dataChanged(LMPlayerEvent.DataChanged e)
	{
		if(e.isChannel(CoinsEventHandler.CHANNEL) && e.side.isClient())
		{
			coinsAlpha = 255;
			prevCoins = clientCoins;
			clientCoins = PlayerCoins.get(e.player.uuid);
		}
	}
}