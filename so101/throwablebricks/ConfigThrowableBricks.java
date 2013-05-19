package so101.throwablebricks;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigThrowableBricks 
{

	//Items
	public static int itemBrickChunkID;
	public static int itemBrickWithNoteID;
	public static int itemBrickBombID;
	
	public static int BrickID;
	public static int BrickNetherID;
	public static int BrickBombID;
	
	public static void loadConfig(FMLPreInitializationEvent e)
	{
		Configuration config = new Configuration(e.getSuggestedConfigurationFile()); //Gets the file
	
		config.load(); //Loads it
		
		/** Items **/
		
		Property brickChunk; //This is a property, see below
		brickChunk = config.getItem("Brick Chunk", 15000); //This gets the property
		brickChunk.comment = "The Brick Chunk Item ID"; //This adds a comment
		itemBrickChunkID = brickChunk.getInt(); //This gets the value
		
		Property noteBrick; //This is a property, see below
		noteBrick = config.getItem("Brick With Note", 15001); //This gets the property
		noteBrick.comment = "The Note Brick Item ID"; //This adds a comment
		itemBrickWithNoteID = noteBrick.getInt(); //This gets the value
		
		Property bombBrick; //This is a property, see below
		bombBrick = config.getItem("Bomb Brick", 15001); //This gets the property
		bombBrick.comment = "The Brick Bomb Item ID"; //This adds a comment
		itemBrickBombID = bombBrick.getInt(); //This gets the value
		
		
		/** Entities **/
		
		Property entityBrickID; //This is a property, see below
		entityBrickID = config.getItem("Brick Entity ID", 162); //This gets the property
		entityBrickID.comment = "The ID the thrown bricks have"; //This adds a comment
		BrickID = entityBrickID.getInt(); //This gets the value
		
		Property entityBrickNetherID; //This is a property, see below
		entityBrickNetherID = config.getItem("Nether Brick Entity ID", 163); //This gets the property
		entityBrickNetherID.comment = "The ID the thrown nether bricks have"; //This adds a comment
		BrickNetherID = entityBrickNetherID.getInt(); //This gets the value
		
		Property entityBrickBombID; //This is a property, see below
		entityBrickBombID = config.getItem("TNT Brick Entity ID", 164); //This gets the property
		entityBrickBombID.comment = "The ID the thrown tnt bricks have"; //This adds a comment
		BrickBombID = entityBrickBombID.getInt(); //This gets the value
	
		//General
		config.save(); //Saves the file
	
	}

}