package latmod.coins.tile;

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
		TileTrade t = (TileTrade) data.getTileEntity();
		
		if(t != null && !t.isInvalid() && t.tradeItem != null)
		{
			if(t.price < 0 || (!t.canBuy && !t.canSell))
			{
				l.add(t.tradeItem.getDisplayName());
			}
			else
			{
				String s = t.tradeItem.getDisplayName();
				if(t.tradeItem.stackSize > 1)
					s = t.tradeItem.stackSize + " x " + s;
				
				l.add(s);
				if(t.price == 0)
					l.add("For free");
				else
					l.add("For " + t.price + " coins");
				l.add("Buy:" + t.canBuy + " | Sell:" + t.canSell);
			}
		}
		
		return l;
	}
}