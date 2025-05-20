package planeat.assembler;

import org.json.JSONObject;
import planeat.domain.Preference;
import planeat.dto.RecommendationResponse;

public class RecommendationAssembler {

    public static RecommendationResponse from(JSONObject mealPlace, String cafeName, Preference pref) {
        String mealName = mealPlace.getString("place_name");

        String reason = String.format(
                "'%s' 선호, %d원 예산, '%s' 분위기를 반영한 추천입니다.",
                pref.getFoodType(), pref.getBudget(), pref.getMood()
        );

        return new RecommendationResponse(mealName, cafeName, reason);
    }

    public static RecommendationResponse emptyResult(String foodType) {
        return new RecommendationResponse(
                "검색 결과 없음",
                "카페 미정",
                "해당 위치 주변에서 '" + foodType + "' 장소를 찾을 수 없습니다."
        );
    }
}
