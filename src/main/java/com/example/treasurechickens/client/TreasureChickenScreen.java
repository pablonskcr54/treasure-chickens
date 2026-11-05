package com.example.treasurechickens.client;

import com.example.treasurechickens.entity.TreasureChickenEntity;
import com.example.treasurechickens.network.NetworkHandler;
import com.example.treasurechickens.network.UpgradeTreasurePayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Стильный экран прокачки курицы: тёмная панель с цветной рамкой под тип
 * курицы, иконка предмета и полоска-индикатор уровня.
 */
public class TreasureChickenScreen extends Screen {

    private static final int PANEL_W = 250;
    private static final int PANEL_H = 180;

    private final TreasureChickenEntity chicken;
    private final int accentColor; // цвет ресурса курицы для рамки

    public TreasureChickenScreen(TreasureChickenEntity chicken) {
        super(Component.translatable("screen.treasure_chickens.title"));
        this.chicken = chicken;
        this.accentColor = accentFromItem(chicken);
    }

    private int accentFromItem(TreasureChickenEntity chicken) {
        var type = chicken.getType();
        if (type == com.example.treasurechickens.init.ModEntities.GOLD_CHICKEN.get()) return 0xFFE8C848;
        if (type == com.example.treasurechickens.init.ModEntities.EMERALD_CHICKEN.get()) return 0xFF3FBF5A;
        if (type == com.example.treasurechickens.init.ModEntities.DIAMOND_CHICKEN.get()) return 0xFF48D6D8;
        if (type == com.example.treasurechickens.init.ModEntities.IRON_CHICKEN.get()) return 0xFFC9C9C9;
        if (type == com.example.treasurechickens.init.ModEntities.NETHERITE_CHICKEN.get()) return 0xFF5C463D;
        if (type == com.example.treasurechickens.init.ModEntities.PRISMARINE_CHICKEN.get()) return 0xFF56A8A0;
        if (type == com.example.treasurechickens.init.ModEntities.SLIME_CHICKEN.get()) return 0xFF7ED34A;
        if (type == com.example.treasurechickens.init.ModEntities.ENDER_PEARL_CHICKEN.get()) return 0xFF28B39A;
        if (type == com.example.treasurechickens.init.ModEntities.XP_CHICKEN.get()) return 0xFFA8E04A;
        if (type == com.example.treasurechickens.init.ModEntities.QUARTZ_CHICKEN.get()) return 0xFFE8E0CC;
        return 0xFF8A8A8A;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void addLabel(String key, int x, int y, int color, Object... format) {
        this.addRenderableWidget(new StringWidget(x, y, PANEL_W - 40, 9,
                Component.translatable(key, format), this.font)
                .setColor(color).alignCenter());
    }

    @Override
    protected void init() {
        int left = (this.width - PANEL_W) / 2;
        int top = (this.height - PANEL_H) / 2;
        int cx = left + 15;
        int y = top + 46;

        // Уровень / max
        this.addLabel("screen.treasure_chickens.level", cx, y, 0xFFFFFF,
                chicken.getLevel(), TreasureChickenEntity.MAX_LEVEL);
        y += 15;

        // Что несёт
        String dropName = chicken.getDef().drop == null
                ? Component.translatable("screen.treasure_chickens.xp").getString()
                : chicken.getDef().drop.getDescription().getString();
        this.addLabel("screen.treasure_chickens.current", cx, y, 0xE6FFFFFF, dropName);
        y += 13;

        this.addLabel("screen.treasure_chickens.drop_current", cx, y, 0xE6FFFFFF,
                chicken.getDef().drop == null ? "—" : chicken.effectiveMaxDrop() + "");
        y += 13;

        this.addLabel("screen.treasure_chickens.interval_current", cx, y, 0xE6FFFFFF,
                chicken.effectiveEggTime() / 20);
        y += 18;

        if (chicken.isMaxLevel()) {
            this.addLabel("screen.treasure_chickens.max", cx, y, 0xFF55FF55);
        } else {
            this.addLabel("screen.treasure_chickens.cost", cx, y, 0xFFFFFF,
                    chicken.upgradeCost(), dropName);
            y += 13;
            int nextLevel = chicken.getLevel() + 1;
            this.addLabel("screen.treasure_chickens.next", cx, y, 0xFFA5FF00,
                    chicken.getDef().drop == null ? 0 : chicken.maxDropAtLevel(nextLevel),
                    chicken.eggTimeAtLevel(nextLevel) / 20);
            y += 13;
            // Подсказка о способности, которая откроется впереди
            if (nextLevel == 5) {
                this.addLabel("screen.treasure_chickens.ability_fast", cx, y, 0xFF55FF55);
            } else if (nextLevel == TreasureChickenEntity.MAX_LEVEL) {
                this.addLabel("screen.treasure_chickens.ability_double", cx, y, 0xFF55FF55);
            }
        }

        this.addRenderableWidget(Button.builder(
                        Component.translatable("button.treasure_chickens.upgrade"),
                        btn -> {
                            NetworkHandler.sendToServer(new UpgradeTreasurePayload(this.chicken.getId()));
                        })
                .bounds(left + (PANEL_W - 120) / 2, top + PANEL_H - 32, 120, 20)
                .build());
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Не вызываем super.renderBackground(): оно размывает весь экран (blur-эффект),
        // из-за чего плашка выглядит «мутной». Рисуем только сплошное затемнение.
        graphics.fill(0, 0, this.width, this.height, 0xB0000000);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        int left = (this.width - PANEL_W) / 2;
        int top = (this.height - PANEL_H) / 2;

        // Тень
        graphics.fill(left + 5, top + 5, left + PANEL_W + 5, top + PANEL_H + 5, 0x99000000);
        // Основная панель — вертикальный градиент: светлее у верха, темнее у низа
        graphics.fillGradient(left, top, left + PANEL_W, top + PANEL_H, 0xFF23272E, 0xFF121419);
        // Цветная рамка-акцент по всему периметру
        graphics.fill(left, top, left + PANEL_W, top + 2, accentColor);
        graphics.fill(left, top + PANEL_H - 2, left + PANEL_W, top + PANEL_H, accentColor);
        graphics.fill(left, top, left + 2, top + PANEL_H, accentColor);
        graphics.fill(left + PANEL_W - 2, top, left + PANEL_W, top + PANEL_H, accentColor);
        // Внутренняя тонкая светлая линия
        graphics.fill(left + 3, top + 3, left + PANEL_W - 3, top + 4, 0x66FFFFFF);
        // Лёгкое свечение рамки ближе к углам заголовка
        graphics.fill(left + 2, top + 2, left + PANEL_W - 2, top + 3, 0xAAFFFFFF);

        // Заголовок
        int cx = this.width / 2;
        graphics.drawCenteredString(this.font,
                Component.translatable("screen.treasure_chickens.title"),
                cx, top + 12, 0xFFFFFFFF);

        // Иконка предмета (что держит курица)
        ItemStack icon = chicken.getDef().drop == null
                ? new ItemStack(net.minecraft.world.item.Items.EXPERIENCE_BOTTLE)
                : new ItemStack(chicken.getDef().drop);
        graphics.renderItem(icon, cx - 6, top + 22);

        // Полоска-индикатор уровня
        int barW = PANEL_W - 40;
        int barX = left + 20;
        int barY = top + 40;
        graphics.fill(barX, barY, barX + barW, barY + 3, 0xFF444444);
        int filled = (int) (barW * (double) chicken.getLevel() / TreasureChickenEntity.MAX_LEVEL);
        graphics.fill(barX, barY, barX + filled, barY + 3, accentColor);

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}