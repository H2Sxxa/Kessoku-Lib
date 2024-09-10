package band.kessoku.lib.events.entity.mixin;

import java.util.List;

import band.kessoku.lib.events.entity.api.*;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin extends LivingEntityMixin {
    @Shadow
    public abstract ServerWorld getServerWorld();

    /**
     * Minecraft by default does not call Entity#onKilledOther for a ServerPlayerEntity being killed.
     * This is a Mojang bug.
     * This is implements the method call on the server player entity and then calls the corresponding event.
     */
    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getPrimeAdversary()Lnet/minecraft/entity/LivingEntity;"))
    private void callOnKillForPlayer(DamageSource source, CallbackInfo ci) {
        final Entity attacker = source.getAttacker();

        // If the damage source that killed the player was an entity, then fire the event.
        if (attacker != null) {
            attacker.onKilledOther(this.getServerWorld(), (ServerPlayerEntity) (Object) this);
            ServerEntityCombatEvent.AFTER_KILLED_OTHER_ENTITY.invoker().afterKilledOtherEntity(this.getServerWorld(), attacker, (ServerPlayerEntity) (Object) this);
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void notifyDeath(DamageSource source, CallbackInfo ci) {
        ServerLivingEntityEvent.AFTER_DEATH.invoker().afterDeath((ServerPlayerEntity) (Object) this, source);
    }

    /**
     * This is called by {@code teleportTo}.
     */
    @Inject(method = "worldChanged(Lnet/minecraft/server/world/ServerWorld;)V", at = @At("TAIL"))
    private void afterWorldChanged(ServerWorld origin, CallbackInfo ci) {
        ServerEntityWorldChangeEvent.AFTER_PLAYER_CHANGE_WORLD.invoker().afterChangeWorld((ServerPlayerEntity) (Object) this, origin, this.getServerWorld());
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEvent.COPY_FROM.invoker().copyFromPlayer(oldPlayer, (ServerPlayerEntity) (Object) this, alive);
    }

    @Redirect(method = "lambda$startSleepInBed$13", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"))
    private Comparable<?> redirectSleepDirection(BlockState state, Property<?> property, BlockPos pos) {
        Direction initial = state.contains(property) ? (Direction) state.get(property) : null;
        return EntitySleepEvent.MODIFY_SLEEPING_DIRECTION.invoker().modifySleepDirection((LivingEntity) (Object) this, pos, initial);
    }

    @Inject(method = "lambda$startSleepInBed$13", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", shift = At.Shift.BY, by = 3), cancellable = true)
    private void onTrySleepDirectionCheck(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> info, @Local @Nullable Direction sleepingDirection) {
        // This checks the result from the event call above.
        if (sleepingDirection == null) {
            info.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE));
        }
    }

    @Redirect(method = "lambda$startSleepInBed$13", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"))
    private void onSetSpawnPoint(ServerPlayerEntity player, RegistryKey<World> dimension, BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage) {
        if (EntitySleepEvent.ALLOW_SETTING_SPAWN.invoker().allowSettingSpawn(player, pos)) {
            player.setSpawnPoint(dimension, pos, angle, spawnPointSet, sendMessage);
        }
    }

    @Redirect(method = "lambda$startSleepInBed$13", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", remap = false))
    private boolean hasNoMonstersNearby(List<HostileEntity> monsters, BlockPos pos) {
        boolean vanillaResult = monsters.isEmpty();
        ActionResult result = EntitySleepEvent.ALLOW_NEARBY_MONSTERS.invoker().allowNearbyMonsters((PlayerEntity) (Object) this, pos, vanillaResult);
        return result != ActionResult.PASS ? result.isAccepted() : vanillaResult;
    }

    @Redirect(method = "lambda$startSleepInBed$13", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
    private boolean redirectDaySleepCheck(World world, BlockPos pos) {
        boolean day = world.isDay();
        ActionResult result = EntitySleepEvent.ALLOW_SLEEP_TIME.invoker().allowSleepTime((PlayerEntity) (Object) this, pos, !day);

        if (result != ActionResult.PASS) {
            return !result.isAccepted(); // true from the event = night-like conditions, so we have to invert
        }

        return day;
    }
}