package com.example.treasurechickens.network;

import com.example.treasurechickens.TreasureChickens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpgradeTreasurePayload(int entityId) implements CustomPacketPayload {
    public static final Type<UpgradeTreasurePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "upgrade_treasure"));

    public static final StreamCodec<FriendlyByteBuf, UpgradeTreasurePayload> STREAM_CODEC =
            StreamCodec.ofMember(UpgradeTreasurePayload::write, UpgradeTreasurePayload::read);

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    private static UpgradeTreasurePayload read(FriendlyByteBuf buf) {
        return new UpgradeTreasurePayload(buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}