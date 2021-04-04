package github.pitbox46.thievingslimes;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("thievingslimes:main"),
            () -> "3.1.0", "3.1.0"::equals, "3.1.0"::equals);
    private static int ID = 0;

    public PacketHandler() {
    }

    public static void init() {
        CHANNEL.registerMessage(
                ID++,
                MessageItemSteal.class,
                (msg, pb) -> {
                    pb.writeVarInt(msg.victimEntityId);
                    pb.writeVarInt(msg.thiefEntityId);
                    pb.writeItemStack(msg.stolenStack);
                },
                pb -> new MessageItemSteal(pb.readVarInt(), pb.readVarInt(), pb.readItemStack()),
                (msg, ctx) -> {
                    (ctx.get()).enqueueWork(() -> ThievingSlimes.PROXY.handleItemSteal(msg));
                    (ctx.get()).setPacketHandled(true);
                });
    }
}