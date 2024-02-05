package ru.dragonestia.picker.util;

import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.repository.details.NodeDetails;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.details.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Component
public class DetailsParser {

    public Set<NodeDetails> parseNodeDetails(String detailsSeq) {
        var details = new HashSet<NodeDetails>();
        for (var detailStr: detailsSeq.split(",")) {
            try {
                details.add(NodeDetails.valueOf(detailStr.toUpperCase()));
            } catch (IllegalArgumentException ignore) {}
        }
        return details;
    }

    public Set<RoomDetails> parseRoomDetails(String detailsSeq) {
        var details = new HashSet<RoomDetails>();
        for (var detailStr: detailsSeq.split(",")) {
            try {
                details.add(RoomDetails.valueOf(detailStr.toUpperCase()));
            } catch (IllegalArgumentException ignore) {}
        }
        return details;
    }

    public Set<UserDetails> parseUserDetails(String detailsSeq) {
        var details = new HashSet<UserDetails>();
        for (var detailStr: detailsSeq.split(",")) {
            try {
                details.add(UserDetails.valueOf(detailStr.toUpperCase()));
            } catch (IllegalArgumentException ignore) {}
        }
        return details;
    }
}
