package me.allybe.packspacked.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;

public interface LoadButtonEvent {

    Event<LoadButtonEvent> EVENT = EventFactory.createArrayBacked(LoadButtonEvent.class, (listeners) -> (resourcePackOrganizer) -> {
        for (LoadButtonEvent listener : listeners) {
            listener.onLoadButton(resourcePackOrganizer);
        }
    });

    void onLoadButton(ResourcePackOrganizer resourcePackOrganizer);
}
