package latmod.coins.game;

import latmod.coins.CoinsItems;
import latmod.core.client.RenderBlocksCustom;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderTradeWorld implements ISimpleBlockRenderingHandler
{
	public RenderBlocksCustom renderBlocks = new RenderBlocksCustom();
	
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		renderBlocks.customMetadata = 0;
		renderBlocks.setCustomColor(null);
		renderBlocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
		renderBlocks.setOverrideBlockTexture(CoinsItems.b_trade.getBlockIcon());
		renderBlocks.renderBlockAsItem(Blocks.stone, 0, 1F);
	}
	
	public boolean renderWorldBlock(IBlockAccess iba, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		TileTrade t = (TileTrade)iba.getTileEntity(x, y, z);
		
		if(t != null && !t.isInvalid())
		{
			ItemStack tex = t.getPaint();
			Block renderBlock = CoinsItems.b_trade;
			
			if(tex == null)
			{
				renderBlocks.customMetadata = 0;
				renderBlocks.setCustomColor(null);
			}
			else
			{
				renderBlock = Block.getBlockFromItem(tex.getItem());
				renderBlocks.customMetadata = tex.getItemDamage();
				renderBlocks.setCustomColor(renderBlock.colorMultiplier(iba, x, y, z));
			}
			renderBlocks.blockAccess = t.getWorldObj();
			renderBlocks.clearOverrideBlockTexture();
			renderBlocks.setRenderBounds(0D, 0D, 0D, 1D, 1D, 1D);
			renderBlocks.renderStandardBlock(renderBlock, t.xCoord, t.yCoord, t.zCoord);
		}
		
		return true;
	}
	
	public boolean shouldRender3DInInventory(int modelId)
	{ return true; }
	
	public int getRenderId()
	{ return BlockTrade.renderID; }
}