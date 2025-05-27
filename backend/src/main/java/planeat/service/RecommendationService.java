package planeat.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import planeat.client.PlaceSearchClient;
import planeat.domain.Invite;
import planeat.domain.InviteParticipant;
import planeat.domain.StepKeyword;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final PlaceSearchClient placeSearchClient;

    public List<Map<String, Object>> recommend(Invite invite) {
        List<InviteParticipant> responses = invite.getResponses();

        System.out.println("✅ 응답자 수: " + responses.size());
        
        double avgLat = responses.stream()
                .mapToDouble(InviteParticipant::getLatitude)
                .average()
                .orElse(0);

        double avgLon = responses.stream()
                .mapToDouble(InviteParticipant::getLongitude)
                .average()
                .orElse(0);

        List<Map<String, Object>> placeRecommendations = new ArrayList<>();

        
        for (InviteParticipant participant : responses) {
        	System.out.println("✅ 답: " + participant.getPreferences());

            for (StepKeyword stepKeyword : participant.getPreferences()) {
            	
            	List<String> keywords = new ArrayList<>();
            	if (stepKeyword.getKeyword1() != null && !stepKeyword.getKeyword1().isBlank()) {
            	    keywords.add(stepKeyword.getKeyword1());
            	}
            	if (stepKeyword.getKeyword2() != null && !stepKeyword.getKeyword2().isBlank()) {
            	    keywords.add(stepKeyword.getKeyword2());
            	}
            	if (stepKeyword.getKeyword3() != null && !stepKeyword.getKeyword3().isBlank()) {
            	    keywords.add(stepKeyword.getKeyword3());
            	}

            	for (String keyword : keywords) {
            	    System.out.println("🔍 검색 키워드: " + keyword); // 👉 이 줄 추가!

            	    List<JSONObject> places = placeSearchClient.searchByKeyword(keyword, avgLat, avgLon);

            	    for (JSONObject place : places) {
            	        Map<String, Object> oneResult = new HashMap<>();
            	        oneResult.put("stepType", stepKeyword.getStepType());
            	        oneResult.put("keyword", keyword);
            	        oneResult.put("placeName", place.optString("place_name"));
            	        oneResult.put("address", place.optString("address_name"));
            	        oneResult.put("category", place.optString("category_group_name"));

            	        placeRecommendations.add(oneResult);
            	    }
            	}

            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recommendations", placeRecommendations);
        return List.of(result);
    }
}


