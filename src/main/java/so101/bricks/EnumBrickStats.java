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

public enum EnumBrickStats 
{
	BRICK("-", 0.35F, 0.3F),
	NETHERBRICK("-", 0.375F, 0.35F),
	TNTBRICK("-", 0.375F, 0.25F),
	ITEM("-", 0.2F, 0.6F),
	LEAD("lead", 0.5F, 0.2F),
	COPPER("copper", 0.4F, 0.5F),
	DIAMOND("diamond", 0.6F, 0.95F),
	OBSIDIAN("obsidian", 0.6F, 0.95F),
	BRONZE("bronze", 0.4F, 0.5F),
	IRON("iron", 0.48F, 0.45F),
	GOLD("gold", 0.95F, 0.18F),
	PLATNIUM("platnium", 1.0F, 0.85F),
	SHINY("shiny", 1.0F, 0.85F),
	NICKEL("nickel", 0.45F, 0.48F),
	SILVER("silver", 0.45F, 0.40F),
	STEEL("steel", 0.58F, 0.6F),
	TIN("tin", 0.35F, 0.25F),
	;
	/**Brick name*/
	public String brickName;
	/**How fast the brick falls. Between 0.0 and 1.0. Default is 0.35*/
	public float weight = 0.35F;
	/**How dense or strong the brick/ingot/alloy is. This will say if the brick can break 
	 * or not. Between 0.0 and 1.0, 1.0 being unbreakable. Default is 0.35*/
	public float density = 0.35F;
	
	public static Map<String, EnumBrickStats> values = new HashMap<String, EnumBrickStats>();
	
	EnumBrickStats(String brickName, float weight, float density)
	{
		this.brickName = brickName;
		this.weight = weight;
		this.density = density;
		//addToValues(brickName, this);
	}
	
	protected static void addToValues(String brick, EnumBrickStats enumvalue)
	{
		if (!brick.equals("-"))
		{
			EnumBrickStats.values.put(brick, enumvalue);
		}
	}
	
	public static EnumBrickStats getStatsForItem(ItemStack item)
	{
		if (item.getUnlocalizedName().toLowerCase().contains("brick"))
		{
			if (item.getItem().equals(Items.netherbrick))  {  return NETHERBRICK;  }
			else if (item.getItem().equals(ThrowableBricksMod.brickTNT))  {  return TNTBRICK;  }
			else
			{
				return BRICK;
			}
		}
		else if (item.getUnlocalizedName().toLowerCase().contains("ingot"))
		{
			String s = OreDictionary.getOreName(Item.getIdFromItem(item.getItem()));
			if (s.startsWith("ingot"))
			{
				String s1 = s.replaceFirst("ingot", "");
				
				List l = Arrays.asList(BRICK.getDeclaringClass().getEnumConstants());
				for (int i = 0; i < l.size(); i++)
				{
					if (l.get(i).toString().toLowerCase().equals(s1.toLowerCase()))
					{
						return (EnumBrickStats) l.get(i);
					}	
				}				
			}
		}
		
		return ITEM;
	}
}
