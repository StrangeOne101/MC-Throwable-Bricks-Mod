package so101.bricks;

import java.lang.reflect.Array;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class EntityBrick extends EntityThrowable
{
	/**Should the brick drop or not. By default, creative sets this to false and all other gamemodes return true. Can be changed with NBT.*/
	public boolean shouldDrop;
	/**Size and scale of the brick. WIP - does not work.*/
	public float scale;
	/**The explosion size or radius for explosive/tnt bricks. Defaults to 4.*/
	public int explosionSize;
	/**The amount of damage the brick does. Default base is 5. Can increase by distance, force, motion and falling. Can be customized by NBT.*/
	public float damage;
	
	protected double[] spawnLoc, highLoc, endLoc;
	
	public EntityBrick(World par1World) 
	{
		super(par1World);
		//this.brickType = EnumBricks.REGULAR;
		if (this.getEntityData().hasKey("Scale") && this.getEntityData().getFloat("Scale") != 1.0F)
		{
			this.setSize(this.getEntityData().getFloat("Scale"), this.getEntityData().getFloat("Scale"));
		}
		init();
	}
	
	/*@Deprecated
	public EntityBrick(World world, EntityPlayer player, EnumBricks type) 
	{
		super(world, player);
		this.brickType = type;
		init();
		this.shouldDrop = !player.capabilities.isCreativeMode;
	}*/
	
	public EntityBrick(World world, EntityPlayer player, ItemStack brick)
	{
		super(world, player);
		brick.stackSize = 1;
		init();
		this.shouldDrop = !player.capabilities.isCreativeMode;
		this.getDataWatcher().updateObject(16, brick);
	}
	
	/**Call and initialize basic variables*/
	public void init() 
	{
		this.shouldDrop = true;
		this.scale = 1.0F;
		this.damage = 5.5F;
	}
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) 
	{		
		/**
		 * Damage hit entity
		 * */
		if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) //Hit a mob
		{
			mop.entityHit.attackEntityFrom(DamageSourceBricks.causeBrickDamage(this, this.getThrower()), this.damage);
		}
		
		
		
		
		/**
		 * Break blocks
		 * */
		if (mop.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
		{
			Block blockHit = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
			boolean flag = blockHit.getMaterial() == Material.glass || blockHit.getMaterial() == Material.plants || blockHit.getMaterial() == Material.circuits || blockHit.getMaterial() == Material.vine;
			if (flag)
			{
				int meta = this.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
				if (!this.worldObj.isRemote)
				{
					blockHit.dropBlockAsItem(this.worldObj, mop.blockX, mop.blockY, mop.blockZ, meta, 0);
					this.worldObj.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
					this.worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, blockHit.stepSound.getBreakSound(), 1.0F, 1.0F);
				}
				
				for (int i = 0; i < 32 && this.worldObj.isRemote; i++)
				{
					this.worldObj.spawnParticle("blockcrack_"+Block.getIdFromBlock(blockHit)+"_"+(meta << 12), mop.blockX, mop.blockY, mop.blockZ, this.getRndMinorDouble(), this.getRndMinorDouble(), this.getRndMinorDouble());
				}
				return;
			}
		}
		
		
		ItemStack drop = null;
		/**
		 * Drop brick or brick chunks
		 * */
		if (this.shouldDrop && this.explosionSize <= 0)
		{
			double velXZ = Math.abs(this.motionX) + Math.abs(this.motionZ);
			double velY = Math.abs(this.motionY);
			
			float density = EnumBrickStats.getStatsForItem(this.dataWatcher.getWatchableObjectItemStack(16)).density;
			double maxVelocity = 3 * (density * density) + 0.5;
			
			boolean flag = velXZ + velY > maxVelocity;
			
			ItemStack item = this.getDataWatcher().getWatchableObjectItemStack(16);
			drop = item.getItem().equals(Items.brick) ? new ItemStack(ThrowableBricksMod.brickChunks, 1 + this.rand.nextInt(2), 0) : (item.getItem().equals(Items.netherbrick) ? new ItemStack(ThrowableBricksMod.brickChunks, 1 + this.rand.nextInt(2), 1) : null);
			
			if (item.getUnlocalizedName().toLowerCase().contains("ingot"))
			{
				String s2 = OreDictionary.getOreName(OreDictionary.getOreID(item));
				String s = s2.replaceFirst("ingot", "");
				String s1 = "nugget" + s;
				ItemStack nugget = OreDictionary.getOres(s1).size() > 0 ? OreDictionary.getOres(s1).get(0) : null;
				if (nugget != null)
				{
					drop = new ItemStack(nugget.getItem(), this.rand.nextInt(5) + 1, nugget.getItemDamage());
				}
			}
			
			
			if (!this.worldObj.isRemote)
			{
				EntityItem eitem;
				if (flag && drop != null) //If it should break
				{
					eitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, drop);
				}
				else
				{
					eitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, item);
				}
				this.worldObj.spawnEntityInWorld(eitem);
			}
			//EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			//p.addChatComponentMessage(new ChatComponentText("Vel: " + this.motionX + " " + this.motionY + " " + this.motionZ));
			//p.addChatComponentMessage(new ChatComponentText("s: " +s+", s1: "+s1 + ", s2: "+s2));
		}
		
		//if (this.getThrower() != null && this.getThrower() instanceof EntityPlayer)
		//{
			//String s = this.worldObj.isRemote ? "CLIENT" : "SERVER";
			
			//p.addChatComponentMessage(new ChatComponentText(s + ": " + this.entityItemStack.getDisplayName()));
		//}
		
		for (int i = 0; i < 8 + (drop == null ? 16 : 0) && this.worldObj.isRemote; i++)
		{
			this.worldObj.spawnParticle("iconcrack_"+Item.itemRegistry.getIDForObject(this.dataWatcher.getWatchableObjectItemStack(16).getItem()), this.posX, this.posY, this.posZ, this.getRndMinorDouble(), this.getRndMinorDouble(), this.getRndMinorDouble());
		}
		
		
		if (!this.worldObj.isRemote && this.explosionSize > 0)
		{
			Entity cause = this;
			if (this.getThrower() != null)
			{
				cause = this.getThrower();
			}
			this.worldObj.createExplosion(cause, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, this.explosionSize, true);
		}
		
		this.setDead();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		/*if (nbt.hasKey("BrickType", 99))
		{
			int type = nbt.getInteger("BrickType");
			if (type == 0) {this.brickType = EnumBricks.REGULAR;}
			else if (type == 1) {this.brickType = EnumBricks.NETHER;}
			else if (type == 2) {this.brickType = EnumBricks.TNT;}
			else {this.brickType = EnumBricks.UNKNOWN;}
		}*/
		
		if (nbt.hasKey("ShouldDrop", 99))
		{
			this.shouldDrop = nbt.getBoolean("ShouldDrop");
		}
		if (nbt.hasKey("Scale", 99))
		{
			this.scale = nbt.getFloat("Scale");
		}
		if (nbt.hasKey("ExplosionRadius", 99))
		{
			this.explosionSize = nbt.getByte("ExplosionRadius");
		}
		if (nbt.hasKey("Damage"))
		{
			this.damage = nbt.getFloat("Damage");
		}
		if (nbt.hasKey("Item"))
		{
			ItemStack i = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
			if (i == null)
			{
				throw new NullPointerException("Tag \"Item\" returned null ItemStack");
			}
			else
			this.dataWatcher.updateObject(16, ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item")));
		}
		
		super.readFromNBT(nbt);
	}
	
	protected void entityInit()
    {
		super.entityInit();
		this.dataWatcher.addObject(16, new ItemStack(Items.brick, 1));
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		nbt.setBoolean("ShouldDrop", this.shouldDrop);
		nbt.setFloat("Scale", scale);
		nbt.setFloat("Damage", this.damage);
		nbt.setByte("ExplosionRadius", (byte)explosionSize);
		nbt.setTag("Item", this.dataWatcher.getWatchableObjectItemStack(16).writeToNBT(new NBTTagCompound()));
		/*if (this.brickType == EnumBricks.REGULAR) {nbt.setInteger("BrickType", 0);}
		else if (this.brickType == EnumBricks.NETHER) {nbt.setInteger("BrickType", 1);}
		else if (this.brickType == EnumBricks.TNT) {nbt.setInteger("BrickType", 2);}
		else {nbt.setInteger("BrickType", 0);}*/
		super.writeToNBT(nbt);
	}
	
	protected double getRndMinorDouble()
	{
		return (this.rand.nextDouble() - 0.5D) / 4;
	}

	protected float getGravityVelocity() 
	{
		return 0.1F;
	}
	
	protected float func_70182_d()
    {
        return 0.75F;
    }
	
	
}
