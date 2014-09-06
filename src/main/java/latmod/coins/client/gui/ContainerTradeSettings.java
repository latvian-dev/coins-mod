package latmod.coins.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import latmod.core.mod.gui.ContainerLM;

public class ContainerTradeSettings extends ContainerLM
{
	public ContainerTradeSettings(EntityPlayer ep, IInventory i)
	{
		super(ep, i);
		
		addPlayerSlots(52);
	}
}