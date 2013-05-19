package so101.throwablebricks;

import java.util.Random;
import java.util.Arrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import so101.throwablebricks.DamageSourceBrick;
import so101.throwablebricks.Main;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBrick extends EntityThrowable
{
	private int currentAge;
	boolean isCreative = false;
	
	private boolean hitBlock = false;
	private int drops = 2;
	private int dropsOther = 0;
	
	private int block;
	private int damageValue;
	
	boolean created = false;
	double startY;
	double highestY;
	double finishY;
	
	public boolean dispensed = false;
	
	byte damage = -1;
	float weight = 0.1F;
	float velocity = 0.75F;
	
	protected int[] blocksAvoided;
	protected int blocksAvoidedAmount = 0;
	
	int heightDifference = 0;
	
	//Minecraft mc = 
	
    public EntityBrick(World par1World)
    {
        super(par1World);
        blocksAvoided = new int[37];
        this.initDefaultBlocksBreakable();
    }

    public EntityBrick(World par1World, EntityLiving par2EntityLiving, boolean par3)
    {
        super(par1World, par2EntityLiving);
        blocksAvoided = new int[37];
        this.initDefaultBlocksBreakable();
        isCreative = par3; //I mainly use this method to spawn in bricks, so I added a boolean if the player was creative or not.
    }

    public EntityBrick(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        blocksAvoided = new int[37];
        this.initDefaultBlocksBreakable();
    }
	
	public void onUpdate()
	{
		super.onUpdate();
		currentAge++;
		
		if (!created)
		{
			created = true;
			startY = posY;
		}
		
		if (posY > startY)
		{
			highestY = posY;
		}
		
		if(dispensed)
		{
			this.posY-=0.15D;
		}
		
		int blockIn = this.worldObj.getBlockId((int)this.posX, (int)this.posY, (int)this.posZ);
		
		if (blockIn == Block.web.blockID)
        {
        	//this.velocity = 0.4F;
			this.setInWeb();
        }
		else if (blockIn == 0)
		{
			this.velocity = 0.75F;
		}
	}
	
	protected float getGravityVelocity()
    {
        return this.weight;
    }
	
	
	/**
	 * Gets the weight of the entity
	 */
	protected float func_70182_d()
    {
        return 0.75F;
    }
	
	/**
	 * Returns the main drop item ID.
	 * Needed for other bricks so not all bricks drop normal bricks, but different ones.
	 */
	public int getDropID()
	{
		return Main.Brick.itemID;
	}
	
	/**
	 * Returns the metadata for the brick chunk being dropped. Used for nether bricks 
	 */
	public int getChunkDropMetadata()
	{
		return 0;
	}
	
	/**
	 * Returns the collision size of the entity
	 * Make bricks more reliable at breaking blocks
	 */
	public float getCollisionBorderSize()
    {
        return 0.15F;
    }
	
	/*public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Damage", (byte)this.damage);
        par1NBTTagCompound.setFloat("Velocity", this.velocity);
        par1NBTTagCompound.setFloat("Weight", this.weight);
    }*/
	
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		if (par1NBTTagCompound.hasKey("Damage"))
		{
			this.damage = (byte) (par1NBTTagCompound.getByte("Damage") & 255);
		}
		
		if (par1NBTTagCompound.hasKey("Velocity"))
		{
			this.velocity = par1NBTTagCompound.getFloat("Velocity");
		}
		
		if (par1NBTTagCompound.hasKey("Weight"))
		{
			this.weight = par1NBTTagCompound.getFloat("Weight");
		}        
    }

	
	protected void spawnParticles(int ID, boolean isBlock)
	{
		if (!isBlock)
		{
			for (int i = 0; i<8 ; i++)
			{
				this.worldObj.spawnParticle("iconcrack_"+ID+"", this.posX, this.posY, this.posZ, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D);
			}
		}
		else if (isBlock)
		{
			for (int i = 0; i<8 ; i++)
			{
				//this.worldObj.spawnParticle("tilecrack_"+ID+"", this.posX, this.posY, this.posZ, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D);
			}
			
			for (int i = 0; i<8 ; i++)
			{
				//this.worldObj.spawnParticle("tilecrack_"+ID+"0", this.posX, this.posY, this.posZ, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D, (rand.nextDouble()/4)-0.1D);
			}
		}
	}
	
	/**
	 * Used to destroy the blocks the brick hits. Only certain blocks, and in switch bellow. Args: BlockID, blockX, blockY, blockZ, damageValue
	 */
	protected boolean modifyHitBlocks(int blockID, int blockX, int blockY, int blockZ, int damageValue)
	{
		if (!worldObj.isRemote)
		{
			switch (blockID)
	    	{
	    	case 83: //sugercane
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.reed, 1));
	    		worldObj.spawnEntityInWorld(item);
	    		return true;
			case 20: //glass
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;
	    	case 102: //glass panes
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;
	    	case 50: //torches
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		EntityItem item2 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.torchWood, 1));
	    		worldObj.spawnEntityInWorld(item2);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;
	    	case 55: //redstone wire
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		EntityItem item3 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.redstone, 1));
	    		worldObj.spawnEntityInWorld(item3);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;
	    	case 59: //wheat crops
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		EntityItem item4 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.seeds,1));
	    		worldObj.spawnEntityInWorld(item4);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		if (damageValue >= 7)
	    		{
	    			EntityItem item5 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.wheat, 1));
	        		worldObj.spawnEntityInWorld(item5);
	        		EntityItem iitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.seeds, rand.nextInt(1)+1));
	        		worldObj.spawnEntityInWorld(iitem);
	        		 
	    		}
	    		return true;
	    	case 60: //farmland
	    		worldObj.setBlock(blockX, blockY, blockZ, Block.dirt.blockID);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;
	    	case 75: //redstone torch (on)
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item6 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.torchRedstoneActive, 1));
	    		worldObj.spawnEntityInWorld(item6);
	    		return true;
	    	case 76: //redstone torch (off)
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item7 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.torchRedstoneActive, 1));
	    		worldObj.spawnEntityInWorld(item7);
	    		return true;
	    	case 104: //melon stem
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item8 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.pumpkinSeeds, 1));
	    		worldObj.spawnEntityInWorld(item8);
	    		return true;
	    	case 105: //pumkin stem
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item9 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.melonSeeds, 1));
	    		worldObj.spawnEntityInWorld(item9);
	    		return true;
	    	case 115: //netherwart
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item10 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.netherStalkSeeds, 1));
	    		worldObj.spawnEntityInWorld(item10);
	    		if (damageValue == 3)
	    		{
	    			worldObj.spawnEntityInWorld(item10);
	    			if (rand.nextInt(1)==0)
	    			{
	    				worldObj.spawnEntityInWorld(item10);
	    			}
	    		}
	    		return true;
	    	case 127: //coco pods
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item11 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.dyePowder, 1, 3));
	    		worldObj.spawnEntityInWorld(item11);
	    		if (damageValue == 2)
	    		{
	    			EntityItem item12 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.dyePowder, rand.nextInt(1)+1, 3));
	        		worldObj.spawnEntityInWorld(item12);
	    		}
	    		return true;
	    	case 31: //tall grass & fern
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		if (damageValue == 1 && rand.nextInt(8)==0)
	    		{
	    			EntityItem item13 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.seeds, 1));
	        		worldObj.spawnEntityInWorld(item13);
	    		}
	    		else if (damageValue == 2)
	    		{
	    			EntityItem item88 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.tallGrass, 1, damageValue));
	        		worldObj.spawnEntityInWorld(item88);
	    		}
	    		return true;	
	    	case 37: //yellow flower
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item14 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.plantYellow));
	    		worldObj.spawnEntityInWorld(item14);
	    		return true;
	    	case 38: //red flower
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		EntityItem item15 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.plantRed));
	    		worldObj.spawnEntityInWorld(item15);
	    		return true;	
	    	case 39: //brown mushroom
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item16 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.mushroomBrown));
	    		worldObj.spawnEntityInWorld(item16);
	    		return true;	
	    	case 40: //red mushroom
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item17 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.mushroomRed));
	    		worldObj.spawnEntityInWorld(item17);
	    		return true;	
	    	case 106: //vine
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		return true;	
	    	case 132: //tripwire
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item18 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.silk));
	    		worldObj.spawnEntityInWorld(item18);
	    		return true;	
	    	case 141: //carrot crops
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem item19 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.carrot));
	    		worldObj.spawnEntityInWorld(item19);
	    		if (damageValue == 2)
	    		{
	    			EntityItem iitem1 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.carrot, rand.nextInt(1)+1));
	        		worldObj.spawnEntityInWorld(iitem1);
	    		}
	    		return true;	
	    	case 142: //potato crops
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		EntityItem item20 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.potato));
	    		worldObj.spawnEntityInWorld(item20);
	    		if (damageValue == 2)
	    		{
	    			if (rand.nextInt(50)==0)
	    			{
	    				EntityItem iitem2 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.potato, rand.nextInt(1)+1));
	    				worldObj.spawnEntityInWorld(iitem2);
	    			}
	    			else
	    			{
	    				EntityItem iitem3 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.poisonousPotato, 1));
	    				worldObj.spawnEntityInWorld(iitem3);
	    				EntityItem iitem4 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.potato, rand.nextInt(1)));
	    				worldObj.spawnEntityInWorld(iitem4);
	    			}
	    		}
	    		return true;	
	    	case 18: //leaves
	    		return true;
	    	case 6: //saplings
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem5 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.sapling, 1, damageValue));
				worldObj.spawnEntityInWorld(iitem5);
				return true;
	    	case 30: //web
	    		return true;
	    	case 51: //fire
	    		return true;
	    	case 79: //ice
	    		if (rand.nextInt(4)==0)
	    		{
	    			worldObj.setBlock(blockX, blockY, blockZ, Block.waterStill.blockID);
	    			worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    			return true;
	    		}
	    	case 78: //snow
	    		return true;
	    	case 131: //trip wire hook
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem56 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Block.tripWireSource, 1));
				worldObj.spawnEntityInWorld(iitem56);
	    		return true;
	    	case 149: //comparator 
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem57 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.comparator, 1));
				worldObj.spawnEntityInWorld(iitem57);
	    		return true;
	    	case 150: //comparator (other state)
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem58 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.comparator, 1));
				worldObj.spawnEntityInWorld(iitem58);
	    		return true;
	    	case 93: //repeater
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem59 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.redstoneRepeater, 1));
				worldObj.spawnEntityInWorld(iitem59);
	    		return true;
	    	case 94: //repeater (other state)
	    		worldObj.setBlock(blockX, blockY, blockZ, 0);
	    		worldObj.markBlockForUpdate(blockX, blockY, blockZ);
	    		EntityItem iitem60 = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.redstoneRepeater, 1));
				worldObj.spawnEntityInWorld(iitem60);
	    		return true;	
	    		
	    		
	    	}
		}
		
		return false;
    	
	}
	
	
    /**
     * Called when the brick hits a block or entity.
     */
	protected void onImpact(MovingObjectPosition m)
    {
    	block = this.worldObj.getBlockId(m.blockX, m.blockY, m.blockZ); 
    	damageValue = this.worldObj.getBlockMetadata(m.blockX, m.blockY, m.blockZ); //Get metadata of block hit
        
    	int a;
    	modifyHitBlocks(block, m.blockX, m.blockY, m.blockZ, damageValue);
    	
    	if (this.getIsAvoidable(block, damageValue))
    	{
    		if (worldObj.isRemote)
    		{
    			spawnParticles(block, true);
    		}
    	}
    	else
    	{
    		this.dropBrickChunks();
    		if (worldObj.isRemote)
    		{
    			spawnParticles(getDropID(), false);
    		}
    	}
    	
    	//this.spawnParticlesBrick();
    	/*if (worldObj.isBlockNormalCube(m.blockX, m.blockY, m.blockZ))
    	{
    		this.setDead();
    	}*/
 
        if(block == Block.tnt.blockID)
        {
        	if(this.getDropID() == Main.BrickNether.itemID)
        	{
        		this.worldObj.setBlock(m.blockX, m.blockY, m.blockZ, 0);   
                worldObj.createExplosion(this.getThrower(), m.blockX, m.blockY, m.blockZ, 5F, true);
        	}
        	else
        	{
        		this.worldObj.setBlock(m.blockX, m.blockY, m.blockZ, 0);   
                worldObj.createExplosion(this.getThrower(), m.blockX, m.blockY, m.blockZ, 4F, true);
        	}
        	 
        }
        
        
        
        /*if(block == Block.ice.blockID && rand.nextInt(5) == 0)
        {
        	this.worldObj.setBlockWithNotify(m.blockX, m.blockY, m.blockZ, Block.waterMoving.blockID);
        	this.mc.sndManager.playSound("random.ice", (float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F, 2.0F, 1.0F); //Play glass sound
        }*/
    	
		
		
        if (m.entityHit != null)
        {
        	finishY = posY;
        	
        	heightDifference = (int)highestY - (int)finishY;
        	
        	byte byte0 = 0;
        	
        	//byte byte0 = (byte)((heightDifference/4)+5);
            if (damage == -1)
            {
            	byte0 = 5;
            }
            else
            {
            	byte0 = damage;
            }

            if (m.entityHit instanceof EntitySkeleton && damage == 5)
            {
                byte0 = 11;
            }
            
            if (rand.nextInt(10)==0)
            {
            	byte0++;
            }
            else if (rand.nextInt(15)==0)
            {
            	byte0--;
            }

            if (!m.entityHit.attackEntityFrom(DamageSourceBrick.causeBrickDamage(this, this.getThrower()), byte0));
        }
		else
		{
			//World.setBlockWithNotify((int)this.posX, (int)this.posY, (int)this.posZ, 19);
		}
        
        
        for (int i = 0; i < 8; i++)
        {
        	if(block != Block.leaves.blockID && block != Block.glass.blockID && block != Block.thinGlass.blockID && block != Block.vine.blockID && block != Block.tallGrass.blockID && block != Block.brewingStand.blockID && block != Block.deadBush.blockID && block != Block.web.blockID && block != Block.fire.blockID && block != Block.sapling.blockID && block != Block.plantRed.blockID && block != Block.plantYellow.blockID && block != Block.reed.blockID)
        	{
        		if (worldObj.isRemote)
        		{
        			//spawnParticles(this.getDropID(), false);
        		}
        	}
        	else
        	{
        		if (worldObj.isRemote)
        		{
        			//spawnParticles(block, true);
        		}
        	}
        }

        
    }
	
	protected void initDefaultBlocksBreakable()
	{
		this.addBlockAvoid(Block.glass.blockID);
		this.addBlockAvoid(Block.thinGlass.blockID);
		this.addBlockAvoid(Block.reed.blockID);
		this.addBlockAvoid(Block.torchWood.blockID);
		this.addBlockAvoid(Block.redstoneWire.blockID);
		this.addBlockAvoid(Block.redstoneRepeaterIdle.blockID);
		this.addBlockAvoid(Block.redstoneRepeaterActive.blockID);
		this.addBlockAvoid(Block.redstoneComparatorIdle.blockID);
		this.addBlockAvoid(Block.redstoneComparatorActive.blockID);
		this.addBlockAvoid(Block.tallGrass.blockID);
		this.addBlockAvoid(Block.torchRedstoneActive.blockID);
		this.addBlockAvoid(Block.torchRedstoneIdle.blockID);
		this.addBlockAvoid(Block.deadBush.blockID);
		this.addBlockAvoid(Block.crops.blockID);
		this.addBlockAvoid(Block.blockSnow.blockID);
		this.addBlockAvoid(Block.plantRed.blockID);
		this.addBlockAvoid(Block.plantYellow.blockID);
		this.addBlockAvoid(Block.sapling.blockID);
		this.addBlockAvoid(Block.netherStalk.blockID);
		this.addBlockAvoid(Block.lever.blockID);
		this.addBlockAvoid(Block.vine.blockID);
		this.addBlockAvoid(Block.melonStem.blockID);
		this.addBlockAvoid(Block.pumpkinStem.blockID);
		this.addBlockAvoid(Block.mushroomCapBrown.blockID);
		this.addBlockAvoid(Block.mushroomCapRed.blockID);
		this.addBlockAvoid(Block.cocoaPlant.blockID);
		this.addBlockAvoid(Block.carrot.blockID);
		this.addBlockAvoid(Block.potato.blockID);
		this.addBlockAvoid(Block.skull.blockID);
		this.addBlockAvoid(Block.tripWire.blockID);
		this.addBlockAvoid(Block.leaves.blockID);
		this.addBlockAvoid(Block.brewingStand.blockID);
		this.addBlockAvoid(Block.web.blockID);
		this.addBlockAvoid(Block.signPost.blockID);
		this.addBlockAvoid(Block.signWall.blockID);
		this.addBlockAvoid(Block.fire.blockID);
		this.addBlockAvoid(Block.snow.blockID);
	}
	
	
	/**
	 * Adds a block to the list of breakable blocks. Can be used by other mods.
	 * @param blockID
	 */
	public void addBlockAvoid(int blockID)
	{
		this.blocksAvoided[blocksAvoidedAmount] = blockID;
		this.blocksAvoidedAmount++;
	}
	
	
	/**
	 * Gets if the block hit is breakable or not.
	 * @param blockID
	 * @param damageValue
	 */
	protected boolean getIsAvoidable(int blockID, int damageValue)
	{
		for (int b = 0; b < this.blocksAvoidedAmount; b++)
		{
			if (blockID == this.blocksAvoided[b])
			{
				return true;
			}
		}
		return false;
	}
        
	/**
	 * Spawns the brick chunks into world if nessasary
	 */
    protected void dropBrickChunks()
    {
    	if (!worldObj.isRemote)
        {
            if((!this.getIsAvoidable(block, damageValue)) && hitBlock == false)
            {
            	this.setDead();
            	if (currentAge <= 7)
            	{
            		if (rand.nextInt(40)==0) 
            		{
            			drops = rand.nextInt(2) + 1;
            			dropsOther = 0;
            		}
            		else
            		{
            			drops = 0;
            			dropsOther = 1;
            		}
            	}
            	else
            	{
            		if (rand.nextInt(100)!=0) 
            		{
            			drops = rand.nextInt(2) + 1;
            			dropsOther = 0;
            		}
            		else
    				{
            			drops = 0;
            			dropsOther = 1;
    				}
            	}
            		
            	if (!isCreative && getDropID() != Main.BrickBomb.itemID)
            	{
            		for (int i = 0; i < drops; i++)
            		{
            			EntityItem chunk = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Main.BrickChunk, 1, this.getChunkDropMetadata()));
        	        	worldObj.spawnEntityInWorld(chunk);
            				
            			//this.dropItem(Main.BrickChunk.itemID, 1);
            		}
            			
            		for (int i = 0; i < dropsOther; i++)
            		{
            			this.dropItem(this.getDropID(), 1);
            		}
            	}
            }
            else
            {
            	hitBlock = true;
            }
        }
    }
}
