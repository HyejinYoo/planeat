package planeat.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import planeat.dto.PreferenceRequest;
import planeat.dto.RecommendationResponse;
import planeat.util.KakaoMapClient;
import planeat.domain.Preference;
import planeat.client.PlaceSearchClient;
import planeat.assembler.RecommendationAssembler;


@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final PlaceSearchClient placeSearchClient;

	public RecommendationResponse recommend(PreferenceRequest request) {
	    Preference pref = request.toDomain();

	    JSONObject kakaoResponse = placeSearchClient.search(
	            pref.getFoodType(),
	            pref.getLatitude(),
	            pref.getLongitude(),
	            2000
	    );

	    JSONArray documents = kakaoResponse.getJSONArray("documents");

	    if (documents.isEmpty()) {
	        return RecommendationAssembler.emptyResult(pref.getFoodType());
	    }

	    JSONObject mealPlace = documents.getJSONObject(0);

	    return RecommendationAssembler.from(mealPlace, "카페 미정", pref);
	}
}