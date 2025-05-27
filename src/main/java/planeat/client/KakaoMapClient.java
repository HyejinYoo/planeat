package planeat.client;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoMapClient implements PlaceSearchClient {

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate;

    @Override
    public List<JSONObject> searchByKeyword(String keyword, double lat, double lon) {
        JSONObject result = search(keyword, lat, lon, 2000);
        JSONArray documents = result.optJSONArray("documents");

        List<JSONObject> places = new ArrayList<>();
        if (documents == null || documents.isEmpty()) {
            JSONObject empty = new JSONObject();
            empty.put("place_name", "추천 결과 없음");
            empty.put("address_name", "해당 위치에 적합한 장소가 없습니다.");
            empty.put("category_group_name", "없음");
            places.add(empty);
            return places;
        }

        for (int i = 0; i < documents.length(); i++) {
            places.add(documents.getJSONObject(i));
        }
        return places;
    }


    public JSONObject search(String keyword, double lat, double lon, int radius) {
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .queryParam("x", lon)
                .queryParam("y", lat)
                .queryParam("radius", radius)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return new JSONObject(response.getBody());
    }
}
