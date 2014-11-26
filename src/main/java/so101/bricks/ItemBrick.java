package so101.bricks;

import java.io.InvalidObjectException;
import java.util.List;

import cpw.mods.fml.client.config.GuiConfigEntries.ChatColorEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBrick extends Item
{
	EnumBricks type;
	
	public ItemBrick(EnumBricks enumnum) 
	{
		this.type = enumnum;
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) 
	{
		if (this.type == EnumBricks.REGULAR || this.type == EnumBricks.NETHER || this.type == EnumBricks.UNKNOWN)
		{
			/*Regular and nether bricks are no longer called in ItemBrick. 
			 * Because the default brick and nether brick objects can 
			 * no longer be overriden due to the new id system,
			 * they are called from @SubscribeEvent in the event bus
			 * */
			try 
			{
				throw new InvalidObjectException("Invalid ItemBrick type - cannot be type "+type.toString());
			} 
			catch (InvalidObjectException e)  {e.printStackTrace();}
		}
		if (this.type == EnumBricks.TNT)
		{
			//Create TNT brick entity.
			EntityBrick brick = new EntityBrick(world, player, EnumBricks.TNT);
			if (itemstack.hasDisplayName())
			{
				brick.getEntityData().setString("CustomName", itemstack.getDisplayName());
			}
			
			if (!player.capabilities.isCreativeMode)
			{
				itemstack.stackSize--;
			}
			if (player.getHeldItem().getTagCompound() != null)
			{
				if (player.getHeldItem().getTagCompound().hasKey("Damage"))
				{
					brick.damage = player.getHeldItem().getTagCompound().getFloat("Damage");
				}
				if (player.getHeldItem().getTagCompound().hasKey("ExplosionRadius"))
				{
					brick.explosionSize = player.getHeldItem().getTagCompound().getByte("ExplosionRadius");
				}
				if (player.getHeldItem().getTagCompound().hasKey("ShouldDrop"))
				{
					brick.shouldDrop = player.getHeldItem().getTagCompound().getBoolean("ShouldDrop");
				}
			}
			
			if (!world.isRemote)
			{
				world.spawnEntityInWorld(brick);
			}
		}
		
		return super.onItemRightClick(itemstack, world, player);
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4) 
	{
		list.add("§r§7Right click to throw!");
	}
}
