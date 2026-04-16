package com.erp.assurance.tunisie.crm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class ClientUpdatedEvent extends ApplicationEvent {
    private final UUID clientId;

    public ClientUpdatedEvent(Object source, UUID clientId) {
        super(source);
        this.clientId = clientId;
    }
}
