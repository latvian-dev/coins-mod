package latmod.coins.client.gui;

import latmod.coins.Coins;
import latmod.coins.tile.TileTrade;
import latmod.core.mod.gui.*;
import latmod.core.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiTradeSettings extends GuiLM
{
	public TileTrade tile;
	
	public TextBoxLM textBoxRSTimer;
	public ButtonLM buttonItem, buttonBuy, buttonSell, buttonOnlyOne, buttonRedstone;
	
	public GuiTradeSettings(ContainerLM c)
	{
		super(c, Coins.mod.getLocation("textures/gui/tradeBlock.png"));
		tile = (TileTrade)c.inv;
		
		widgets.add(textBoxRSTimer = new TextBoxLM(this, 80, 50, 79, 16)
		{
			public boolean canAddChar(char c)
			{ return c >= '0' && c <= '9' && super.canAddChar(c); }
			
			public void textChanged()
			{
				Integer t = Converter.parseInt(text);
				if(t != null)
				{
					if(t < -1)
					{
						t = -1;
						textBoxRSTimer.text = "" + t;
					}
					
					NBTTagCompound data = new NBTTagCompound();
					data.setInteger("Timer", t);
					tile.sendClientAction("RSTimer", data);
				}
			}
		});
		
		textBoxRSTimer.charLimit = 40;
		
		if(tile.redstoneTimer > 0)
			textBoxRSTimer.text = "" + tile.redstoneTimer;
		
		widgets.add(buttonItem = new ButtonLM(this, 8, 8, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				ItemStack is = container.player.inventory.getItemStack();
				
				NBTTagCompound data = new NBTTagCompound();
				if(is != null) is.writeToNBT(data);
				
				tile.sendClientAction("TradeItem", data);
			}
		});
	}
	
	public void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		textureWidth = textureHeight = 128;
		
		super.drawGuiContainerBackgroundLayer(f, x, y);
		
		textureWidth = textureHeight = 256;
		
		/*
		buttonRedstone.render(button_redstone[condenser.redstoneMode.ID]);
		buttonSecurity.render(button_security[condenser.security.level.ID]);
		buttonInvMode.render(button_inv[condenser.invMode.ID]);
		
		if(condenser.repairTools.isOn())
			buttonRepairItems.render(button_inner_pressed);
		
		buttonSettings.render(button_back);*/
	}
	
	public void drawScreen(int mx, int my, float f)
	{
		super.drawScreen(mx, my, f);
		
		FastList<String> al = new FastList<String>();
		
		/*
		if(buttonSettings.mouseOver(mx, my))
			al.add(LC.mod.translate("back"));
		
		if(buttonRedstone.mouseOver(mx, my))
			al.add(condenser.redstoneMode.getText());
		
		if(buttonSecurity.mouseOver(mx, my))
			al.add(condenser.security.level.getText());
		
		if(buttonInvMode.mouseOver(mx, my))
			al.add(condenser.invMode.getText());
		
		if(buttonRepairItems.mouseOver(mx, my))
			al.add(condenser.repairTools.getText());
			*/
		
		if(!al.isEmpty()) drawHoveringText(al, mx, my, fontRendererObj);
	}
}