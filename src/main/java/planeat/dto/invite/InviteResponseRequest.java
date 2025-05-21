package planeat.dto.invite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import planeat.domain.InviteParticipant;
import planeat.dto.preference.StepPreference;

import java.util.List;

@Getter
@Builder
public class InviteResponseRequest {
    private final String name;
    private final List<StepPreference> steps;
    private final double latitude;
    private final double longitude;

    public InviteParticipant toDomain() {
        return InviteParticipant.builder()
                .name(name)
                .preferences(steps)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
