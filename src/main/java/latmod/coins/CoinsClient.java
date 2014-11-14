package latmod.coins;
import latmod.coins.client.render.*;
import latmod.coins.tile.TileTrade;
import latmod.core.LMProxy;
import latmod.core.client.LatCoreMCClient;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CoinsClient extends LMProxy
{
	public void preInit(FMLPreInitializationEvent e)
	{
		RenderTradeWorld.instance.register();
		LatCoreMCClient.addTileRenderer(TileTrade.class, new RenderTrade());
	}
}