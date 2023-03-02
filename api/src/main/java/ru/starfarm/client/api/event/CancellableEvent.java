package ru.starfarm.client.api.event;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CancellableEvent {

    protected boolean cancelled;

}
