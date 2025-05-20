package planeat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendedPlace {
    private String name;
    private String address;
    private String type;  // meal, dessert, activity 등
}