package latmod.coins;
import latmod.coins.block.BlockTrade;
import latmod.coins.client.gui.*;
import latmod.coins.client.render.*;
import latmod.coins.tile.TileTrade;
import latmod.core.client.LatCoreMCClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
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
	
	public Object getClientGuiElement(int ID, EntityPlayer ep, World w, int x, int y, int z)
	{
		IInventory inv = (IInventory)w.getTileEntity(x, y, z);
		return new GuiTradeSettings(new ContainerTradeSettings(ep, inv));
	}
}