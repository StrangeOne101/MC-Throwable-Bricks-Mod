package so101.bricks.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import so101.bricks.EntityBrick;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderBrick extends Render
{
	public RenderBrick() 
	{
		super();
	}

	public void doRender(Entity eentity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) 
	{
		ItemStack iconStack;
		EntityBrick entity = (EntityBrick)eentity;
		ItemStack item = entity.getDataWatcher().getWatchableObjectItemStack(16);
		if (item == null)
		{
			iconStack = new ItemStack(Items.cake, 1);
		}
		else
		{
			iconStack = item;
		}
		
		IIcon iicon = iconStack.getIconIndex();
		
	    if (iicon != null)
	    {
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        GL11.glScalef(0.5F, 0.5F, 0.5F);
	        //this.preRender(entity);
	        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
	        ResourceLocation resourcelocation = texturemanager.getResourceLocation(iconStack.getItemSpriteNumber());
	        TextureAtlasSprite missingno = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
	        this.bindTexture(resourcelocation);
	        Tessellator tessellator = Tessellator.instance;
	
	
	        this.doStuffWithTessellator(tessellator, iicon);
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glPopMatrix();
	    }
		
	}
	
	
	private void doStuffWithTessellator(Tessellator tessellator, IIcon iicon)
    {
        float minX = iicon.getMinU();
        float maxX = iicon.getMaxU();
        float minZ = iicon.getMinV();
        float maxZ = iicon.getMaxV();
        float full = 1.0F;
        float centerOffset = 0.5F;
        float quarter = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - centerOffset), (double)(0.0F - quarter), 0.0D, (double)minX, (double)maxZ);
        tessellator.addVertexWithUV((double)(full - centerOffset), (double)(0.0F - quarter), 0.0D, (double)maxX, (double)maxZ);
        tessellator.addVertexWithUV((double)(full - centerOffset), (double)(full - quarter), 0.0D, (double)maxX, (double)minZ);
        tessellator.addVertexWithUV((double)(0.0F - centerOffset), (double)(full - quarter), 0.0D, (double)minX, (double)minZ);
        tessellator.draw();
    }
   
    /**Prerender method for entity. Used for scaling. WIP*/
    public void preRender(EntityBrick entity)
    {
    	if (entity.getEntityData().getFloat("Scale") != 1.0F)
    	{
    		float scale = entity.getEntityData().getFloat("Scale");
    		GL11.glScalef(scale, scale, scale);
    	}
    	
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) 
	{
		//No longer needed
		return null;
	}
    
}
