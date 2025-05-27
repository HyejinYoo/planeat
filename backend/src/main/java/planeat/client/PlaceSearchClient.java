package planeat.client;

import java.util.List;

import org.json.JSONObject;

public interface PlaceSearchClient {
	List<JSONObject> searchByKeyword(String keyword, double lat, double lon);
}