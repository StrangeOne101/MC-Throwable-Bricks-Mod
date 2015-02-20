package so101.bricks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scala.actors.threadpool.Arrays;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public enum EnumBricks 
{
	//Break forumla: max v = 3 * (strength ^ 2) + 0.5
	BRICK(0.35F, 0.3F),
	NETHERBRICK(0.375F, 0.35F, EnumBrickAbilities.BURNING, 0.68F),
	TNTBRICK(0.375F, 0.25F, EnumBrickAbilities.EXPLOSIVE, 4F),
	ITEM(0.2F, 0.6F, EnumBrickAbilities.ITEM, 1.0F),
	LEAD(0.5F, 0.2F),
	COPPER(0.4F, 0.5F),
	DIAMOND(0.6F, 0.95F),
	OBSIDIAN(0.6F, 0.95F),
	BRONZE(0.4F, 0.5F),
	IRON(0.48F, 0.45F),
	GOLD(0.95F, 0.18F),
	PLATINUM(1.0F, 0.85F),
	SHINY(1.0F, 0.85F),
	MYTHRIL(1.0F, 0.85F),
	MITHRIL(1.0F, 0.85F),
	NICKEL(0.45F, 0.48F),
	SILVER(0.45F, 0.40F),
	STEEL( 0.58F, 0.6F),
	TIN(0.35F, 0.25F),
	OSMIUM(0.42F, 0.48F),
	BEDROCKIUM(2F, 0.8F, EnumBrickAbilities.UNBREAKABLE, 1F),
	ENDERIUM(1.0F, 0.8F, EnumBrickAbilities.ENDER, 1F),
	ALUMINUM(0.46F, 0.38F),
	UNSTABLE(0.8F, 0.5F, EnumBrickAbilities.EXPLODEONSHATTER, 0.05F),
	BLUTONIUM(0.8F, 0.5F, EnumBrickAbilities.EXPLODEONSHATTER, 0.25F),
	FAIRY(0.4F, 0.5F, EnumBrickAbilities.SPARKLY, 0.5F),
	INVAR(0.49F, 0.5F),
	ELECTRUM(0.44F, 0.44F),
	ELECTRUMFLUX(0.42F, 0.44F, EnumBrickAbilities.REDSTONE, 0.8F),
	REDSTONEALLOY(0.50F, 0.43F, EnumBrickAbilities.REDSTONE, 0.3F),
	FIERY(0.45F, 0.49F, EnumBrickAbilities.BURNING, 0.68F),
	SEAREDBRICK(0.35F, 0.35F),
	;
	
	/**How fast the brick falls. Between 0.0 and 1.0. Default is 0.35*/
	public float weight = 0.35F;
	/**How dense or strong the brick/ingot/alloy is. This will say if the brick can break 
	 * or not. Between 0.0 and 1.0, 1.0 being unbreakable. Default is 0.35*/
	public float density = 0.35F;
	
	/**Special ability the brick or ingot has*/
	public EnumBrickAbilities special = EnumBrickAbilities.NONE;
	/**How great the ability is. 0.0 being nothing, 1.0 being completely*/
	public float abilityScale = 0.0F;
	
	/**A list of ingots that can be thrown but aren't picked up normally by the mod. Created because enderio ingots didn't contain 
	 * "ingots" in there unlocalized name*/
	public static List<String> ingotExceptionsList = new ArrayList<String>();
	
	/**A list of items that can be thrown but are not meant to be thrown. Created because other mod issues where the name
	 * still lets it be picked up as an ingot.*/
	public static List<String> ingotExceptionsExclude = new ArrayList<String>();
	
	EnumBricks(float weight, float density)
	{
		this.weight = weight;
		this.density = density;
	}
	
	EnumBricks(float weight, float density, EnumBrickAbilities special, float scale)
	{
		this.weight = weight;
		this.density = density;
		this.special = special;
		this.abilityScale = scale;
	}
	
	static 
	{
		ingotExceptionsList.add("darkSteel");
		ingotExceptionsList.add("electricalSteel");
		ingotExceptionsList.add("energeticAlloy");
		ingotExceptionsList.add("redstoneAlloy");
		ingotExceptionsList.add("conductiveIron");
		ingotExceptionsList.add("soularium");
		
		ingotExceptionsExclude.add("item.for.cratedBrick");
		ingotExceptionsExclude.add("item.for.cratedNetherBrick");
		ingotExceptionsExclude.add("item.steeleafIngot");
		ingotExceptionsExclude.add("item.tconstruct.MetalPattern.ingot");
	}
	
	public static EnumBricks getStatsForItem(ItemStack item)
	{
		boolean flag = false;
		for (String s : EnumBricks.ingotExceptionsList)
		{
			if (item.getUnlocalizedName().contains(s))
			{
				flag = true;
				break;
			}
		}
		
		if (item.getUnlocalizedName().toLowerCase().contains("brick"))
		{
			if (item.getItem().equals(Items.netherbrick))  {  return NETHERBRICK;  }
			else if (item.getItem().equals(ThrowableBricksMod.brickTNT))  {  return TNTBRICK;  }
			else
			{
				return BRICK;
			}
		}
		else if ((item.getUnlocalizedName().toLowerCase().contains("ingot") && !item.getUnlocalizedName().toLowerCase().contains("nugget")) || flag)
		{
			String s = OreDictionary.getOreName(OreDictionary.getOreID(item));
			if (s.startsWith("ingot"))
			{
				String s1 = s.replaceFirst("ingot", "");
				
				List<EnumBricks> l = new ArrayList<EnumBricks>();
				for (EnumBricks b : BRICK.getDeclaringClass().getEnumConstants())
				{
					l.add(b);
				}
				for (int i = 0; i < l.size(); i++)
				{
					if (l.get(i).toString().toLowerCase().equals(s1.toLowerCase()))
					{
						return (EnumBricks) l.get(i);
					}	
				}
				
			}
			else if (item.getUnlocalizedName().toLowerCase().contains("bedrockium"))
			{
				return BEDROCKIUM;
			}
			return IRON;
		}
		
		return ITEM;
	}
}
