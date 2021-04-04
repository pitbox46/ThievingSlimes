package github.pitbox46.thievingslimes;

import net.minecraft.item.ItemStack;

public class MessageItemSteal {
    public final int victimEntityId;
    public final int thiefEntityId;
    public final ItemStack stolenStack;

    public MessageItemSteal(int victimEntityId, int thiefEntityId, ItemStack stolenStack) {
        this.victimEntityId = victimEntityId;
        this.thiefEntityId = thiefEntityId;
        this.stolenStack = stolenStack;
    }
}
