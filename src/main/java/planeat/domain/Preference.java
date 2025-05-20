package planeat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Preference {
    private String foodType;
    private int budget;
    private String mood;
    private double latitude;
    private double longitude;
}