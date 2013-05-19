package so101.throwablebricks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemBrick extends Item
{
	public boolean isNether = false;
	public boolean isBomb = false;
	private int strengthLv;
	
	public ItemBrick(int id, int type)
	{
		super(id);
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.tabCombat);
		if (type == 1)
		{
			isNether = true;
		}
		else if (type == 2)
		{
			isBomb = true;
		}
	}

	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            par1ItemStack.stackSize--;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (par3EntityPlayer.isPotionActive(Potion.damageBoost))
        {
        	
        }
        
        if (!par2World.isRemote && !isNether && !isBomb)
        {
            par2World.spawnEntityInWorld(new EntityBrick(par2World, par3EntityPlayer, par3EntityPlayer.capabilities.isCreativeMode));
        }
        
        if (!par2World.isRemote && isNether)
        {
            par2World.spawnEntityInWorld(new EntityBrickNether(par2World, par3EntityPlayer, par3EntityPlayer.capabilities.isCreativeMode));
        }
        
        if (!par2World.isRemote && isBomb)
        {
            par2World.spawnEntityInWorld(new EntityBrickBomb(par2World, par3EntityPlayer, par3EntityPlayer.capabilities.isCreativeMode));
        }
        
        return par1ItemStack;
    }
}