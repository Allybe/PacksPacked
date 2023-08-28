package me.allybe.packspacked.client;

import me.allybe.packspacked.SavedPackProfile;
import me.allybe.packspacked.client.gui.LoadScreen;
import me.allybe.packspacked.client.gui.SaveScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.text.Text;

import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class PackspackedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

    }

    public static void showLoadScreen(ResourcePackOrganizer resourcePackOrganizer) {
        MinecraftClient.getInstance().setScreen(new LoadScreen(MinecraftClient.getInstance().currentScreen, Text.of("Load Packs"), resourcePackOrganizer));
    }

    public static void showSaveScreen(ResourcePackOrganizer resourcePackOrganizer) {
        MinecraftClient.getInstance().setScreen(new SaveScreen(MinecraftClient.getInstance().currentScreen, Text.of("Save Packs"), resourcePackOrganizer));
    }
}
