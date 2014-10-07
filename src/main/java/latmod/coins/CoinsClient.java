package latmod.coins;
import latmod.coins.block.BlockTrade;
import latmod.coins.client.render.*;
import latmod.coins.tile.TileTrade;
import latmod.core.client.LatCoreMCClient;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CoinsClient extends CoinsCommon
{
	public void preInit()
	{
		LatCoreMCClient.addBlockRenderer(BlockTrade.renderID = LatCoreMCClient.getNewBlockRenderID(), new RenderTradeWorld());
		LatCoreMCClient.addTileRenderer(TileTrade.class, new RenderTrade());
	}

	public void init()
	{
	}

	public void postInit()
	{
	}
}