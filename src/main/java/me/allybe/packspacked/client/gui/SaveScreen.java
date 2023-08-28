package me.allybe.packspacked.client.gui;

import me.allybe.packspacked.SavedPackProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SaveScreen extends Screen {
    private final Screen parent;
    private final Text title;
    private final ResourcePackOrganizer resourcePackOrganizer;
    private TextFieldWidget saveNameField;
    private ButtonWidget saveButton;
    private Text saveNameText;
    private SavedPackProfile PackSaver;
    private ButtonWidget cancelButton;

    public SaveScreen(Screen parent, Text title, ResourcePackOrganizer resourcePackOrganizer) {
        super(title);
        this.parent = parent;
        this.title = title;
        this.resourcePackOrganizer = resourcePackOrganizer;
    }

    public void tick() {
        this.saveNameField.tick();
    }

    protected void init() {
        Text cancelButtonText = Text.of("Cancel");
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(cancelButtonText, button -> returnToParent()).dimensions(this.width / 2 - 154, this.height - 48, 150, 20).build());

        Text saveButtonText = Text.of("Save");
        this.saveButton = this.addDrawableChild(ButtonWidget.builder(saveButtonText, button -> savePacks()).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());

        Text saveNameText = Text.of("Save Name");
        this.saveNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 60, 200, 20, saveNameText);
        this.saveNameField.setChangedListener((saveName) -> {
            this.saveNameText = Text.of(saveName);
            this.saveButton.active = !saveName.isEmpty();
        });
        this.addSelectableChild(this.saveNameField);

        this.setInitialFocus(this.saveNameField);
    }

    public void savePacks() {
        this.PackSaver = new SavedPackProfile();
        this.resourcePackOrganizer.getEnabledPacks().forEach((pack) -> {
            this.PackSaver.addPack(pack);
        });
        this.PackSaver.setPackedName(this.saveNameText.getString());
        this.PackSaver.savePackProfileToFile();
        returnToParent();
    }

    public void returnToParent() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);

        this.saveNameField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

}


