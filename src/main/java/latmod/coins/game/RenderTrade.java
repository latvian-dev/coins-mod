package latmod.coins.game;

import latmod.core.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderTrade extends TileEntitySpecialRenderer
{
	public EntityItem entityItem = null;
	public RenderBlocks renderBlocks = new RenderBlocks();
	
	public RenderTrade()
	{
	}
	
	public void renderTileEntityAt(TileEntity te, double tx, double ty, double tz, float pt)
	{
		if(te == null || te.isInvalid()) return;
		TileTrade t = (TileTrade)te;
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();
		GL11.glTranslated(tx + 0.5D, ty + 0.5D, tz + 0.5D);
		ItemStack tex = t.getPaint();
		if(tex == null) tex = new ItemStack(t.blockType);
		bindTexture(TextureMap.locationBlocksTexture);
		renderBlocks.blockAccess = t.getWorldObj();
		renderBlocks.clearOverrideBlockTexture();
		renderBlocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
		renderBlocks.renderBlockSandFalling(Block.getBlockFromItem(tex.getItem()), t.getWorldObj(), t.xCoord, t.yCoord, t.zCoord, tex.getItemDamage());
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		
		if(t.tradeItem != null && t.tradeItem.getItem() != null)
		{
			if(entityItem == null) entityItem = new EntityItem(te.getWorldObj(), 0D, 0D, 0D, t.tradeItem);
			
			GL11.glPushMatrix();
			GL11.glTranslated(tx, ty + 1D, tz + 1D);
			GL11.glScalef(1F, -1F, -1F);
			
			float rotYaw = 0F;

			if(t.rotation == 2) rotYaw = 180F;
			else if(t.rotation == 3) rotYaw = 0F;
			else if(t.rotation == 4) rotYaw = 90F;
			else if(t.rotation == 5) rotYaw = -90F;
			
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(rotYaw, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			{
				GL11.glPushMatrix();
				GL11.glTranslatef(0.5F, 0.65F, -0.005F);
				
				if(!(t.tradeItem.getItem() instanceof ItemBlock))
					GL11.glTranslatef(0F, 0.05F, 0F);
				
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				GL11.glRotatef(180F, 0F, 1F, 0F);
				
				float iS = 1.2F;
				GL11.glScalef(-iS, -iS, iS);
				
				entityItem.worldObj = t.getWorldObj();
				entityItem.setEntityItemStack(InvUtils.singleCopy(t.tradeItem));
				entityItem.hoverStart = 0F;
				
				RenderItem.renderInFrame = true;
                RenderManager.instance.renderEntityWithPosYaw(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                RenderItem.renderInFrame = false;
				
				GL11.glPopMatrix();
			}
			
			if(t.price >= 0 && (t.canBuy || t.canSell))
			{
				GL11.glDisable(GL11.GL_LIGHTING);
				
				{
					GL11.glPushMatrix();
					GL11.glTranslatef(0.5F, 0.075F, -0.005F);
					GL11.glColor4f(1F, 1F, 1F, 1F);
					
					String s1 = t.tradeItem.getDisplayName();
					
					if(t.tradeItem.stackSize > 1)
						s1 = t.tradeItem.stackSize + " x " + s1;
					
					int ss = func_147498_b().getStringWidth(s1);
					
					double d = 1D / Math.max((ss + 10), 64);
					
					GL11.glScaled(d, d, d);
					
					func_147498_b().drawString(s1, -ss / 2, 0, 0xFFD1D1D1);
					GL11.glPopMatrix();
				}
				
				{
					GL11.glPushMatrix();
					GL11.glTranslated(0.5F, 0.80F, -0.005F);
					GL11.glColor4f(1F, 1F, 1F, 1F);
					
					String s2 = (t.price == 0) ? "Free" : ("$ " + t.price);
					
					int ss2 = func_147498_b().getStringWidth(s2);
					double d = 1D / Math.max((ss2 + 10), 64);
					
					GL11.glScaled(d, d, d);
					
					func_147498_b().drawString(s2, -ss2 / 2, 0, 0xFFFFAE00);
					GL11.glPopMatrix();
				}
				
				GL11.glEnable(GL11.GL_LIGHTING);
			}
			
			GL11.glPopMatrix();
			
			GL11.glPopMatrix();
		}
	}
}