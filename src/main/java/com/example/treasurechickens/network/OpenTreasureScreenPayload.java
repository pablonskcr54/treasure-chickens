package com.example.treasurechickens.network;

import com.example.treasurechickens.TreasureChickens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenTreasureScreenPayload(int entityId, int level) implements CustomPacketPayload {
    public static final Type<OpenTreasureScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "open_treasure_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenTreasureScreenPayload> STREAM_CODEC =
            StreamCodec.ofMember(OpenTreasureScreenPayload::write, OpenTreasureScreenPayload::read);

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(level);
    }

    private static OpenTreasureScreenPayload read(FriendlyByteBuf buf) {
        return new OpenTreasureScreenPayload(buf.readInt(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}