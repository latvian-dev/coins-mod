package latmod.coins.tile;

import java.util.UUID;

import latmod.coins.*;
import latmod.core.*;
import latmod.core.mod.tile.*;
import latmod.core.util.FastList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

public class TileTrade extends TileLM implements IPaintable, IClientActionTile
{
	public ItemStack tradeItem;
	public ItemStack renderItem;
	private ItemStack paintItem;
	public int price;
	public byte rotation;
	public boolean canSell;
	public boolean canBuy;
	public int sellOnlyOne;
	public final FastList<UUID> playersBought = new FastList<UUID>();
	public int redstoneTimer;
	public int redstoneTimerTick;
	
	public boolean rerenderBlock()
	{ return true; }
	
	public void onUpdate()
	{
		if(!worldObj.isRemote && redstoneTimerTick > 0)
		{
			redstoneTimerTick--;
			
			if(redstoneTimerTick == 0)
			{
				markDirty();
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType);
			}
		}
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
		
		sellOnlyOne = tag.getByte("SellOnlyOne");
		
		if(sellOnlyOne == 2)
		{
			playersBought.clear();
			
			NBTTagList l = tag.getTagList("Players", LatCoreMC.NBT_STRING);
			for(int i = 0; i < l.tagCount(); i++)
				playersBought.add(UUID.fromString(l.getStringTagAt(i)));
		}
		
		redstoneTimer = tag.getByte("RSTimer");
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
		tag.setByte("SellOnlyOne", (byte)sellOnlyOne);
		
		if(sellOnlyOne == 2)
		{
			NBTTagList l = new NBTTagList();
			
			for(int i = 0; i < playersBought.size(); i++)
				l.appendTag(new NBTTagString(playersBought.get(i).toString()));
			
			if(l.tagCount() > 0)
				tag.setTag("Players", l);
		}
		
		tag.setByte("RSTimer", (byte)redstoneTimer);
	}
	
	public void onPlacedBy(EntityPlayer ep, ItemStack is)
	{
		super.onPlacedBy(ep, is);
		rotation = (byte)LatCoreMC.get2DRotation(ep).ordinal();
	}
	
	public boolean onRightClick(EntityPlayer ep, ItemStack is, int side, float x, float y, float z)
	{
		if(worldObj.isRemote) return true;
		
		if(ep.capabilities.isCreativeMode && ep.isSneaking())
		{
			ep.openGui(Coins.inst, 0, worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		
		if(price < 0) return true;
		
		if(tradeItem != null && tradeItem.getItem() != null)
		{
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
	
	public ItemStack getPaint()
	{ return paintItem; }
	
	public boolean setPaint(ItemStack is, EntityPlayer ep)
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
	
	public void onClientAction(EntityPlayer ep, String action, NBTTagCompound data)
	{
		if(action.equals("RSTimer"))
		{
			redstoneTimer = data.getInteger("Timer");
		}
		else if(action.equals("TradeItem"))
		{
			if(data.hasNoTags())
				tradeItem = null;
			else tradeItem = ItemStack.loadItemStackFromNBT(data);
		}
		else super.onClientAction(ep, action, data);
		markDirty();
	}
}