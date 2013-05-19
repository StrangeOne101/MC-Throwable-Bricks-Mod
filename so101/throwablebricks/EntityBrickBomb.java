package so101.throwablebricks;

import so101.throwablebricks.Main;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBrickBomb extends EntityBrick
{
	protected boolean isCreative = false;
	protected Entity player = this;
	
	public EntityBrickBomb(World par1World)
    {
        super(par1World);
    }

    public EntityBrickBomb(World par1World, EntityLiving par2EntityLiving, boolean par3)
    {
        super(par1World, par2EntityLiving, par3);
        player = par2EntityLiving;
        isCreative = par3;
    }

    public EntityBrickBomb(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }


	public int getDropID()
	{
		return Main.BrickBomb.itemID;
	}
    
    protected void onImpact(MovingObjectPosition m)
    {
    	super.onImpact(m);
    	
    	int blockID = this.worldObj.getBlockId(m.blockX, m.blockY, m.blockZ); 
    	
    	if(!this.getIsAvoidable(blockID, worldObj.getBlockMetadata(m.blockX, m.blockY, m.blockZ)) || blockID == Block.leaves.blockID)
    	{
    		if (!worldObj.isRemote)
    		{
    			worldObj.createExplosion(player, posX, posY, posZ, 4F, true);
    		}	
    	}
    }
}
