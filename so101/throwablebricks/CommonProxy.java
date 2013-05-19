package so101.throwablebricks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import so101.throwablebricks.Main;

public class CommonProxy implements IGuiHandler
{
	public void registerRenderInformation()
	{
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { //For GUI's
		return null;
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { //For GUI's
		return null;
	}
	public void registerTiles()
	{ 
	}
public void registerBlocks()
{ 
}
public void addNames()
{ 
	LanguageRegistry.addName(new ItemStack(Main.Brick,1), "Brick");
	LanguageRegistry.addName(new ItemStack(Main.BrickNether,1), "Nether Brick");
	LanguageRegistry.addName(new ItemStack(Main.BrickBomb,1), "TNT Brick");
	
	LanguageRegistry.addName(new ItemStack(Main.BrickChunk, 1, 0), "Brick Chunks");
	LanguageRegistry.addName(new ItemStack(Main.BrickChunk, 1, 1), "Nether Brick Chunks");
	LanguageRegistry.addName(new ItemStack(Main.BrickChunk, 1, 2), "Hacked Brick Chunks");

}
public void addRecipes()
{ 
	GameRegistry.addRecipe(new ItemStack(Main.Brick,1), "XX", "XX", Character.valueOf('X'), new ItemStack(Main.BrickChunk,1,0));
	GameRegistry.addRecipe(new ItemStack(Main.BrickNether,1), "XX", "XX", Character.valueOf('X'), new ItemStack(Main.BrickChunk,1,1)); 
	GameRegistry.addRecipe(new ItemStack(Main.BrickBomb,1), "GSG", "SBS", "GSG", Character.valueOf('S'), Item.gunpowder, Character.valueOf('G'), Block.sand, Character.valueOf('B'), Main.Brick);
	GameRegistry.addRecipe(new ItemStack(Main.BrickBomb,1), "GSG", "SBS", "GSG", Character.valueOf('S'), Item.gunpowder, Character.valueOf('G'), Block.sand, Character.valueOf('B'), Main.BrickNether);
	GameRegistry.addShapelessRecipe(new ItemStack(Main.BrickBomb,1), Main.Brick, Block.tnt);
	GameRegistry.addShapelessRecipe(new ItemStack(Main.BrickBomb,1), Main.BrickNether, Block.tnt);
}
}
