package ru.starfarm.client.api.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RealmId {

    private final String name;
    private final int id;

}
