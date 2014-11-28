package latmod.coins.client.render;

import latmod.coins.Coins;
import latmod.coins.tile.TileTrade;
import latmod.core.client.BlockRendererLM;
import latmod.core.tile.IPaintable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderTradeWorld extends BlockRendererLM
{
	public static final RenderTradeWorld instance = new RenderTradeWorld();
	
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		renderBlocks.customMetadata = 0;
		renderBlocks.setCustomColor(null);
		renderBlocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
		renderBlocks.setOverrideBlockTexture(Coins.b_trade.getBlockIcon());
		renderBlocks.renderBlockAsItem(Blocks.stone, 0, 1F);
	}
	
	public boolean renderWorldBlock(IBlockAccess iba, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		TileTrade t = (TileTrade)iba.getTileEntity(x, y, z);
		
		if(t == null || t.isInvalid()) return false;
		
		renderBlocks.blockAccess = t.getWorldObj();
		renderBlocks.clearOverrideBlockTexture();
		IPaintable.Renderer.renderCube(renderBlocks, t.getPaint(), IPaintable.Renderer.to6(Coins.b_trade.getIcon(0, 0)), x, y, z, renderBlocks.fullBlock);
		
		return true;
	}
	
	public boolean shouldRender3DInInventory(int modelId)
	{ return true; }
}