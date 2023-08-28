package me.allybe.packspacked;

import me.allybe.packspacked.client.PackspackedClient;
import me.allybe.packspacked.client.events.LoadButtonEvent;
import me.allybe.packspacked.client.events.SaveButtonEvent;
import net.fabricmc.api.ModInitializer;

public class Packspacked implements ModInitializer {
    @Override
    public void onInitialize() {
        SaveButtonEvent.EVENT.register((resourcePackOrganizer) -> {
            PackspackedClient.showSaveScreen(resourcePackOrganizer);
        });
        LoadButtonEvent.EVENT.register((resourcePackOrganizer) -> {
            PackspackedClient.showLoadScreen(resourcePackOrganizer);
        });
    }
}
