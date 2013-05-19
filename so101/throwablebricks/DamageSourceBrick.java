package so101.throwablebricks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.StatCollector;

public class DamageSourceBrick extends DamageSource
{
	
	protected DamageSourceBrick(String par1Str) 
	{
		super(par1Str);
	}
	
	
	/**
	 *  Returns the Damage type of a brick
	 * */
	public static DamageSource causeBrickDamage(Entity par0Entity, Entity par1Entity)
	{
		return (new EntityDamageSourceIndirect("brick", par0Entity, par1Entity)).setProjectile();
	}
	
	@Override
	public String getDeathMessage(EntityLiving par1EntityLiving)
    {
        EntityLiving entityliving1 = par1EntityLiving.func_94060_bK();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entityliving1 != null && StatCollector.func_94522_b(s1) ? StatCollector.translateToLocalFormatted(s1, new Object[] {par1EntityLiving.getTranslatedEntityName(), entityliving1.getTranslatedEntityName()}): StatCollector.translateToLocalFormatted(s, new Object[] {par1EntityLiving.getTranslatedEntityName()});
    }
	
	/**
     * Returns the message to be displayed on player death.
     */
    /*public String getDeathMessage(EntityLiving par1EntityLiving)
    {
        EntityLiving entityliving1 = par1EntityLiving.func_94060_bK();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entityliving1 != null && StatCollector.func_94522_b(s1) ? StatCollector.translateToLocalFormatted(s1, new Object[] {par1EntityLiving.func_96090_ax(), entityliving1.func_96090_ax()}): StatCollector.translateToLocalFormatted(s, new Object[] {par1EntityLiving.func_96090_ax()});
    }*/
}
