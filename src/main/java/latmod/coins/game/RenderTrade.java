package latmod.coins.game;

import latmod.core.InvUtils;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderTrade extends TileEntitySpecialRenderer
{
	public EntityItem entityItem = null;
	
	public RenderTrade()
	{
	}
	
	public void renderTileEntityAt(TileEntity te, double rx, double ry, double rz, float pt)
	{
		if(te == null || te.isInvalid()) return;
		TileTrade t = (TileTrade)te;
		
		if(t.tradeItem != null && t.tradeItem.getItem() != null)
		{
			if(entityItem == null) entityItem = new EntityItem(te.getWorldObj(), 0D, 0D, 0D, t.tradeItem);
			
			GL11.glPushMatrix();
			GL11.glTranslated(rx, ry + 1D, rz + 1D);
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
				GL11.glTranslatef(0.5F, 0.5F, -0.005F);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				//float iS = 1F / 6F;
				//GL11.glScalef(-1F, -1F, 1F);
				
				EntityItem entityitem = new EntityItem(t.getWorldObj(), 0.0D, 0.0D, 0.0D, InvUtils.singleCopy(t.tradeItem));
				entityitem.hoverStart = 0.0F;
				
				//GL11.glTranslatef(-0.453125F * (float)Direction.offsetX[p_82402_1_.hangingDirection], -0.18F, -0.453125F * (float)Direction.offsetZ[p_82402_1_.hangingDirection]);
				GL11.glRotatef(180F, 0F, 1F, 0F);
				
				/*float rotYaw = 0F;
				
				if(t.rotation == 2) rotYaw = 180F;
				else if(t.rotation == 3) rotYaw = 0F;
				else if(t.rotation == 4) rotYaw = 90F;
				else if(t.rotation == 5) rotYaw = -90F;
				*/
				GL11.glRotatef(rotYaw, 0F, 0F, 1F);
				
				entityItem.worldObj = t.getWorldObj();
				entityItem.setEntityItemStack(t.tradeItem);
				entityItem.rotationYaw = 0F;
				entityItem.rotationPitch = 0F;
				entityItem.age = 0;
				entityItem.hoverStart = 0F;
				
				RenderItem.renderInFrame = true;
                RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                RenderItem.renderInFrame = false;
				
				GL11.glPopMatrix();
			}
			
			GL11.glDisable(GL11.GL_LIGHTING);
			
			{
				double d = 1D / 64D;
				
				GL11.glPushMatrix();
				GL11.glTranslatef(0.5F, 0.15F, -0.005F);
				GL11.glScaled(d, d, d);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				String s1 = t.tradeItem.getDisplayName();
				
				int ss = func_147498_b().getStringWidth(s1);
				func_147498_b().drawString(s1, -ss / 2, 0, 0xFFD1D1D1);
				GL11.glPopMatrix();
			}
			
			{
				double d = 1D / 64D;
				
				GL11.glPushMatrix();
				GL11.glTranslated(0.5F, 0.75F, -0.005F);
				GL11.glScaled(d, d, d);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				String s2 = (t.price == 0) ? "Free" : ("$ " + t.price);
				
				int ss2 = func_147498_b().getStringWidth(s2);
				func_147498_b().drawString(s2, -ss2 / 2, 0, 0xFFFFAE00);
				GL11.glPopMatrix();
			}
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
			
			GL11.glPopMatrix();
		}
	}
}