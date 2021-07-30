package easton.bigbeacons.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void sendModCheckPacket(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        ServerPlayNetworking.send(player, new Identifier("bigbeacons", "s2c-query"), PacketByteBufs.empty());
    }

    @Inject(method = "respawnPlayer", at = @At("TAIL"))
    private void sendModCheckPacketOnRespawn(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayNetworking.send(cir.getReturnValue(), new Identifier("bigbeacons", "s2c-query"), PacketByteBufs.empty());
    }

}