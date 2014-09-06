package latmod.coins.tile;

import latmod.coins.*;
import latmod.core.*;
import latmod.core.mod.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileTrade extends TileLM implements IPaintable, IClientActionTile
{
	public static final String BUTTON_BUY = "buy";
	public static final String BUTTON_SELL = "sell";
	
	public ItemStack tradeItem;
	public ItemStack renderItem;
	private ItemStack paintItem;
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
		
		if(!tag.hasKey("Paint")) paintItem = null;
			paintItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Paint"));
		
		price = tag.getInteger("Price");
		rotation = tag.getByte("Rot");
		canSell = tag.getBoolean("CanSell");
		canBuy = tag.getBoolean("CanBuy");
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
		if(tradeItem != null)
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			tradeItem.writeToNBT(tag1);
			tag.setTag("Item", tag1);
		}
		
		if(paintItem != null)
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			paintItem.writeToNBT(tag1);
			tag.setTag("Paint", tag1);
		}
		
		tag.setInteger("Price", price);
		tag.setByte("Rot", rotation);
		tag.setBoolean("CanSell", canSell);
		tag.setBoolean("CanBuy", canBuy);
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is)
	{
		super.onPlacedBy(ep, is);
		rotation = (byte)LatCoreMC.get2DRotation(ep).ordinal();
		canBuy = true;
		canSell = false;
		price = 0;
	}
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, int side, float x, float y, float z)
	{
		if(worldObj.isRemote) return true;
		
		if(ep.capabilities.isCreativeMode && ep.isSneaking())
		{
			ep.openGui(Coins.inst, 0, worldObj, xCoord, yCoord, zCoord);
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
				if(!ep.capabilities.isCreativeMode && !PlayerCoins.take(ep, price, true))
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
				PlayerCoins.set(ep, PlayerCoins.get(ep.getUniqueID()) + price);
			}
		}
	}
	
	public ItemStack getPaint(int s)
	{ return paintItem; }
	
	public boolean setPaint(ItemStack is, EntityPlayer ep, int s)
	{
		if(ep != null && !ep.capabilities.isCreativeMode) return false;
		
		if(paintItem == null || is == null || !paintItem.isItemEqual(is))
		{
			paintItem = is;
			markDirty();
			return true;
		}
		
		return false;
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
}