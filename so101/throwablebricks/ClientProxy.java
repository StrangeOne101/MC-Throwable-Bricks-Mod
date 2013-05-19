package so101.throwablebricks;

import cpw.mods.fml.client.registry.RenderingRegistry;
import so101.throwablebricks.CommonProxy;
import so101.throwablebricks.Main;
import so101.throwablebricks.EntityBrick;
import so101.throwablebricks.EntityBrickBomb;
import so101.throwablebricks.EntityBrickNether;
//import ThrowableBricks.bricks.EntityBrickNether;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy 
{
    @Override    
	public void registerRenderInformation()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBrick.class, new RenderSnowball(Main.Brick, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrickNether.class, new RenderSnowball(Main.BrickNether, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrickBomb.class, new RenderSnowball(Main.BrickBomb, 0));
		/**     The above is for 1.5+      **/
		//MinecraftForgeClient.preloadTexture("/ThrowableBricks/items.png");
	}

}