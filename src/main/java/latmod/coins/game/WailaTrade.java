package latmod.coins.game;

import java.util.List;

import net.minecraft.item.ItemStack;
import latmod.core.apis.BasicWailaHandler;
import mcp.mobius.waila.api.*;

public class WailaTrade extends BasicWailaHandler
{
	public WailaTrade() throws Exception
	{
		super(false, true, false);
	}
	
	public List<String> getWailaBody(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
	{
		TileTrade t = (TileTrade)data.getTileEntity();
		
		if(t != null && !t.isInvalid() && t.tradeItem != null)
		{
			String s = t.tradeItem.getDisplayName();
			if(t.tradeItem.stackSize > 1)
				s = t.tradeItem.stackSize + " x " + s;
			
			l.add(s);
			l.add("For " + t.price + " coins");
			l.add("Can sell: " + t.canSell);
		}
		
		return l;
	}
}