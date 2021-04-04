package github.pitbox46.thievingslimes;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void handleItemSteal(MessageItemSteal msg){
        Minecraft mc = Minecraft.getInstance();
        ClientWorld w = mc.world;
        if (w != null) {
            Entity victim = w.getEntityByID(msg.victimEntityId);
            Entity thief = w.getEntityByID(msg.thiefEntityId);
            if (thief != null && victim instanceof LivingEntity) {
                thief.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent((slimeAbsorption) -> {
                    slimeAbsorption.setAbsorbedStack(msg.stolenStack);
                });
            }
        }
    }
}
