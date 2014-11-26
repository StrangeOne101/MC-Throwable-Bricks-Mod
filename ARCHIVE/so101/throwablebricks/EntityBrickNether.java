package so101.throwablebricks;

import so101.throwablebricks.Main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBrickNether extends EntityBrick
{
	protected boolean isCreative = false;
	protected boolean isRaining = false;
	
	protected int fireLevel = 1;
	
	public EntityBrickNether(World par1World)
    {
        super(par1World);
    }
	
	public void onUpdate()
	{
		super.onUpdate();
		if (worldObj.isRaining() && !isRaining)
		{
			this.isRaining = true;
			fireLevel = 0;
		}
	}

    public EntityBrickNether(World par1World, EntityLiving par2EntityLiving, boolean par3)
    {
        super(par1World, par2EntityLiving, par3);
        isCreative = par3;
    }

    public EntityBrickNether(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }


	public int getDropID()
	{
		return Main.BrickNether.itemID;
	}
	
	public int getChunkDropMetadata()
	{
		return 1;
	}
    
    protected void onImpact(MovingObjectPosition m)
    {
    	super.onImpact(m);
    	
    	for (int i=0;i<fireLevel+1;i++)
		{
			worldObj.spawnParticle("lava",this.posX, this.posY, this.posZ, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D);
		}
    	
    	if (m.entityHit != null)
    	{
    		Entity entity = m.entityHit;
    		boolean isHit = entity.isEntityAlive();
    		
    		if (isHit && rand.nextInt(5)>1 && !this.isRaining)
    		{
    			entity.setFire(rand.nextInt(5)+3);
    		}
    		else if (!isHit)
    		{
    			
    		}
    	}
    	else 
    	{
    		if (rand.nextInt(5)>1 && worldObj.getBlockId((int)posX, (int)posY, (int)posZ)==0 && fireLevel != 0)
    		{
    			if(!this.worldObj.isRemote)
    			{
    				worldObj.setBlock((int)posX, (int)posY, (int)posZ, Block.fire.blockID);
    			}
    			for (int i=0;i<3+fireLevel;i++)
				{
					worldObj.spawnParticle("lava",this.posX, this.posY, this.posZ, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D);
				}
    			
    		}
			
    	}
    	
    	
    	
    	
    }
}
