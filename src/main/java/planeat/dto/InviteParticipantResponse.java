package planeat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InviteParticipantResponse {
    private String name;
    private List<StepPreference> preferences;
    private double latitude;
    private double longitude;
}
