package so101.bricks;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorBrickDispense extends BehaviorDefaultDispenseItem
{
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack)
	{
        World world = iblocksource.getWorld();
        IPosition iposition = BlockDispenser.func_149939_a(iblocksource);
        EnumFacing enumfacing = BlockDispenser.func_149937_b(iblocksource.getBlockMetadata());
        EntityBrick brick = new EntityBrick(world, iposition.getX(), iposition.getY(), iposition.getZ());
        ItemStack i = itemstack.copy();
        i.stackSize = 1;
    	brick.getDataWatcher().updateObject(16, i);
    	brick.shouldDrop = true;
        IProjectile iprojectile = brick;
        iprojectile.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        world.spawnEntityInWorld((Entity)iprojectile);
        itemstack.splitStack(1);
        return itemstack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource p_82485_1_)
    {
        p_82485_1_.getWorld().playAuxSFX(1002, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
    }
    
    protected float func_82498_a()
    {
        return 4.0F;
    }

    protected float func_82500_b()
    {
        return 1.1F;
    }

}
