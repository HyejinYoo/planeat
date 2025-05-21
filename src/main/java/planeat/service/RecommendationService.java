package planeat.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import planeat.client.PlaceSearchClient;
import planeat.domain.Invite;
import planeat.domain.InviteParticipant;
import planeat.dto.preference.StepPreference;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final PlaceSearchClient placeSearchClient;

    public List<Map<String, Object>> recommend(Invite invite) {
        List<InviteParticipant> responses = invite.getResponses();

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
            for (StepPreference pref : participant.getPreferences()) {
                if (pref.getKeywords() == null || pref.getKeywords().isEmpty()) continue;

                for (String keyword : pref.getKeywords()) {
                    JSONObject place = placeSearchClient.searchByKeyword(keyword, avgLat, avgLon);

                    Map<String, Object> oneResult = new HashMap<>();
                    oneResult.put("keyword", keyword);
                    oneResult.put("placeName", place.optString("place_name"));
                    oneResult.put("address", place.optString("address_name"));
                    oneResult.put("category", place.optString("category_group_name"));

                    placeRecommendations.add(oneResult);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recommendations", placeRecommendations);
        return List.of(result);
    }
}

