package planeat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepPreference {
    private String type;       // meal, walk, dessert, activity, viewpoint
    private String foodType;
    private String mood;
    private int budget;
    private String subtype;
}