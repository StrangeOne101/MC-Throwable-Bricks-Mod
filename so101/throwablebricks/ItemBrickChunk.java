package so101.throwablebricks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import cpw.mods.fml.relauncher.*;
import so101.throwablebricks.Main;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBrickChunk extends Item 
{
	protected Icon[] iconChunk = new Icon[3];
	
	public ItemBrickChunk(int par1) 
	{
		super(par1); 
		setCreativeTab(CreativeTabs.tabMaterials); 
		setHasSubtypes(true); 
		setMaxDamage(0);
	}
	
	/**Adds the texture from the directory stated**/
	public void registerIcons(IconRegister iconRegister)
	{
		iconChunk[0] = iconRegister.registerIcon("Bricks:brickChunk");
		iconChunk[1] = iconRegister.registerIcon("Bricks:brickChunkNether");
		iconChunk[2] = iconRegister.registerIcon("Bricks:brickChunkHacked");	
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int i)
	{
		return iconChunk[i < 2 ? i : 2];
	}

	public String getItemDisplayName(ItemStack is)
	{
		switch (is.getItemDamage())
		{
			case 0: return "Brick Chunk";
			case 1: return "Nether Brick Chunk";
		}
		return "Hacked Brick Chunk";
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemID, CreativeTabs tab, List itemList)
	{
		for(int i=0;i<2;i++)
		{
			itemList.add(new ItemStack(itemID,1,i)); 
		}
	}
}