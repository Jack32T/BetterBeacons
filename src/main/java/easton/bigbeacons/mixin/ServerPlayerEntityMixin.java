package easton.bigbeacons.mixin;

import easton.bigbeacons.PlayerModdedDuck;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerModdedDuck {

    boolean hasMod = false;

    @Override
    public boolean hasMod() {
        return this.hasMod;
    }

    @Override
    public void setHasMod(boolean modded) {
        this.hasMod = modded;
    }
}
