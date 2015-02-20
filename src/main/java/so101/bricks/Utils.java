package so101.bricks;

import java.util.Calendar;

import net.minecraft.util.EnumChatFormatting;

public class Utils 
{
	public static String formatChristmasColors(String s)
	{
		String[] s1 = s.split("");
		String s2 = "";
		String red = EnumChatFormatting.DARK_RED.toString();
		String green = EnumChatFormatting.DARK_GREEN.toString();
		boolean flash = true;
		boolean b = Calendar.getInstance().get(Calendar.SECOND) % 2 == 0;
		String color = b ? red : green;
		/*for (int i = 0; i < s1.length; i++)
		{
			: true;
			String color = i % 2 == 0 ? (b ? red : green) : (b ? green : red);
			s2 = s2 + color + s1[i];
		}*/
		s2 = color + s;
		return s2;
	}
}
