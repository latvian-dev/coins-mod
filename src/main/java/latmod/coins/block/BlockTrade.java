package latmod.coins.block;

import latmod.coins.Coins;
import latmod.coins.client.render.RenderTradeWorld;
import latmod.coins.tile.TileTrade;
import latmod.core.LMMod;
import latmod.core.block.BlockLM;
import latmod.core.tile.TileLM;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.*;

public class BlockTrade extends BlockLM
{
	public BlockTrade(String s)
	{
		super(s, Material.wood);
		setBlockUnbreakable();
		isBlockContainer = true;
		Coins.mod.addTile(TileTrade.class, s);
	}
	
	public LMMod getMod()
	{ return Coins.mod; }
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTabToDisplayOn()
	{ return CreativeTabs.tabMisc; }
	
	public void onBlockClicked(World w, int x, int y, int z, EntityPlayer ep)
	{
		TileTrade t = (TileTrade)w.getTileEntity(x, y, z);
		if(t != null && !t.isInvalid()) t.onLeftClick(ep);
	}
	
	public TileLM createNewTileEntity(World w, int m)
	{ return new TileTrade(); }
	
	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{ return RenderTradeWorld.instance.getRenderId(); }
	
	public boolean isOpaqueCube()
	{ return false; }
	
	public boolean renderAsNormalBlock()
	{ return false; }
	
	public boolean isBlockSolid(IBlockAccess iba, int x, int y, int z, int side)
	{ return true; }
	
	public boolean isSideSolid(IBlockAccess iba, int x, int y, int z, ForgeDirection side)
	{ return true; }
	
	public int isProvidingWeakPower(IBlockAccess iba, int x, int y, int z, int s)
	{
		//TileTrade t = (TileTrade)iba.getTileEntity(x, y, z);
		//return (t != null && t.redstoneTimerTick != 0) ? 15 : 0;
		return 0;
	}
	
	public boolean canProvidePower()
	{ return false; }
}