package so101.bricks;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = ThrowableBricksMod.modID, name = "Throwable Bricks Mod", version = ThrowableBricksMod.version)
public class ThrowableBricksMod 
{
	@SidedProxy(clientSide = "so101.bricks.ClientProxy", serverSide = "so101.bricks.CommonProxy")
	public static CommonProxy proxy;

	public static String CHANNEL = "BRICKENTUPDATE";
	
	public static final String modID = "throwablebricks";
	public static final String version = "2.7";
	
	public static final Item brick = Items.brick;
	public static final Item brickNether = Items.netherbrick;
	public static final Item brickTNT = new Item().setUnlocalizedName("brickTNT").setTextureName(modID+":brickTNT");
	public static final Item brickChunks = new ItemBrickChunks().setUnlocalizedName("brickChunks");

	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		GameRegistry.registerItem(brickTNT, "explosivebrick", modID);
		GameRegistry.registerItem(brickChunks, "brickchunks", modID);	
		
		proxy.registerRendering();
		
		EntityRegistry.registerModEntity(EntityBrick.class, "Brick", 162, this, 80, 2, true);
		EntityList.addMapping(EntityBrick.class, "Brick", 162);
		
		OreDictionary.registerOre("brickNormal", brick);
		OreDictionary.registerOre("brickNether", brickNether);
		OreDictionary.registerOre("brickTNT", brickTNT);
		
		OreDictionary.registerOre("itemBrick", brick);
		OreDictionary.registerOre("itemBrick", brickNether);
		OreDictionary.registerOre("itemBrick", brickTNT);
		
		GameRegistry.addRecipe(new ItemStack(brick, 1), new Object[] {"XX", "XX", Character.valueOf('X'), new ItemStack(brickChunks, 1, 0)});
		GameRegistry.addRecipe(new ItemStack(brickNether, 1), new Object[] {"XX", "XX", Character.valueOf('X'), new ItemStack(brickChunks, 1, 1)});
		GameRegistry.addShapelessRecipe(new ItemStack(brickTNT), new Object[] {brick, Block.getBlockFromName("tnt")});
		
		MinecraftForge.EVENT_BUS.register(this);
		
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{

	}
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) 
	{
		if (this.canThrow(event.itemStack) && !(event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().getBoolean("NoTooltip"))) 
		{ 
			event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Right click to throw!");
			
			boolean flag1 = false;
			if (event.itemStack.hasTagCompound())
			{
				NBTTagCompound nbt = event.itemStack.getTagCompound();
				boolean flag = nbt.hasKey("Damage") || nbt.hasKey("Scale") || nbt.hasKey("ExplosionRadius");
				flag1 = flag;
				if (flag)
				{
					event.toolTip.add("");
				}
				if (nbt.hasKey("Damage"))
				{
					event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Damage: " + nbt.getFloat("Damage"));
				}
				if (nbt.hasKey("Scale"))
				{
					event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Scale: " + nbt.getFloat("Scale"));
				}
			}
			if ((event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().hasKey("ExplosionSize")) || event.itemStack.getItem().equals(brickTNT))
			{
				if (!flag1)
				{
					event.toolTip.add("");
				}
				if (event.showAdvancedItemTooltips)
				{
					event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Explosion Radius: " + (event.itemStack.hasTagCompound() ? event.itemStack.getTagCompound().getInteger("ExplosionRadius") : 4));
				}
				else
				{
					event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Explodes on Impact");
				}					
			}
		}
	}
	
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			this.throwBrickEvent(e.entityPlayer);
		}
	}
	
	@SubscribeEvent
	public void onEntityRightClick(EntityInteractEvent e)
	{
		this.throwBrickEvent(e.entityPlayer);
	}
	
	/** Trys to throw a brick
	 * */
	public void throwBrickEvent(EntityPlayer player)
	{
		if (player.getHeldItem() != null)
		{
			//String item = Item.itemRegistry.getNameForObject(player.getHeldItem().getItem());
			ItemStack iitem = player.getHeldItem();
			
			if (this.canThrow(iitem))
			{
				//EnumBricks type = item.equals("brick") ? EnumBricks.REGULAR : (item.equals("netherbrick") ? EnumBricks.NETHER : EnumBricks.UNKNOWN);
				
					EntityBrick brick = new EntityBrick(player.worldObj, player, player.getHeldItem().copy());
					boolean flag1 = player.getHeldItem().getItem().equals(brickTNT) || (player.getHeldItem().hasTagCompound() && player.getHeldItem().getTagCompound().hasKey("ExplosionSize"));
					brick.shouldDrop = !player.capabilities.isCreativeMode;
					brick.explosionSize = flag1 ? 4 : 0;
					if (player.getHeldItem().hasDisplayName())
					{
						brick.getEntityData().setString("CustomName", player.getHeldItem().getDisplayName());
					}
					if (player.getHeldItem().getTagCompound() != null)
					{
						if (player.getHeldItem().getTagCompound().hasKey("Damage"))
						{
							brick.damage = player.getHeldItem().getTagCompound().getFloat("Damage");
						}
						if (player.getHeldItem().getTagCompound().hasKey("ExplosionRadius"))
						{
							brick.explosionSize = player.getHeldItem().getTagCompound().getByte("ExplosionRadius");
						}
						if (player.getHeldItem().getTagCompound().hasKey("ShouldDrop"))
						{
							brick.shouldDrop = player.getHeldItem().getTagCompound().getBoolean("ShouldDrop");
						}
					}
					if (!player.worldObj.isRemote)
					{
						player.worldObj.spawnEntityInWorld(brick);
					}	
					if (!player.capabilities.isCreativeMode)
					{
						player.getHeldItem().stackSize = player.getHeldItem().stackSize - 1;
						//player.getHeldItem().stackSize--;
					}
				}
			}
	}
	
	public boolean canThrow(ItemStack item)
	{
		if (((item.getUnlocalizedName().toLowerCase().contains("brick") && !item.getUnlocalizedName().toLowerCase().contains("chunk") && !(item.getItem() instanceof ItemBlock)) || item.getUnlocalizedName().toLowerCase().contains("ingot")|| (item.hasTagCompound() && item.getTagCompound().getBoolean("Throwable"))) && !(item.getItem() instanceof ItemBlock))
		{
			return true;
		}
		return false;
	}
}
