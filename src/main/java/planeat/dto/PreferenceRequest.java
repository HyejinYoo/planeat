package planeat.dto;

import lombok.Getter;
import lombok.Setter;
import planeat.domain.Preference;

@Getter
@Setter
public class PreferenceRequest {
    private String foodType;
    private int budget;
    private String mood;
    private double latitude;
    private double longitude;

    public Preference toDomain() {
        return new Preference(foodType, budget, mood, latitude, longitude);
    }
}