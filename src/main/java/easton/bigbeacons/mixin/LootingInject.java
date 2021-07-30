package easton.bigbeacons.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootingEnchantLootFunction.class)
abstract class LootingInject {
    @Final
    @Shadow private LootNumberProvider countRange;

    @Shadow private boolean hasLimit() {return true;}

    @Final
    @Shadow private int limit;

    @Inject(method = "process(Lnet/minecraft/item/ItemStack;Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/item/ItemStack;", cancellable = true, at = @At(value = "HEAD"))
    private void inject(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> info) {
        Entity entity2 = (Entity)context.get(LootContextParameters.KILLER_ENTITY);
        if (entity2 instanceof LivingEntity) {
            int r = EnchantmentHelper.getLooting((LivingEntity)entity2);
            if (((LivingEntity) entity2).hasStatusEffect(StatusEffects.LUCK)) {
                r = r + ((LivingEntity) entity2).getStatusEffect(StatusEffects.LUCK).getAmplifier() + 1;
            }
            //r = r + (((LivingEntity) entity2).hasStatusEffect(StatusEffects.LUCK) ? ((LivingEntity) entity2).getStatusEffect(StatusEffects.LUCK).getAmplifier() + 1 : 0);
            if (r == 0) {
                info.setReturnValue(stack);
            }

            float q = (float)r * this.countRange.nextFloat(context);
            stack.increment(Math.round(q));
            if (this.hasLimit() && stack.getCount() > this.limit) {
                stack.setCount(this.limit);
            }
        }
        info.setReturnValue(stack);

    }
}