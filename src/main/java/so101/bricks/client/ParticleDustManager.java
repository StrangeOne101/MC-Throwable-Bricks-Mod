package so101.bricks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ParticleDustManager 
{
	private static Minecraft mc = Minecraft.getMinecraft();
	private static World theWorld = mc.theWorld;
	
	public ParticleDustManager INSTANCE;
	
	public ParticleDustManager() 
	{
		INSTANCE = this;
	}
	
	public EntityFX spawnParticle(String particleName, double x, double y, double z, double par8, double par10, double par12, double red, double green, double blue, double scale)
	{
		if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
		{
			int var14 = mc.gameSettings.particleSetting;

			if (var14 == 1 && theWorld.rand.nextInt(3) == 0)
			{
				var14 = 2;
			}

			double var15 = mc.renderViewEntity.posX - x;
			double var17 = mc.renderViewEntity.posY - y;
			double var19 = mc.renderViewEntity.posZ - z;
			EntityFX var21 = null;
			double var22 = 16.0D;

			if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22)
			{
				return null;
			}
			else if (var14 > 1)
			{
				return null;
			}
			else
			{
				if (particleName.equals("dust"))
				{
					var21 = new EntityDustFX(theWorld, x, y, z, (float)par8, (float)par10, (float)par12, (float)red, (float)green, (float)blue, (float)scale);
				}
				mc.effectRenderer.addEffect((EntityFX)var21);
				return (EntityFX)var21;
			}
		}
		return null;
	}

	
	
	@SideOnly(Side.CLIENT)
	private class EntityDustFX extends EntityFX
	{
	    private float portalParticleScale;
	    private double portalPosX;
	    private double portalPosY;
	    private double portalPosZ;

	    public EntityDustFX(World par1World, double x, double y, double z, double velX, double velY, double velZ, double red, double green, double blue, double scale)
	    {
	        super(par1World, x, y, z, velX, velY, velZ);
	        this.motionX = velX;
	        this.motionY = velY;
	        this.motionZ = velZ;
	        this.portalPosX = this.posX = x;
	        this.portalPosY = this.posY = y;
	        this.portalPosZ = this.posZ = z;
	        //float f = this.rand.nextFloat() * 0.6F + 0.4F;
	        this.particleScale *= 0.7;
	        this.particleScale *= scale;
	        this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
	        this.particleRed = (float)red;
	        this.particleGreen = (float) green;
	        this.particleBlue = (float)blue;
	        this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
	        this.noClip = true;
	        this.setParticleTextureIndex((int)(Math.random() * 8.0D));
	    }

	    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	    {
	        float f6 = ((float)this.particleAge + par2) / (float)this.particleMaxAge;
	        f6 = 1.0F - f6;
	        f6 *= f6;
	        f6 = 1.0F - f6;
	        this.particleScale = this.portalParticleScale * f6;
	        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	    }

	    public int getBrightnessForRender(float par1)
	    {
	        int i = super.getBrightnessForRender(par1);
	        float f1 = (float)this.particleAge / (float)this.particleMaxAge;
	        f1 *= f1;
	        f1 *= f1;
	        int j = i & 255;
	        int k = i >> 16 & 255;
	        k += (int)(f1 * 15.0F * 16.0F);

	        if (k > 240)
	        {
	            k = 240;
	        }

	        return j | k << 16;
	    }

	    /**
	     * Gets how bright this entity is.
	     */
	    public float getBrightness(float par1)
	    {
	        float f1 = super.getBrightness(par1);
	        float f2 = (float)this.particleAge / (float)this.particleMaxAge;
	        f2 = f2 * f2 * f2 * f2;
	        return f1 * (1.0F - f2) + f2;
	    }

	    /**
	     * Called to update the entity's position/logic.
	     */
	    public void onUpdate()
	    {
	        this.prevPosX = this.posX;
	        this.prevPosY = this.posY;
	        this.prevPosZ = this.posZ;
	        float f = (float)this.particleAge / (float)this.particleMaxAge;
	        float f1 = f;
	        f = -f + f * f * 2.0F;
	        f = 1.0F - f;
	        this.posX = this.portalPosX + this.motionX * (double)f;
	        this.posY = this.portalPosY + this.motionY * (double)f + (double)(1.0F - f1);
	        this.posZ = this.portalPosZ + this.motionZ * (double)f;

	        if (this.particleAge++ >= this.particleMaxAge)
	        {
	            this.setDead();
	        }
	    }
	}
}
