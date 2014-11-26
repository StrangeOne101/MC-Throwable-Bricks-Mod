package so101.bricks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceBricks extends DamageSource
{

	public DamageSourceBricks(String par1String) 
	{
		super(par1String);
	}
	
	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_)
    {
        EntityLivingBase entitylivingbase1 = p_151519_1_.func_94060_bK();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase1 != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {p_151519_1_.func_145748_c_(), entitylivingbase1.func_145748_c_()}): new ChatComponentTranslation(s, new Object[] {p_151519_1_.func_145748_c_()});
    }
	
	public static DamageSource causeBrickDamage(Entity par1Entity, Entity par2Entity, DeathType deathType)
	{
		return (new EntityDamageSourceIndirect("brick."+deathType.toString().toLowerCase(), par1Entity, par2Entity)).setProjectile();
	}
	
	public static DamageSource causeBrickDamage(Entity par1Entity, Entity par2Entity)
	{
		return (new EntityDamageSourceIndirect("brick.player", par1Entity, par2Entity)).setProjectile();
	}
	
	enum DeathType
	{
		SELF, PLAYER, SQUASHED, GENERAL
	}

}
