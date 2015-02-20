package so101.bricks;

import java.util.Calendar;

import so101.bricks.client.ParticleDustManager;
import so101.bricks.item.ItemBrickChunks;
import so101.bricks.item.ItemCannon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = ThrowableBricksMod.modID, name = "Throwable Bricks Mod", version = ThrowableBricksMod.version)
public class ThrowableBricksMod 
{
	@SidedProxy(clientSide = "so101.bricks.client.ClientProxy", serverSide = "so101.bricks.CommonProxy")
	public static CommonProxy proxy;

	//public static String CHANNEL = "BRICKENTUPDATE";
	
	public static final String modID = "ThrowableBricks";
	public static final String version = "3.0";
	
	public static final Item brick = Items.brick;
	public static final Item brickNether = Items.netherbrick;
	public static final Item brickTNT = new Item().setUnlocalizedName("brickTNT").setTextureName(modID+":brickTNT").setCreativeTab(CreativeTabs.tabMaterials);
	public static final Item brickChunks = new ItemBrickChunks().setUnlocalizedName("brickChunks");
	public static final Item brickCannon = new ItemCannon();
	
	public static EasterEggDate DATE_THANKSGIVING = EasterEggDate.THANKSGIVING;
	public static EasterEggDate DATE_CHRISTMAS = EasterEggDate.CHRISTMAS;
	
	public static boolean DEBUG = false;
	
 	public enum EasterEggDate
	{
		THANKSGIVING(27, 10),
		CHRISTMAS(25, 11);
		
		public int MONTH;
		public int DAY;
		
		EasterEggDate(int day, int month)
		{
			this.MONTH = month;
			this.DAY = day;
		}
	}
	
	public ConfigBricks config;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		config = new ConfigBricks(event.getSuggestedConfigurationFile());
		config.load();
		config.save();
		
		if (ConfigBricks.enableTNTBricks)
		{
			GameRegistry.registerItem(brickTNT, "explosivebrick", modID);
			OreDictionary.registerOre("brickTNT", brickTNT);
			OreDictionary.registerOre("itemBrick", brickTNT);
			GameRegistry.addShapelessRecipe(new ItemStack(brickTNT), new Object[] {Items.brick, Blocks.tnt});
		}
		if (ConfigBricks.enableBrickCannon)
		{
			GameRegistry.registerItem(brickCannon, "brickcannon");
			GameRegistry.addRecipe(new ItemStack(brickCannon, 1), new Object[] {"BI ", "BBI", " SB", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('S'), Items.stick, Character.valueOf('B'), Blocks.brick_block});
			GameRegistry.addRecipe(new ItemStack(brickCannon, 1), new Object[] {"BI ", "BBI", " SB", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('S'), Items.stick, Character.valueOf('B'), Blocks.nether_brick});
		}
		
		GameRegistry.registerItem(brickChunks, "brickchunks", modID);	
		
		proxy.registerRendering();
		
		EntityRegistry.registerModEntity(EntityBrick.class, "Brick", ConfigBricks.brickID, this, 80, 2, true);
		EntityList.addMapping(EntityBrick.class, "Brick", ConfigBricks.brickID);
		
		OreDictionary.registerOre("brickNormal", brick);
		OreDictionary.registerOre("brickNether", brickNether);
		
		
		OreDictionary.registerOre("itemBrick", brick);
		OreDictionary.registerOre("itemBrick", brickNether);
		
		
		GameRegistry.addRecipe(new ItemStack(brick, 1), new Object[] {"XX", "XX", Character.valueOf('X'), new ItemStack(brickChunks, 1, 0)});
		GameRegistry.addRecipe(new ItemStack(brickNether, 1), new Object[] {"XX", "XX", Character.valueOf('X'), new ItemStack(brickChunks, 1, 1)});
		
		GameRegistry.addRecipe(new ItemStack(brick, 4), new Object[] {"X", Character.valueOf('X'), Blocks.brick_block});
		GameRegistry.addRecipe(new ItemStack(brickNether, 4), new Object[] {"X", Character.valueOf('X'), Blocks.nether_brick});
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		new ParticleDustManager();
		
	}
	
	@SubscribeEvent
	/**Adds custom drops to brick blocks*/
	public void onBlockBreak(BreakEvent e)
	{
		if (e.getPlayer() != null && e.getPlayer().getHeldItem() != null && ConfigBricks.enableBrickCustomDrops)
		{
			if (!e.block.equals(Blocks.brick_block) && !e.block.equals(Blocks.nether_brick))
			{
				return;
			}
			float f = 0.7F;
			double d0 = (double)(e.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(e.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(e.world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			EntityItem eitem = new EntityItem(e.world, e.x + d0, e.y + d1, e.z + d2);			
            
			//if (ForgeHooks.canToolHarvestBlock(e.block, e.blockMetadata, e.getPlayer().getHeldItem()))
			if (e.getPlayer().getHeldItem().getItem() instanceof ItemPickaxe)
			{
				if (e.block.equals(Blocks.brick_block))
				{
					e.setCanceled(true);
					e.world.setBlockToAir(e.x, e.y, e.z);
					eitem.setEntityItemStack(new ItemStack(Items.brick, 3 + e.world.rand.nextInt(2)));
				}
				else if (e.block.equals(Blocks.nether_brick))
				{
					e.setCanceled(true);
					e.world.setBlockToAir(e.x, e.y, e.z);
					eitem.setEntityItemStack(new ItemStack(Items.netherbrick, 3 + e.world.rand.nextInt(2)));
				}
				else
				{
					return;
				}
			}

			if (eitem.getEntityItem() != null)
			{
				e.world.spawnEntityInWorld(eitem);
			}
			
		}
	}
	
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		BlockDispenser.dispenseBehaviorRegistry.putObject(brick, new BehaviorBrickDispense());
		BlockDispenser.dispenseBehaviorRegistry.putObject(brickNether, new BehaviorBrickDispense());
		
		if (ConfigBricks.enableTNTBricks)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(brickTNT, new BehaviorBrickDispense());
		}
		if (ConfigBricks.enableIngotThrows)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(Items.gold_ingot, new BehaviorBrickDispense());
			BlockDispenser.dispenseBehaviorRegistry.putObject(Items.iron_ingot, new BehaviorBrickDispense());
		}
	}
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) 
	{
		if (event.entityPlayer.inventory == null || event.entityPlayer.inventory.mainInventory == null)
		{
			return;
		}
		if (canThrow(event.itemStack) && ConfigBricks.enableTooltipRightClickToThrow && !(event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().getBoolean("NoTooltip"))) 
		{ 
			String s = "Right click to throw!"; 
			if (isDay(DATE_CHRISTMAS))
			{
				s = Utils.formatChristmasColors(s);
			}
			event.toolTip.add(EnumChatFormatting.GRAY.toString() + s);
			
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
			if ((event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().hasKey("ExplosionRadius")) || event.itemStack.getItem().equals(brickTNT))
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
		else if (event.itemStack.getItem().equals(brickCannon))
		{
			ItemStack item = null;
			boolean gunpowder = false;
			for (int i = 0; i < event.entityPlayer.inventory.mainInventory.length; i++)
			{
				if (ThrowableBricksMod.canThrow(event.entityPlayer.inventory.mainInventory[i]) && item == null)
				{
					item = event.entityPlayer.inventory.mainInventory[i];
					//break;
				}
				else if (event.entityPlayer.inventory.mainInventory[i] != null && event.entityPlayer.inventory.mainInventory[i].getItem() == Items.gunpowder)
				{
					gunpowder = true;
				}
				if (item != null && gunpowder)
				{
					break;
				}
			}
			if (!gunpowder && ConfigBricks.brickCannonsGunpowder && !event.entityPlayer.capabilities.isCreativeMode)
			{
				event.toolTip.add(EnumChatFormatting.GRAY.toString() + "No Gunpowder to Fire!");
			}
			else if (item != null)
			{
				event.toolTip.add(EnumChatFormatting.GRAY.toString() + "Firing: " + item.getDisplayName());
			}
			else
			{
				event.toolTip.add(EnumChatFormatting.GRAY.toString() + "No Item to Fire!");
			}
		}
	}
	
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent e)
	{
		if (e.isCanceled())
		{
			//e.entityPlayer.addChatComponentMessage(new ChatComponentText("CANCELED"));
			return;
		}
		
		if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR )
		{
			this.throwBrickEvent(e.entityPlayer);
		}
		else if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.entityPlayer.getHeldItem() != null && canThrow(e.entityPlayer.getHeldItem()))
		{
			Block block = e.world.getBlock(e.x, e.y, e.z);
			if (!(block instanceof ITileEntityProvider || block instanceof BlockWorkbench) )
			{
				this.throwBrickEvent(e.entityPlayer);
			} 
		}
	}
	
	@SubscribeEvent
	public void onEntityRightClick(EntityInteractEvent e)
	{
		//if (
				this.throwBrickEvent(e.entityPlayer);//)
			//{
			//	e.setCanceled(true);
			//}
		
		
	}
	
	/** Trys to throw a brick
	 * */
	public void throwBrickEvent(EntityPlayer player)
	{
		if (player.getHeldItem() != null)
		{
			//String item = Item.itemRegistry.getNameForObject(player.getHeldItem().getItem());
			ItemStack iitem = player.getHeldItem();
			
			if (canThrow(iitem))
			{
				//EnumBricks type = item.equals("brick") ? EnumBricks.REGULAR : (item.equals("netherbrick") ? EnumBricks.NETHER : EnumBricks.UNKNOWN);
				
				EntityBrick brick = new EntityBrick(player.worldObj, player, player.getHeldItem().copy(), true);
				boolean flag1 = player.getHeldItem().getItem().equals(brickTNT) || (player.getHeldItem().hasTagCompound() && player.getHeldItem().getTagCompound().hasKey("ExplosionRadius"));
				brick.shouldDrop = !player.capabilities.isCreativeMode;
				brick.explosionSize = flag1 ? 4 : 0;
				brick.normalPlayerThrow = true;
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
				//return true;
			}
		}
		//return false;
	}
	
	public static boolean canThrow(ItemStack item)
	{
		if (item == null)
		{
			return false;
		}
		boolean flag1 = item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemArmor;
		boolean flag2 = item.getUnlocalizedName().toLowerCase().contains("brick") && !item.getUnlocalizedName().toLowerCase().contains("chunk") && !item.getUnlocalizedName().toLowerCase().contains("cannon");
		boolean flag3 = item.getUnlocalizedName().toLowerCase().contains("ingot") && ConfigBricks.enableIngotThrows;
		boolean flag4 = item.hasTagCompound() && item.getTagCompound().getBoolean("Throwable");
		@SuppressWarnings("deprecation")
		boolean flag5 = OreDictionary.getOreName(OreDictionary.getOreID(item)).startsWith("ingot") && EnumBricks.ingotExceptionsList.contains(OreDictionary.getOreName(OreDictionary.getOreID(item)).replaceFirst("ingot", "")) && ConfigBricks.enableIngotThrows;
		boolean flag6 = EnumBricks.ingotExceptionsExclude.contains(item.getUnlocalizedName());
		
		if (!flag1 && (flag2 || flag3 || flag4 || flag5) && !flag6)
		{
			return true;
		}
		return false;
		/*if (((item.getUnlocalizedName().toLowerCase().contains("brick") && !item.getUnlocalizedName().toLowerCase().contains("chunk") && !(item.getItem() instanceof ItemBlock)) || item.getUnlocalizedName().toLowerCase().contains("ingot")|| (item.hasTagCompound() && item.getTagCompound().getBoolean("Throwable") || EnumBricks.ingotExceptions.contains(OreDictionary.getOreID(item)))) && !(item.getItem() instanceof ItemBlock))
		{
			return true;
		}
		return false;*/
	}
	
	public static boolean isDay(EasterEggDate date)
	{
		return Calendar.getInstance().get(Calendar.MONTH) == date.MONTH && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == date.DAY;
	}
}
