package github.pitbox46.thievingslimes;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ThievingSlimes.MOD_ID)
public class ThievingSlimes {
    public static final String MOD_ID = "thievingslimes";
    private static final Logger LOGGER = LogManager.getLogger();

    public static CommonProxy PROXY;

    public ThievingSlimes() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
