package so101.bricks.item;

import so101.bricks.ConfigBricks;
import so101.bricks.EntityBrick;
import so101.bricks.EnumBricks;
import so101.bricks.ThrowableBricksMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemCannon extends Item
{
	public CannonLevel cannonType;
	
	public enum CannonLevel
	{
		BRICK, ITEM, INGOT;
	}
	
	public ItemCannon() 
	{
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setUnlocalizedName("brickCannon");
		this.setTextureName(ThrowableBricksMod.modID + ":cannon");
		this.setFull3D();
		this.setMaxDamage(256);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) 
	{
		InventoryPlayer pinv = player.inventory;
		ItemStack itemS = null;
		int gunpowder = 0;
		int i;
		int i1 = 0;
		for (i = 0; i < pinv.mainInventory.length; i++)
		{
			if (ThrowableBricksMod.canThrow(pinv.mainInventory[i]) && itemS == null)
			{
				itemS = pinv.mainInventory[i];
				i1 = i;
			}
			else if (pinv.mainInventory[i] != null && pinv.mainInventory[i].getItem() == Items.gunpowder)
			{
				gunpowder += pinv.mainInventory[i].stackSize;
			}
			if (itemS != null && (gunpowder != 0 || !ConfigBricks.brickCannonsGunpowder))
			{
				break;
			}
		}
		
		if (gunpowder == 0 && !player.capabilities.isCreativeMode)
		{
			player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "note.hat", 3F, 2F);
			return item;
		}
		
		//0.00291789 * Math.exp^(5.93221 * x) 
		
		if (itemS != null )//&& (gunpowder != 0 || !ConfigBricks.brickCannonsGunpowder))
		{
			//double gdouble = 0.00291789 * Math.exp(5.93221 * ((EnumBricks.getStatsForItem(itemS).density + EnumBricks.getStatsForItem(itemS).weight) / 2));
			int powder = 0;
			
			int scale = 5;
			
			if (ConfigBricks.brickCannonsGunpowder && !player.capabilities.isCreativeMode)
			{
				powder = 1 + world.rand.nextInt(3);
				if (gunpowder < powder)
				{
					try {
						scale /= powder / gunpowder;
					}
					catch (ArithmeticException e) {} //Divide be zero error
				}
					
			}
			
			if (ThrowableBricksMod.DEBUG)
			{
				player.addChatComponentMessage(new ChatComponentText("Gpowder: " + gunpowder));
				player.addChatComponentMessage(new ChatComponentText("Powder: " + powder));
			}
			
			
			EntityBrick brick = new EntityBrick(player.worldObj, player, itemS.copy());
			boolean flag1 = itemS.getItem().equals(ThrowableBricksMod.brickTNT) || (itemS.hasTagCompound() && itemS.getTagCompound().hasKey("ExplosionRadius"));
			brick.shouldDrop = !player.capabilities.isCreativeMode;
			brick.explosionSize = flag1 ? 4 : 0;
			if (itemS.hasDisplayName())
			{
				brick.getEntityData().setString("CustomName", itemS.getDisplayName());
			}
			if (itemS.getTagCompound() != null)
			{
				if (itemS.getTagCompound().hasKey("Damage"))
				{
					brick.damage = itemS.getTagCompound().getFloat("Damage");
				}
				if (itemS.getTagCompound().hasKey("ExplosionRadius"))
				{
					brick.explosionSize = itemS.getTagCompound().getByte("ExplosionRadius");
				}
				if (itemS.getTagCompound().hasKey("ShouldDrop"))
				{
					brick.shouldDrop = itemS.getTagCompound().getBoolean("ShouldDrop");
				}
			}
			if (!player.worldObj.isRemote)
			{
				player.worldObj.spawnEntityInWorld(brick);
				brick.motionX *= scale;
				brick.motionY *= scale;
				brick.motionZ *= scale;
				
			}	

			
			//itemS.stackSize = itemS.stackSize > 1 ? itemS.stackSize - 1 : 0;
			/*if (itemS.stackSize == 0)
			{
				pinv.mainInventory[i1] = null;
			}*/
			//pinv.decrStackSize(i1, 1);
			if (!player.capabilities.isCreativeMode && !world.isRemote)
			{
				item.damageItem(1, player);
				pinv.decrStackSize(i1, 1);
				
				double d = (EnumBricks.getStatsForItem(itemS).density * (EnumBricks.getStatsForItem(itemS).weight * 10));
				d *= d;
				//d -= d;
				
				if (!world.isRemote)
				{
					float d1 = ((EnumBricks.getStatsForItem(itemS).density + EnumBricks.getStatsForItem(itemS).weight) / 2);
					d1 = d1 - 0.3F + (world.rand.nextFloat() * d1);
					d1 *= 10;
					item.damageItem((int) d1, player);
					if (ThrowableBricksMod.DEBUG)
					{
						player.addChatComponentMessage(new ChatComponentText("Dmg: " + d1));
					}
				}
			}
			
			if (ConfigBricks.brickCannonsGunpowder && !world.isRemote && !player.capabilities.isCreativeMode)
			{
				/*for (int k = 0; k < pinv.mainInventory.length && powder > 0; k++)
				{
					ItemStack is = pinv.mainInventory[k];
					if (is != null && is.getItem() == Items.gunpowder)
					{
						is.stackSize -= powder;
						if (is.stackSize <= 0)
						{
							powder = Math.abs(is.stackSize);
							pinv.mainInventory[i] = null;
							pinv.
						}
						else
						{
							powder = 0;
							break;
						}
					}
					if (powder <= 0)
					{
						break;
					}
				}*/
				for (int k = 0; k < powder; k++)
				{
					pinv.consumeInventoryItem(Items.gunpowder);
				}
			}
		}
		player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, itemS != null ? "random.explode" : "note.hat", 3F, 2F);
		/*if (world.isRemote)
		{*/
			String particle = itemS != null ? "largesmoke" : "smoke";
			int extraRand = itemS != null ? 2 : 8;
			
			for (int j = 0; j < 5 + world.rand.nextInt(extraRand); j++)
			{
				double x = player.posX - 0.5 + world.rand.nextDouble();
				double y = player.posY - 1 + world.rand.nextDouble();
				double z = player.posZ - 0.5 + world.rand.nextDouble();
				world.spawnParticle(particle, x, itemS != null ? y - 0.5 : y, z, 0.0, 0.0, 0.0);
			}
		//}
		
		return item;
	}

	@Override
	public boolean getIsRepairable(ItemStack cannon, ItemStack item) 
	{
		if (item.getItem() != null && Block.getBlockFromItem(item.getItem()) == Blocks.brick_block)
		{
			return true;
		}
		return false;
	}
}
