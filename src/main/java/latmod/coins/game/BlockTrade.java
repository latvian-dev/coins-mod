package latmod.coins.game;

import latmod.coins.Coins;
import latmod.core.client.LatCoreMCClient;
import latmod.core.mod.block.BlockLM;
import latmod.core.mod.tile.TileLM;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.*;

public class BlockTrade extends BlockLM
{
	public static int renderID = -1;
	
	public BlockTrade(String s)
	{
		super(Coins.mod, s, Material.wood);
		isBlockContainer = true;
		setBlockUnbreakable();
		Coins.mod.addTile(TileTrade.class, s);
	}
	
	public void onBlockClicked(World w, int x, int y, int z, EntityPlayer ep)
	{
		TileTrade t = (TileTrade)w.getTileEntity(x, y, z);
		if(t != null && !t.isInvalid()) t.onLeftClick(ep);
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTabToDisplayOn()
	{ return CreativeTabs.tabMisc; }
	
	public TileLM createNewTileEntity(World w, int m)
	{ return new TileTrade(); }
	
	public boolean isOpaqueCube()
	{ return false; }
	
	public boolean renderAsNormalBlock()
	{ return false; }
	
	public int getRenderType()
	{ return renderID; }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int s)
	{ return LatCoreMCClient.blockNullIcon; }
	
	public boolean isBlockSolid(IBlockAccess iba, int x, int y, int z, int side)
	{ return true; }
	
	public boolean isSideSolid(IBlockAccess iba, int x, int y, int z, ForgeDirection side)
	{ return true; }
}