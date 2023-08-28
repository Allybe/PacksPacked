package me.allybe.packspacked;

import com.google.common.collect.ImmutableList;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SavedPackProfile {
    private String packedName;
    private List<String> packs = new ArrayList<String>();

    public void addPack(ResourcePackOrganizer.Pack pack) {
        if (pack.canBeDisabled()) {
            packs.add(pack.getDisplayName().getString());
        }
    }

    public void setPackedName(String packedName) {
        this.packedName = packedName;
    }

    public void savePackProfileToFile() {
        if (packs.isEmpty()) {
            return;
        }

        Path gamePath = FabricLoader.getInstance().getGameDir();
        Path packspackedPath = Path.of(gamePath.toString() + "/data/" + packedName + ".txt");

        ImmutableList<String> reversedPacks = ImmutableList.copyOf(packs).reverse();
        String packsString = String.join(",", reversedPacks);

        if (new File(packspackedPath.toString()).exists()) {
            return;
        }

        try {
            Files.writeString(packspackedPath, packsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
