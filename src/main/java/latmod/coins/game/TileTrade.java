package latmod.coins.game;

import latmod.coins.PlayerCoins;
import latmod.core.*;
import latmod.core.mod.tile.TileLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileTrade extends TileLM
{
	public ItemStack tradeItem;
	public int price;
	public byte rotation;
	public boolean canSell;
	
	public void readTileData(NBTTagCompound tag)
	{
		if(!tag.hasKey("Item")) tradeItem = null; else
			tradeItem = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
		price = tag.getInteger("Price");
		rotation = tag.getByte("Rot");
		canSell = tag.getBoolean("CanSell");
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
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is)
	{
		super.onPlacedBy(ep, is);
		rotation = (byte)LMUtils.get2DRotation(ep).ordinal();
	}
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, int side, float x, float y, float z)
	{
		if(worldObj.isRemote) return true;
		
		if(tradeItem != null && tradeItem.getItem() != null)
		{
			if(price != 0)
			{
				if(!PlayerCoins.take(ep, price, true))
				{
					LatCore.printChat(ep, "You can't afford that!");
					return true;
				}
			}
			
			LMUtils.dropItem(ep, tradeItem);
		}
		
		return true;
	}
	
	public void onLeftClick(EntityPlayer ep)
	{
		if(worldObj.isRemote) return;
		
		if(tradeItem != null && tradeItem.getItem() != null && canSell)
		{
			if(price == 0)
			{
				LatCore.printChat(ep, "This item is for free, you can't sell that!");
				return;
			}
		}
		
		return;
	}
}