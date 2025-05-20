package planeat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutePreferenceRequest {
    private List<StepPreference> steps;
    private double latitude;
    private double longitude;
}
