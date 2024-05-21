package pl.rsww.offerwrite.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractDomainEvent {
    private UUID entityId;
}
