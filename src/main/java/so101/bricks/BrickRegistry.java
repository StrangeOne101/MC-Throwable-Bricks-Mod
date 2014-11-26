package so101.bricks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BrickRegistry 
{
	public static List<ItemStack> brickIngotNames = new ArrayList<ItemStack>();
	public static Map<String, EnumBrickStats> brickStats = new HashMap<String, EnumBrickStats>();
	
	static
	{
		for (String s : OreDictionary.getOreNames())
		{
			if (s.startsWith("ingot") || s.startsWith("brick"))
			{
				for (ItemStack i : OreDictionary.getOres(s))
				{
					brickIngotNames.add(i);
				}
			}
		}
	}
}
