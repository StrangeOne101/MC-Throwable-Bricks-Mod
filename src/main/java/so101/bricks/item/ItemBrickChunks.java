package so101.bricks.item;

import java.util.List;

import so101.bricks.ThrowableBricksMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBrickChunks extends Item
{
	private IIcon[] iconChunks = new IIcon[3];
	
	public ItemBrickChunks() 
	{
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) 
	{
		iconChunks[0] = reg.registerIcon(ThrowableBricksMod.modID + ":brickChunk");
		iconChunks[1] = reg.registerIcon(ThrowableBricksMod.modID + ":brickChunkNether");
		iconChunks[2] = reg.registerIcon(ThrowableBricksMod.modID + ":brickChunkHacked");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack item) 
	{
		if (item.getItemDamage() == 0)
		{
			return "item.brickChunks.regular";
		}
		else if (item.getItemDamage() == 1)
		{
			return "item.brickChunks.nether";
		}
		
		return "item.brickChunks.hacked";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int i) 
	{
		return iconChunks[i <= 1 ? i : 2];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) 
	{
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}
}
