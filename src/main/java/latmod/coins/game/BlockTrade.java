package latmod.coins.game;

import latmod.coins.Coins;
import latmod.core.mod.block.BlockLM;
import latmod.core.mod.tile.TileLM;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.*;

public class BlockTrade extends BlockLM
{
	public static BlockTrade inst;
	
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
}