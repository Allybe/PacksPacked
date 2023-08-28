package me.allybe.packspacked.client.gui;

import me.allybe.packspacked.client.ResourcePackLoader;
import me.allybe.packspacked.client.gui.widget.PackedListWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class LoadScreen extends Screen {

    private Screen parent;
    private Text title;
    private ResourcePackOrganizer resourcePackOrganizer;
    private PackedListWidget packedListWidget;
    public LoadScreen(Screen parent, Text title, ResourcePackOrganizer resourcePackOrganizer) {
        super(title);
        this.resourcePackOrganizer = resourcePackOrganizer;
        this.parent = parent;
        this.title = title;
    }

    protected void init() {
        Text FoundPacksText = Text.of("Found Packs");
        this.packedListWidget = new PackedListWidget(this.client, this ,this.width, this.height, FoundPacksText);
        this.addSelectableChild(this.packedListWidget);

        Text cancelButtonText = Text.of("Cancel");
        this.addDrawableChild(ButtonWidget.builder(cancelButtonText, button -> returnToParent()).dimensions(this.width / 2 - 154, this.height - 48, 150, 20).build());

        Text loadButtonText = Text.of("Load");
        this.addDrawableChild(ButtonWidget.builder(loadButtonText, button -> loadPacks()).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    public void loadPacks() {
        Path path = Path.of("C:\\Users\\systh\\Codespaces\\Personal Projects\\Minecraft Mods\\PacksPacked\\run\\data\\save.txt");
        ResourcePackLoader loader = new ResourcePackLoader(path, this.resourcePackOrganizer);
        loader.loadPacks();
        returnToParent();
    }

     private void updatePacks() {
        packedListWidget.children().clear();
        packedListWidget.setSelected((PackedListWidget.PackedListEntry) null);

        List<PackedPacks> packedPacks = getPackedPacks();
        packedPacks.forEach((pack) -> {
            packedListWidget.children().add(new PackedListWidget.PackedListEntry(pack.name, pack.description, this.packedListWidget, MinecraftClient.getInstance().currentScreen));
         });

    }

    public List<PackedPacks> getPackedPacks() {
        @Nullable
        List<PackedPacks> finalFiles = Collections.emptyList();

        Path gamePath = FabricLoader.getInstance().getGameDir();
        File directoryPath = new File(gamePath.toString() + "\\data\\");

        File filesList[] = directoryPath.listFiles();
        for(File file : filesList) {
            if (!file.isFile() || !file.getName().endsWith(".txt")) {
                continue;
            }   
            finalFiles.add(new PackedPacks(file.getName(), "Description"));
        }
        return finalFiles;
    }

    public void returnToParent() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    public class PackedPacks {
        public Text name;
        public Text description;

        PackedPacks(String name, String description) {
            this.name = Text.of(name);
            this.description = Text.of(description);
        }
    }
}
