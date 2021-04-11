package github.pitbox46.thievingslimes;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class EventHandlers {

    @SubscribeEvent
    public void onEntityDamaged(LivingDamageEvent damageEvent) {
        Entity entity = damageEvent.getEntity();
        Entity source = damageEvent.getSource().getTrueSource();
        if(entity instanceof PlayerEntity && source != null) {
            source.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
                if(entity.getEntityWorld().getRandom().nextInt(10) != 0) return;
                if(slimeAbsorption.getAbsorbedStack() != ItemStack.EMPTY) return;
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
                slimeAbsorption.setAbsorbedStack(itemStack);
                PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> damageEvent.getSource().getTrueSource()),
                        new MessageItemSteal(player.getEntityId(), source.getEntityId(), itemStack.copy()));
            });
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent updateEvent) {
        LivingEntity entity = updateEvent.getEntityLiving();
        entity.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
            if(entity.getRNG().nextInt(20) == 0) slimeAbsorption.getAbsorbedStack().damageItem(1, entity, e -> {});
        });
    }
}
