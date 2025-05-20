package planeat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationResponse {
    private String mealPlace;
    private String cafePlace;
    private String reason;
}