package github.pitbox46.thievingslimes;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlers {

    @SubscribeEvent
    public void onEntityDamaged(LivingDamageEvent damageEvent) {
        Entity entity = damageEvent.getEntity();
        DamageSource source = damageEvent.getSource();
        if(entity instanceof PlayerEntity && source.getTrueSource() != null && source.getTrueSource().getClass() == SlimeEntity.class) {
            if(entity.getEntityWorld().getRandom().nextInt(10) != 0) return;
            SlimeEntity slime = (SlimeEntity) source.getTrueSource();
            if(slime.getHeldItemMainhand() != ItemStack.EMPTY) return;
            PlayerEntity player = (PlayerEntity) entity;
            int selectedSlot = entity.getEntityWorld().getRandom().nextInt(6);

            ItemStack itemStack;
            if(selectedSlot <= 3) {
                itemStack = ((NonNullList<ItemStack>) player.getArmorInventoryList()).get(selectedSlot);
                player.setItemStackToSlot(EquipmentSlotType.fromSlotTypeAndIndex(EquipmentSlotType.Group.ARMOR, selectedSlot), ItemStack.EMPTY);
            }
            else if(selectedSlot == 4) {
                itemStack = player.getHeldItemMainhand();
                player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
            else {
                itemStack = player.getHeldItemOffhand();
                player.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
            }
            slime.setHeldItem(Hand.MAIN_HAND, itemStack);
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent) {
        if(deathEvent.getEntityLiving().getClass() == SlimeEntity.class) {
            SlimeEntity slime = (SlimeEntity) deathEvent.getEntityLiving();
            ItemStack itemStack = slime.getHeldItemMainhand();
            if(itemStack != ItemStack.EMPTY) {
                slime.getEntityWorld().addEntity(new ItemEntity(slime.getEntityWorld(), slime.getPosX(), slime.getPosY(), slime.getPosZ(), itemStack));
            }
        }
    }
}
