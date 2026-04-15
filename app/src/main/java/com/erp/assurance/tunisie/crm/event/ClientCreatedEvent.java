package com.erp.assurance.tunisie.crm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class ClientCreatedEvent extends ApplicationEvent {
    private final UUID clientId;
    private final String clientName;

    public ClientCreatedEvent(Object source, UUID clientId, String clientName) {
        super(source);
        this.clientId = clientId;
        this.clientName = clientName;
    }
}
