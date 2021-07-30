package easton.bigbeacons.mixin;

import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBlockEntity.class)
public interface BeaconAdvancementMixin {
    @Accessor("level")
    public void setLevel(int level);
}