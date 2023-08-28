package me.allybe.packspacked.mixin;

import me.allybe.packspacked.client.events.LoadButtonEvent;
import me.allybe.packspacked.client.events.SaveButtonEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public abstract class AddButtonsMixin extends Screen {

    @Shadow @Final private ResourcePackOrganizer organizer;

    protected AddButtonsMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addSaveButton(CallbackInfo info) {
        Text text = Text.of("Save Packs");
        this.addDrawableChild(ButtonWidget.builder(text, button -> SaveButtonEvent.EVENT.invoker().onSaveButton(this.organizer)).dimensions(this.width / 2 + 4, this.height - 20, 150, 20).build());
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addLoadButton(CallbackInfo info) {
        Text text = Text.of("Load Packs");
        this.addDrawableChild(ButtonWidget.builder(text, button -> LoadButtonEvent.EVENT.invoker().onLoadButton(this.organizer)).dimensions(this.width / 2 - 154, this.height - 20, 150, 20).build());
    }

}
