package so101.throwablebricks; //The package your mod is in

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet23VehicleSpawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BaseMod;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import so101.throwablebricks.CommonProxy;
import so101.throwablebricks.ConfigThrowableBricks;
import so101.throwablebricks.DispenserBehaviorBrick;
import so101.throwablebricks.DispenserBehaviorBrickNether;
import so101.throwablebricks.DispenserBehaviorBrickBomb;
import so101.throwablebricks.EntityBrick;
import so101.throwablebricks.EntityBrickBomb;
import so101.throwablebricks.EntityBrickNether;
import so101.throwablebricks.ItemBrick;
import so101.throwablebricks.ItemBrickChunk;
import so101.throwablebricks.ClientPacketHandler;

@NetworkMod(clientSideRequired=true,serverSideRequired=false, 
clientPacketHandlerSpec = @SidedPacketHandler(channels = {"ThrowableBricks" }, packetHandler = ClientPacketHandler.class), 
serverPacketHandlerSpec = @SidedPacketHandler(channels = {}, packetHandler = ServerPacketHandler.class)) 
@Mod(modid="Bricks",name="Throwable Bricks Mod",version="2.3.5.2")

public class Main
{
	public MinecraftServer mcServer;
	public World worldObj;
	
	
	public static final Item Brick = new ItemBrick(80, 0).setUnlocalizedName("brick");
	public static final Item BrickNether = new ItemBrick(149, 1).setUnlocalizedName("netherbrick");
	public static Item BrickBomb;
	public static Item BrickChunk;
	
	@PreInit()
	public void PreInit(FMLPreInitializationEvent e)
	{
		
		ConfigThrowableBricks cc = new ConfigThrowableBricks();

		ConfigThrowableBricks.loadConfig(e);
		
		EntityRegistry.registerModEntity(EntityBrick.class, "Brick", cc.BrickID, this, 80, 2, true);
		LanguageRegistry.instance().addStringLocalization("entity.Bricks.Brick.name", "Brick");
		
		EntityRegistry.registerModEntity(EntityBrickNether.class, "NetherBrick", cc.BrickNetherID, this, 80, 2, true);
		LanguageRegistry.instance().addStringLocalization("entity.Bricks.NetherBrick.name", "Nether Brick");
		
		EntityRegistry.registerModEntity(EntityBrickBomb.class, "BrickBomb", cc.BrickBombID, this, 80, 2, true);
		LanguageRegistry.instance().addStringLocalization("entity.Bricks.BrickBomb.name", "TNT Brick");
		
		LanguageRegistry.instance().addStringLocalization("death.attack.brick", "%1$s was bricked to death");
		LanguageRegistry.instance().addStringLocalization("death.attack.brick.player", "%1$s was bricked to death by %2$s");
		LanguageRegistry.instance().addStringLocalization("death.attack.brick.item", "%1$s was bricked to death by %2$s using %3$s");
		
		BrickChunk = new ItemBrickChunk(cc.itemBrickChunkID);
		BrickBomb = new ItemBrick(cc.itemBrickBombID, 2).setUnlocalizedName("Bricks:brickTNT");
		//LanguageRegistry.instance().addNameForObject(new ItemStack(this.BrickChunk, 1, 0),"item.ThrowableBricksMod.BrickChunk.name" , "Brick Chunk");
		
		 BlockDispenser.dispenseBehaviorRegistry.putObject(Brick, new DispenserBehaviorBrick());
		 BlockDispenser.dispenseBehaviorRegistry.putObject(BrickNether, new DispenserBehaviorBrickNether());
		 BlockDispenser.dispenseBehaviorRegistry.putObject(BrickBomb, new DispenserBehaviorBrickBomb());
	}
	
	@Instance("Bricks") //The instance, this is very important later on
	public static Main instance = new Main();

	@SidedProxy(clientSide = "so101.throwablebricks.ClientProxy", serverSide = "so101.throwablebricks.CommonProxy") 
	public static CommonProxy proxy;

	@Init
	public void Init(FMLInitializationEvent event)
	{ 
		NetworkRegistry.instance().registerGuiHandler(this, proxy); 
		proxy.registerRenderInformation();
		proxy.addNames();
		proxy.addRecipes();
	}
}