package so101.bricks;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy 
{
	@Override
	public void registerRendering() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBrick.class, new RenderBrick());//new RenderSnowball((Item) Item.itemRegistry.getObject("brick")));
	}
}
