package planeat.domain;

import lombok.Builder;
import lombok.Getter;
import planeat.dto.preference.StepPreference;

import java.util.List;

@Getter
@Builder
public class InviteParticipant {
    private final String name;
    private final List<StepPreference> preferences;
    private final double latitude;
    private final double longitude;
}
