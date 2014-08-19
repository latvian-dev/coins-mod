package latmod.coins.game;
import java.util.Random;

import latmod.coins.*;
import latmod.core.mod.LMMod;
import latmod.core.mod.item.ItemLM;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.*;

public class ItemCoins extends ItemLM
{
	public static ItemCoins inst;
	
	public static ItemStack COINS_1;
	public static ItemStack COINS_10;
	public static ItemStack COINS_100;
	public static ItemStack COINS_1000;
	public static ItemStack COINS_10000;
	
	public ItemCoins(String s)
	{
		super(s);
		setMaxStackSize(1);
		
		COINS_1 = create(1);
		COINS_10 = create(10);
		COINS_100 = create(100);
		COINS_1000 = create(1000);
		COINS_10000 = create(10000);
	}
	
	public LMMod getMod()
	{ return Coins.mod; }
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab()
	{ return CreativeTabs.tabMisc; }
	
	public String getUnlocalizedName(ItemStack is)
	{ return super.getUnlocalizedName(is) + (isPlural(getCoins(is)) ? "_p" : ""); }
	
	public String getItemStackDisplayName(ItemStack is)
	{ return "" + EnumChatFormatting.GOLD + getCoins(is) + EnumChatFormatting.RESET + " " + super.getItemStackDisplayName(is); }
	
	public void onPostLoaded()
	{
		itemsAdded.add(COINS_1);
		itemsAdded.add(COINS_10);
		itemsAdded.add(COINS_100);
		itemsAdded.add(COINS_1000);
		itemsAdded.add(COINS_10000);
	}
	
	public ItemStack create(long i)
	{
		ItemStack is = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("Coins", i);
		is.setTagCompound(tag);
		return is;
	}
	
	public ItemStack createRandom(Random r, int i)
	{ return create(r.nextInt(i) + 1); }
	
	public long getCoins(ItemStack is)
	{
		if(is == null || !is.hasTagCompound()) return 0;
		return is.stackTagCompound.getLong("Coins");
	}
	
	public boolean isPlural(long l)
	{
		String s = "" + l;
		return !s.endsWith("1") || s.endsWith("11");
	}
	
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer ep)
	{
		ep.renderBrokenItemStack(is);
		
		long c = getCoins(is);
		PlayerCoins.set(ep, PlayerCoins.get(ep) + c);
		//LatCore.printChat(ep, "Added " + c + " coins");
		
		if(!ep.capabilities.isCreativeMode) is.stackSize--;
		
		return is;
	}
}