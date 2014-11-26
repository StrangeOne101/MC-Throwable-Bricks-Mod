package so101.bricks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum EnumBricks 
{	
	REGULAR (((Item)Item.itemRegistry.getObject("brick")), new ItemStack(ThrowableBricksMod.brickChunks)),
	NETHER(((Item)Item.itemRegistry.getObject("netherbrick")), new ItemStack(ThrowableBricksMod.brickChunks, 1, 1)), 
	TNT(null, null), 
	UNKNOWN(null, null);
	
	protected Item brick;
	protected ItemStack chunkItem;
	
	EnumBricks(Item brick, ItemStack brickChunk)
	{
		this.brick = brick;
		this.chunkItem = brickChunk;
	}
	
	public static boolean canDrop(EnumBricks en)
	{
		try
		{
			return en.brick != null;
		}
		catch (NullPointerException e) {}
		return false;
	}
}
