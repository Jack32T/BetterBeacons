package easton.bigbeacons.mixin;

import easton.bigbeacons.BeaconEntity;
import easton.bigbeacons.BigBeacons;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.model.BlockStateSupplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlock.class)
public abstract class BeaconBlockMixin extends BlockWithEntity {

    protected BeaconBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("RETURN"), method = "createBlockEntity", cancellable = true)
    public void properEntity(BlockPos pos, BlockState state, CallbackInfoReturnable<BlockEntity> info) {
        info.setReturnValue(new BeaconEntity(pos, state));
    }

    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    public void properUsage(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        if (world.isClient) {
            info.setReturnValue(ActionResult.SUCCESS);
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BeaconEntity) {
                player.openHandledScreen((BeaconEntity)blockEntity);
                player.incrementStat(Stats.INTERACT_WITH_BEACON);
            }

            info.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlaced", cancellable = true)
    public void properPlacement(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo info) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BeaconEntity) {
                ((BeaconEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }
        info.cancel();
    }

    @Inject(at = @At("RETURN"), method = "getTicker", cancellable = true)
    public void bigBeaconTicker(World world, BlockState state, BlockEntityType<BeaconEntity> type, CallbackInfoReturnable<BlockEntityTicker<BeaconEntity>> info) {
        info.setReturnValue(checkType(type, BigBeacons.BEACON_ENTITY, BeaconEntity::tick));
    }

}