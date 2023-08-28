package me.allybe.packspacked.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import java.util.Objects;

@Environment(value= EnvType.CLIENT)
public class PackedListWidget
        extends AlwaysSelectedEntryListWidget<PackedListWidget.PackedListEntry> {
    static final Identifier RESOURCE_PACKS_TEXTURE = new Identifier("textures/gui/resource_packs.png");
    static final Text INCOMPATIBLE = Text.translatable("pack.incompatible");
    static final Text INCOMPATIBLE_CONFIRM = Text.translatable("pack.incompatible.confirm.title");
    private final Text title;
    private final Screen screen;

    public PackedListWidget(MinecraftClient client, Screen screen, int width, int height, Text title) {
        super(client, width, height, 32, height - 55 + 4, 36);
        this.screen = screen;
        this.title = title;
        this.centerListVertically = false;
        Objects.requireNonNull(client.textRenderer);
        this.setRenderHeader(true, (int)(9.0f * 1.5f));
    }

    @Override
    protected void renderHeader(MatrixStack matrices, int x, int y, Tessellator tessellator) {
        MutableText text = Text.empty().append(this.title).formatted(Formatting.UNDERLINE, Formatting.BOLD);
        this.client.textRenderer.draw(matrices, text, (float)(x + this.width / 2 - this.client.textRenderer.getWidth(text) / 2), (float)Math.min(this.top + 3, y), 0xFFFFFF);
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    @Override
    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    @Environment(value=EnvType.CLIENT)
    public static class PackedListEntry
            extends AlwaysSelectedEntryListWidget.Entry<PackedListEntry> {
        private final PackedListWidget widget;
        protected final Screen screen;
        private final Text displayName;
        private final Text description;
        public PackedListEntry(Text name, Text description, PackedListWidget widget, Screen screen) {
            this.screen = screen;
            this.widget = widget;
            this.displayName = name;
            this.description = description;
        }

        private static OrderedText trimTextToWidth(MinecraftClient client, Text text) {
            int i = client.textRenderer.getWidth(text);
            if (i > 157) {
                StringVisitable stringVisitable = StringVisitable.concat(client.textRenderer.trimToWidth(text, 157 - client.textRenderer.getWidth(ELLIPSIS)), StringVisitable.plain(ELLIPSIS));
                return Language.getInstance().reorder(stringVisitable);
            }
            return text.asOrderedText();
        }

        private static MultilineText createMultilineText(MinecraftClient client, Text text) {
            return MultilineText.create(client.textRenderer, (StringVisitable)text, 157, 2);
        }

        @Override
        public Text getNarration() {
            return Text.translatable("narrator.select", this.pack.getDisplayName());
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, this.pack.getIconId());
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            DrawableHelper.drawTexture(matrices, x, y, 0.0f, 0.0f, 32, 32, 32, 32);
            OrderedText orderedText = (OrderedText) this.displayName;
            MultilineText multilineText = (MultilineText) this.description;
            if (this.isSelectable() && (this.client.options.getTouchscreen().getValue().booleanValue() || hovered)) {
                RenderSystem.setShaderTexture(0, RESOURCE_PACKS_TEXTURE);
                DrawableHelper.fill(matrices, x, y, x + 32, y + 32, -1601138544);
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                int i = mouseX - x;
                int j = mouseY - y;
                if (this.pack.canBeEnabled()) {
                    if (i < 32) {
                        DrawableHelper.drawTexture(matrices, x, y, 0.0f, 32.0f, 32, 32, 256, 256);
                    } else {
                        DrawableHelper.drawTexture(matrices, x, y, 0.0f, 0.0f, 32, 32, 256, 256);
                    }
                } else {
                    if (this.pack.canBeDisabled()) {
                        if (i < 16) {
                            DrawableHelper.drawTexture(matrices, x, y, 32.0f, 32.0f, 32, 32, 256, 256);
                        } else {
                            DrawableHelper.drawTexture(matrices, x, y, 32.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveTowardStart()) {
                        if (i < 32 && i > 16 && j < 16) {
                            DrawableHelper.drawTexture(matrices, x, y, 96.0f, 32.0f, 32, 32, 256, 256);
                        } else {
                            DrawableHelper.drawTexture(matrices, x, y, 96.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveTowardEnd()) {
                        if (i < 32 && i > 16 && j > 16) {
                            DrawableHelper.drawTexture(matrices, x, y, 64.0f, 32.0f, 32, 32, 256, 256);
                        } else {
                            DrawableHelper.drawTexture(matrices, x, y, 64.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                }
            }
            this.client.textRenderer.drawWithShadow(matrices, orderedText, (float)(x + 32 + 2), (float)(y + 1), 0xFFFFFF);
            multilineText.drawWithShadow(matrices, x + 32 + 2, y + 12, 10, 0x808080);
        }

        private boolean isSelectable() {
            return !this.pack.isPinned() || !this.pack.isAlwaysEnabled();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double d = mouseX - (double)this.widget.getRowLeft();
            double e = mouseY - (double)this.widget.getRowTop(this.widget.children().indexOf(this));
            if (this.isSelectable() && d <= 32.0) {
                if (this.pack.canBeEnabled()) {
                    this.client.setScreen(new ConfirmScreen(confirmed -> {
                        this.client.setScreen(this.screen);
                        if (confirmed) {
                            this.pack.enable();
                        }
                    }, INCOMPATIBLE_CONFIRM, text));
                    return true;
                }
                if (d < 16.0 && this.pack.canBeDisabled()) {
                    this.pack.disable();
                    return true;
                }
                if (d > 16.0 && e < 16.0 && this.pack.canMoveTowardStart()) {
                    this.pack.moveTowardStart();
                    return true;
                }
                if (d > 16.0 && e > 16.0 && this.pack.canMoveTowardEnd()) {
                    this.pack.moveTowardEnd();
                    return true;
                }
            }
            return false;
        }
    }
}

