package latmod.coins.client.gui;

import latmod.coins.Coins;
import latmod.coins.tile.TileTrade;
import latmod.core.*;
import latmod.core.gui.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiTradeSettings extends GuiLM
{
	public TileTrade tile;
	
	public TextBoxLM textBoxCoins;
	public ButtonLM buttonItem, buttonBuy, buttonSell;
	
	public GuiTradeSettings(ContainerTradeSettings c, TileTrade t)
	{
		super(c, Coins.mod.getLocation("textures/gui/tradeBlock.png"));
		tile = t;
		xSize = 176;
		ySize = 134;
		
		widgets.add(textBoxCoins = new TextBoxLM(this, 80, 29, 79, 16)
		{
			public boolean canAddChar(char c)
			{ return c >= '0' && c <= '9' && super.canAddChar(c); }
			
			public void textChanged()
			{
				if(text.length() == 0) text = "0";
				
				Integer t = MathHelperLM.toInt(text);
				if(t != null)
				{
					if(t < 0) t = 0;
					if(t >= 20000000) t = 20000000;
					
					text = "" + t;
					
					NBTTagCompound data = new NBTTagCompound();
					data.setInteger("Price", t);
					tile.sendClientAction("Price", data);
				}
			}
		});
		
		textBoxCoins.charLimit = 12;
		textBoxCoins.text = tile.price + "";
		
		widgets.add(buttonItem = new ButtonLM(this, 7, 7, 18, 18)
		{
			public void onButtonPressed(int b)
			{
				ItemStack is = container.player.inventory.getItemStack();
				
				NBTTagCompound data = new NBTTagCompound();
				if(is != null)
				{
					ItemStack is1 = is.copy();
					if(b == 1) is1.stackSize = 1;
					is1.writeToNBT(data);
				}
				
				tile.sendClientAction("Item", data);
			}
		});
		
		widgets.add(buttonBuy = new ButtonLM(this, 80, 8, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				playClickSound();
				tile.clientPressButton(TileTrade.BUTTON_BUY, b);
			}
		});
		
		widgets.add(buttonSell = new ButtonLM(this, 143, 8, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				playClickSound();
				tile.clientPressButton(TileTrade.BUTTON_SELL, b);
			}
		});
	}
	
	public void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(f, mx, my);
		
		if(tile.canBuy) buttonBuy.render(button_pressed);
		//if(buttonBuy.mouseOver(mx, my)) buttonBuy.render(button_over);
		if(tile.canSell) buttonSell.render(button_pressed);
		//if(buttonSell.mouseOver(mx, my)) buttonSell.render(button_over);
	}
	
	public void drawScreen(int mx, int my, float f)
	{
		super.drawScreen(mx, my, f);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.enableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		
		if(tile.renderItem != null)
		{
			int x = guiLeft + 8;
			int y = guiTop + 8;
			
			GL11.glTranslatef(0F, 0F, 32F);
			this.zLevel = 200F;
			itemRender.zLevel = 200F;
			FontRenderer font = null;
			if (tile.renderItem != null) font = tile.renderItem.getItem().getFontRenderer(tile.renderItem);
			if (font == null) font = fontRendererObj;
			itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), tile.renderItem, x, y);
			itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), tile.renderItem, x, y, null);
			this.zLevel = 0F;
			itemRender.zLevel = 0F;
		}
		
		RenderHelper.disableStandardItemLighting();
		
		GL11.glDisable(GL11.GL_LIGHTING);
		textBoxCoins.render(83, 33, 0xCECECE);
		
		FastList<String> al = new FastList<String>();
		
		if(buttonItem.mouseOver(mx, my))
		{
			if(tile.renderItem != null)
				al.add(tile.tradeItem.stackSize + " x " + tile.tradeItem.getDisplayName());
		}
		
		if(buttonBuy.mouseOver(mx, my))
			al.add(tile.canBuy ? "Enabled" : "Disabled");
		
		if(buttonSell.mouseOver(mx, my))
			al.add(tile.canSell ? "Enabled" : "Disabled");
		
		if(!al.isEmpty()) drawHoveringText(al, mx, my, fontRendererObj);
	}
}