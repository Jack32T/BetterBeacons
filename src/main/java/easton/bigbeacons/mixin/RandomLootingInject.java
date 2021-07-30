package easton.bigbeacons.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomChanceWithLootingLootCondition.class)
abstract class RandomLootingInject {

    @Final
    @Shadow
    private float chance;

    @Final
    @Shadow
    private float lootingMultiplier;

    @Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", cancellable = true, at = @At(value = "HEAD"))
    private void inject(LootContext lootContext, CallbackInfoReturnable<Boolean> info) {
        Entity entity2 = (Entity)lootContext.get(LootContextParameters.KILLER_ENTITY);
        int j = 0;
        if (entity2 instanceof LivingEntity) {
            j = EnchantmentHelper.getLooting((LivingEntity)entity2);
            if (((LivingEntity) entity2).hasStatusEffect(StatusEffects.LUCK)) {
                j = j + ((LivingEntity) entity2).getStatusEffect(StatusEffects.LUCK).getAmplifier() + 1;
            }

        }

        info.setReturnValue(lootContext.getRandom().nextFloat() < this.chance + (float)j * this.lootingMultiplier);
    }
}