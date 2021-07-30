package easton.bigbeacons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientBigBeacons implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
       // ((RegisterInvoker) BlockEntityRenderDispatcher.INSTANCE).invokeRegister(BigBeacons.BEACON_ENTITY, new BeaconEntityRenderer(BlockEntityRenderDispatcher.INSTANCE));
        BlockEntityRendererRegistry.INSTANCE.register(BigBeacons.BEACON_ENTITY, BeaconEntityRenderer::new);

        ScreenRegistry.register(BigBeacons.BEACON_HANDLER, BeaconScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(new Identifier("bigbeacons", "s2c-query"), (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                ClientPlayNetworking.send(new Identifier("bigbeacons", "c2s-acknowledge"), PacketByteBufs.empty());
            });
        });
    }
}