package latmod.coins.client.gui;

import latmod.core.gui.ContainerLM;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTradeSettings extends ContainerLM
{
	public ContainerTradeSettings(EntityPlayer ep)
	{
		super(ep, null);
		addPlayerSlots(52);
	}
}