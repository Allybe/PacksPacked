package me.allybe.packspacked.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ResourcePackLoader {

    private static final Path PACKS_DIR = FabricLoader.getInstance().getConfigDir();
    private String resourcePacksString;
    private ResourcePackOrganizer resourcePackOrganizer;

    public ResourcePackLoader(Path SavePath, ResourcePackOrganizer resourcePackOrganizer) {
        this.resourcePackOrganizer = resourcePackOrganizer;
        try {
            resourcePacksString = Files.readString(SavePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearPacks() {
        resourcePackOrganizer.getEnabledPacks().forEach(pack -> {
            System.out.println("Removing pack: " + pack.getDisplayName().getString());
            if (pack != null) {
                return;
            }
            if (pack.canBeDisabled()) {
                pack.disable();
            }
        });
        
    }

    public void loadPacks() {
        clearPacks();
        Collection<String> savedResources = Arrays.stream(resourcePacksString.split(",")).toList();
        List<ResourcePackOrganizer.Pack> DisabledPackList = resourcePackOrganizer.getDisabledPacks().toList();

        DisabledPackList.forEach(pack -> {
            try {
                if (savedResources.contains(pack.getDisplayName().getString())) {
                    System.out.println("Adding pack: " + pack.getDisplayName().getString());
                    pack.enable();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }
}
