package planeat.dto.invite;

import lombok.*;
import planeat.domain.InviteParticipant;
import planeat.domain.StepKeyword;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteResponseRequest {
    private String name;
    private List<StepKeyword> preferences;
    private double latitude;
    private double longitude;

    public InviteParticipant toDomain() {
        return InviteParticipant.builder()
                .name(name)
                .preferences(preferences)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
