package com.beanfeed.tdsword.screen;

import com.beanfeed.tdsword.TransDimensionalSword;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TDSscreen extends HandledScreen<TDSscreenhandler> {
     private static final Identifier MAIN_TEXTURE =
            new Identifier(TransDimensionalSword.MODID, "textures/gui/container/tdsmenu.png");
    private static final Identifier ACTIVATE_TEXTURE =
            new Identifier(TransDimensionalSword.MODID, "textures/gui/container/tdsactivate.png");
    private final boolean isActivated;
    public TDSscreen(TDSscreenhandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.isActivated = handler.isActivated;
        this.backgroundHeight = 175;
        this.titleY = 5;
        this.playerInventoryTitleY = 83;
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if(isActivated) RenderSystem.setShaderTexture(0, MAIN_TEXTURE);
        else RenderSystem.setShaderTexture(0, ACTIVATE_TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int titleWidth = this.textRenderer.getWidth(title.getString());
        Text nTitle = Text.translatable("menu.title.tdsword.alttdsmenu");
        if(!isActivated) {
            titleWidth = this.textRenderer.getWidth(nTitle.getString());
        }
        //TransDimensionalSword.LOGGER.info(String.valueOf(width));
        this.titleX = (backgroundWidth - titleWidth) / 2;
        if(!isActivated) this.textRenderer.draw(matrices, nTitle, (float)this.titleX, (float)this.titleY, 4210752);
        else this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
    }
}
