package so101.throwablebricks;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public final class DispenserBehaviorBrickBomb extends BehaviorProjectileDispense
{
    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
    	EntityBrickBomb brick = new EntityBrickBomb(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    	brick.dispensed = true;
    	return brick; 
    }
}
