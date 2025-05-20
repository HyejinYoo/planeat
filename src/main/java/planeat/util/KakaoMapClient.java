package planeat.util;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;

import planeat.client.PlaceSearchClient;
import planeat.dto.StepPreference;

@Component
@RequiredArgsConstructor
public class KakaoMapClient implements PlaceSearchClient {

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public JSONObject searchByStep(StepPreference step, double lat, double lon) {
        String keyword;

        switch (step.getType()) {
            case "meal":
            case "dessert":
                keyword = step.getFoodType() != null ? step.getFoodType() : "음식점";
                break;
            case "walk":
                keyword = "산책로";
                break;
            case "activity":
                keyword = step.getSubtype() != null ? step.getSubtype() : "놀거리";
                break;
            case "viewpoint":
                keyword = step.getMood() != null ? step.getMood() + " 야경" : "야경 명소";
                break;
            default:
                keyword = "장소";
        }

        JSONObject result = search(keyword, lat, lon, 2000); // ✅ 이 메서드 정의가 필요해!
        return result.getJSONArray("documents").getJSONObject(0); // 첫 번째 장소 반환
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
