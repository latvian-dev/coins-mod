package latmod.coins;
import latmod.coins.game.*;
import latmod.core.client.LatCoreClient;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CoinsClient extends CoinsCommon
{
	public void preInit()
	{
		LatCoreClient.addTileRenderer(TileTrade.class, new RenderTrade());
	}

	public void init()
	{
	}

	public void postInit()
	{
	}
}