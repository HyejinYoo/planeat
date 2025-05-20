package planeat.client;

import org.json.JSONObject;

import planeat.dto.StepPreference;


public interface PlaceSearchClient {
    JSONObject searchByStep(StepPreference step, double lat, double lon);
    
    JSONObject search(String keyword, double lat, double lon, int radius);
}