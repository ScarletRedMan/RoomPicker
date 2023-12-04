package ru.dragonestia.loadbalancer.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;

import java.net.URI;

@Getter
public class Bucket {

    private final String identifier;
    private final String nodeIdentifier;
    private final SlotLimit slots;
    private final String payload;
    private boolean locked = false;

    @JsonCreator
    private Bucket(@JsonProperty("identifier") String identifier,
                   @JsonProperty("nodeIdentifier") String nodeIdentifier,
                   @JsonProperty("slots") SlotLimit slots,
                   @JsonProperty("payload") String payload,
                   @JsonProperty("locked") boolean locked) {
        this.identifier = identifier;
        this.nodeIdentifier = nodeIdentifier;
        this.slots = slots;
        this.payload = payload;
        this.locked = locked;
    }

    public static Bucket create(String identifier, Node node, SlotLimit limit, String payload) {
        return new Bucket(identifier, node.identifier(), limit, payload, false);
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    public boolean isAvailable(int usedSlots, int requiredSlots) {
        if (locked) return false;
        if (slots.isUnlimited()) return true;
        return slots.slots() >= usedSlots + requiredSlots;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Bucket other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }

    public URI createApiURI() {
        return URI.create("/nodes/" + nodeIdentifier + "/buckets/" + identifier);
    }

    public String getUsingPercentage(int used) {
        if (getSlots().isUnlimited()) return "0%";
        double percent = used / (double) getSlots().slots() * 100;
        return ((int) percent) + "%";
    }
}
