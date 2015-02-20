package so101.bricks;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigBricks extends Configuration
{
	/**Whether TNT Bricks are allowed to be in game*/
	public static boolean enableTNTBricks;
	/***/
	public static boolean enableTerrainDamage;
	public static boolean enableIngotThrows;
	public static boolean enableNetherbrickFire;
	public static boolean enableTooltipRightClickToThrow;
	public static boolean enableBrickCannon;
	public static boolean enableSmashDrops;	
	public static boolean enableBrickCustomDrops;
	public static boolean enableBlockToBrickRecipe;
	
	public static boolean brickCannonsIngots;
	public static boolean brickCannonsGunpowder;
	
	public static int brickID;
	
	public ConfigBricks(File file) 
	{
		super(file);
	}
	
	@Override
	public void load()
	{
		super.load();
		enableTNTBricks = this.getBoolean("EnableExplosiveBricks", "General", true, "Whether or not TNT Bricks are enabled in game or not. "
				+ "WARNING: Disabling this after it has been enabled will remove all TNT Bricks on load. This will not remove custom exploding bricks though.");
		enableTerrainDamage = this.getBoolean("TerrainDamage", "General", true, "Whether Explosive Bricks damage terrain or not.");
		enableIngotThrows = this.getBoolean("AllowThrownIngots", "General", true, "Whether players can throw any ingot normally. If disabled, custom bricks can still br thrown.");
		enableNetherbrickFire = this.getBoolean("NetherBrickFire", "General", true, "Set this to false if nether bricks shouldn't place fire when they land.");
		enableSmashDrops = this.getBoolean("BrickBreakBlocks", "General", true, "If bricks drop items when they smash (brick chunks, nuggets, etc).");
		enableBrickCustomDrops = this.getBoolean("BrickBlockCustomDrops", "General", false, "Whether brick blocks (and nether brick blocks) should drop brick items instead of blocks now.");
		enableTooltipRightClickToThrow = this.getBoolean("RightClickThrowTooltip", "General", true, "If the tooltip found on bricks or ingots is enabled or not (\"Right click to throw\").");
		enableBrickCannon = this.getBoolean("EnableBrickCannon", "General", true, "If the brick cannon is enabled or not. WARNING: Disabling this after it being enabled will remove all brick cannons on world load!");
		brickID = this.getInt("BrickEntityId", "Entity", 162, 0, 256, "The entity ID used by bricks.");
		brickCannonsGunpowder = this.getBoolean("CannonGunpowderUse", "General", true, "Whether gunpowder is required to fire brick cannons.");
		brickCannonsIngots = this.getBoolean("CannonIngotUse", "General", true, "Whether brick cannons can fire ingots out of them.");
		enableBlockToBrickRecipe = this.getBoolean("CustomBrickRecipes", "General", true, "If the recipe that converts brick blocks back into bricks is enabled.");
		
		ThrowableBricksMod.DEBUG = this.getBoolean("Debug", "Debug", false, "Debug mode for bricks mod. Keep off unless you know what you are doing.");
	}
}
