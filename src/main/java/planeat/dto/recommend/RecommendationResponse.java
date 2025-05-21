package planeat.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendationResponse {
    private final List<RecommendedPlace> places;  
    private final String reason;                  
}
