package latmod.coins.tile;

import java.util.List;

import latmod.coins.PlayerCoins;
import latmod.coins.client.gui.*;
import latmod.core.*;
import latmod.core.tile.*;
import mcp.mobius.waila.api.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.*;

public class TileTrade extends TileLM implements IPaintable, IClientActionTile, IGuiTile, IWailaTile.Stack, IWailaTile.Body
{
	public static final String BUTTON_BUY = "buy";
	public static final String BUTTON_SELL = "sell";
	
	public ItemStack tradeItem;
	public ItemStack renderItem;
	public Paint[] paint = new Paint[1];
	public int price;
	public byte rotation;
	public boolean canSell;
	public boolean canBuy;
	
	public boolean rerenderBlock()
	{ return true; }
	
	public void onUpdate()
	{
	}
	
	public void readTileData(NBTTagCompound tag)
	{
		if(!tag.hasKey("Item")) tradeItem = null; else
			tradeItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
		
		renderItem = (tradeItem == null) ? null : InvUtils.singleCopy(tradeItem);
		
		price = tag.getInteger("Price");
		rotation = tag.getByte("Rot");
		canSell = tag.getBoolean("CanSell");
		canBuy = tag.getBoolean("CanBuy");
		Paint.readFromNBT(tag, "Paint", paint);
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
		if(tradeItem != null)
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			tradeItem.writeToNBT(tag1);
			tag.setTag("Item", tag1);
		}
		
		tag.setInteger("Price", price);
		tag.setByte("Rot", rotation);
		tag.setBoolean("CanSell", canSell);
		tag.setBoolean("CanBuy", canBuy);
		Paint.writeToNBT(tag, "Paint", paint);
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is)
	{
		super.onPlacedBy(ep, is);
		rotation = (byte)MathHelperLM.get2DRotation(ep).ordinal();
		canBuy = true;
		canSell = false;
		price = 0;
	}
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, int side, float x, float y, float z)
	{
		if(worldObj.isRemote) return true;
		
		if(ep.capabilities.isCreativeMode && ep.isSneaking())
		{
			LatCoreMC.openGui(ep, this, 0);
			return true;
		}
		
		if(tradeItem != null && tradeItem.getItem() != null)
		{
			if(ep.capabilities.isCreativeMode) return true;
			
			if(!canBuy)
			{
				LatCoreMC.printChat(ep, "This item can't be bought!");
				return true;
			}
			
			if(price != 0)
			{
				if(!ep.capabilities.isCreativeMode && !PlayerCoins.take(ep.getUniqueID(), price, true))
				{
					LatCoreMC.printChat(ep, "You can't afford that!");
					return true;
				}
			}
			
			InvUtils.dropItem(ep, tradeItem);
		}
		
		return true;
	}
	
	public void onLeftClick(EntityPlayer ep)
	{
		if(worldObj.isRemote) return;
		
		if(tradeItem != null && tradeItem.getItem() != null && canSell)
		{
			if(price == 0) return;
			
			int neededSize = tradeItem.stackSize;
			
			for(int i = 0; i < ep.inventory.mainInventory.length; i++)
			{
				if(ep.inventory.mainInventory[i] != null && InvUtils.itemsEquals(ep.inventory.mainInventory[i], tradeItem, false, true))
					neededSize -= ep.inventory.mainInventory[i].stackSize;
				
				if(neededSize <= 0) break;
			}
			
			if(neededSize <= 0)
			{
				InvUtils.reduceItemInInv(ep.inventory, InvUtils.getPlayerSlots(ep), tradeItem, tradeItem.stackSize);
				PlayerCoins.set(ep.getUniqueID(), PlayerCoins.get(ep.getUniqueID()) + price);
			}
		}
	}
	
	public void handleButton(String button, int mouseButton, EntityPlayer ep)
	{
		if(button.equals(BUTTON_BUY))
		{
			canBuy = !canBuy;
			markDirty();
		}
		else if(button.equals(BUTTON_SELL))
		{
			canSell = !canSell;
			markDirty();
		}
	}
	
	public void onClientAction(EntityPlayer ep, String action, NBTTagCompound data)
	{
		if(action.equals("Item"))
		{
			if(data.hasNoTags())
				tradeItem = null;
			else tradeItem = ItemStack.loadItemStackFromNBT(data);
		}
		else if(action.equals("Price"))
		{
			price = data.getInteger("Price");
		}
		else super.onClientAction(ep, action, data);
		markDirty();
	}
	
	public boolean setPaint(PaintData p)
	{
		if(p.player.capabilities.isCreativeMode && p.canReplace(paint[0]))
		{
			paint[0] = p.paint;
			markDirty();
			return true;
		}
		
		return false;
	}
	
	public Container getContainer(EntityPlayer ep, int ID)
	{ return new ContainerTradeSettings(ep); }
	
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer ep, int ID)
	{ return new GuiTradeSettings(new ContainerTradeSettings(ep), this); }
	
	public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config)
	{ return tradeItem; }
	
	public void addWailaBody(IWailaDataAccessor data, IWailaConfigHandler config, List<String> info)
	{
		if(canBuy || canSell)
		{
			if(price == 0) info.add("For free");
			else info.add("For " + price + " coins");
			
			info.add("Buy: " + (canBuy ? "Yes" : "No"));
			info.add("Sell: " + (canSell ? "Yes" : "No"));
		}
	}

	public Paint[] getPaint()
	{ return new Paint[]{ paint[0], paint[0], paint[0], paint[0], paint[0], paint[0] }; }
}