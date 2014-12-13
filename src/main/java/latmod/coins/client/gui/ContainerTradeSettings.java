package latmod.coins.client.gui;

import latmod.core.gui.ContainerLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerTradeSettings extends ContainerLM
{
	public ContainerTradeSettings(EntityPlayer ep, IInventory i)
	{
		super(ep, i);
		
		addPlayerSlots(52);
	}
}