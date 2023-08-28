package me.allybe.packspacked.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;

public interface SaveButtonEvent {
    Event<SaveButtonEvent> EVENT = EventFactory.createArrayBacked(SaveButtonEvent.class, (listeners) -> (ResourcePackOrganizer resourcePackOrganizer) -> {
        for (SaveButtonEvent listener : listeners) {
            listener.onSaveButton(resourcePackOrganizer);
        }
    });

    void onSaveButton(ResourcePackOrganizer resourcePackOrganizer);
}

