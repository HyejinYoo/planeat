package planeat.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendedPlace {
    private final String name;
    private final String address;
    private final String type;  // meal, dessert, activity 등
}