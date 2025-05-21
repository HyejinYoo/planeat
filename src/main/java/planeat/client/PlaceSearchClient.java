package planeat.client;

import org.json.JSONObject;

import planeat.dto.preference.StepPreference;


public interface PlaceSearchClient {
    JSONObject searchByKeyword(String keyword, double lat, double lon);
}