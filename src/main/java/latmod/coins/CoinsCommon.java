package latmod.coins;

import latmod.core.mod.gui.ContainerEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CoinsCommon implements IGuiHandler // CoinsClient
{
	public void preInit() {}
	public void init() {}
	public void postInit() {}
	
	public Object getServerGuiElement(int ID, EntityPlayer ep, World w, int x, int y, int z)
	{
		IInventory inv = (IInventory)w.getTileEntity(x, y, z);
		return new ContainerEmpty(ep, inv);
	}
	
	public Object getClientGuiElement(int ID, EntityPlayer ep, World w, int x, int y, int z)
	{
		return null;
	}
}