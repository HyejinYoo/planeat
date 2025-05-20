package planeat.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import planeat.client.PlaceSearchClient;
import planeat.dto.RecommendedPlace;
import planeat.dto.RoutePreferenceRequest;
import planeat.dto.StepPreference;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutePlannerService {

    private final PlaceSearchClient placeSearchClient;

    public List<RecommendedPlace> planRoute(RoutePreferenceRequest request) {
        List<RecommendedPlace> route = new ArrayList<>();

        for (StepPreference step : request.getSteps()) {
            try {
                JSONObject place = placeSearchClient.searchByStep(step, request.getLatitude(), request.getLongitude());

                String name = place.getString("place_name");
                String address = place.getString("address_name");
                String type = step.getType();

                route.add(new RecommendedPlace(name, address, type));

            } catch (Exception e) {
                route.add(new RecommendedPlace("추천 실패", "결과 없음", step.getType()));
            }
        }

        return route;
    }
}
