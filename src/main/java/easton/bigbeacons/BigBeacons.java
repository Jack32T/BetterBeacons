package easton.bigbeacons;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class BigBeacons implements ModInitializer {

	//public static final Beacon BIG_BEACON;
	//public static final BlockItem BEACON_ITEM;
	public static BlockEntityType<BeaconEntity> BEACON_ENTITY;
	public static final ScreenHandlerType<BeaconHandler> BEACON_HANDLER;

	public static final StatusEffect FLIGHT = new FlightEffect();

	public static final String MOD_ID = "bigbeacons";
	public static final Identifier BIGBEACON = new Identifier(MOD_ID, "beacon");

	static {
		BEACON_HANDLER = ScreenHandlerRegistry.registerSimple(BIGBEACON, BeaconHandler::new);
		//BIG_BEACON = Registry.register(Registry.BLOCK, BIGBEACON, new Beacon(FabricBlockSettings.of(Material.GLASS, MaterialColor.DIAMOND).hardness(3.0f).luminance(15).solidBlock(BigBeacons::never)));
		//BEACON_ITEM = Registry.register(Registry.ITEM, BIGBEACON, new BlockItem(BIG_BEACON, new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.RARE)));
		BEACON_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, BIGBEACON, FabricBlockEntityTypeBuilder.create(BeaconEntity::new, Blocks.BEACON).build());
		//BlockRenderLayerMap.INSTANCE.putBlock(BigBeacons.BIG_BEACON, RenderLayer.getCutout());
	}

	@Override
	public void onInitialize() {
		Registry.register(Registry.STATUS_EFFECT, new Identifier("bigbeacons", "flight"), FLIGHT);

		ServerPlayNetworking.registerGlobalReceiver(new Identifier("bigbeacons", "c2s-acknowledge"), (server, player, handler, buf, responseSender) -> {
			((PlayerModdedDuck)player).setHasMod(true);
		});
		//funi time
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager.literal("test")
					.then(CommandManager.literal("c").executes(ctx -> {
				ServerPlayerEntity playerEntity = ctx.getSource().getPlayer();
				playerEntity.changeGameMode(GameMode.CREATIVE);

				return 1;
			}))
					.then(CommandManager.literal("s").executes(ctx -> {
						ServerPlayerEntity playerEntity = ctx.getSource().getPlayer();
						playerEntity.changeGameMode(GameMode.SURVIVAL);

						return 1;
					}))
			);

		}));
	}
}
