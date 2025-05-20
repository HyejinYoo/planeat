package planeat.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import planeat.dto.*;
import planeat.util.KakaoMapClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final Map<String, Invite> inviteMap = new HashMap<>();
    private final KakaoMapClient kakaoMapClient;

    public String createInvite(int participantCount, List<StepInfo> steps) {
        String code = UUID.randomUUID().toString();
        Invite invite = new Invite(code, participantCount, steps);
        inviteMap.put(code, invite);
        return code;
    }

    public boolean addResponse(String inviteCode, InviteResponseRequest request) {
        Invite invite = inviteMap.get(inviteCode);
        if (invite == null) return false;

        InviteParticipantResponse response = new InviteParticipantResponse(
                request.getName(),
                request.getPreferences(),
                request.getLatitude(),
                request.getLongitude()
        );
        invite.addResponse(response);
        return true;
    }

    public Optional<Invite> getInvite(String inviteCode) {
        return Optional.ofNullable(inviteMap.get(inviteCode));
    }

    public boolean isComplete(String inviteCode) {
        Invite invite = inviteMap.get(inviteCode);
        return invite != null && invite.isComplete();
    }

    public List<Map<String, Object>> getRecommendations(String inviteCode) {
        Invite invite = inviteMap.get(inviteCode);
        if (invite == null || !invite.isComplete()) return null;

        List<Map<String, Object>> result = new ArrayList<>();
        List<StepInfo> steps = invite.getSteps();
        List<InviteParticipantResponse> responses = invite.getResponses();

        double avgLat = responses.stream().mapToDouble(InviteParticipantResponse::getLatitude).average().orElse(0);
        double avgLon = responses.stream().mapToDouble(InviteParticipantResponse::getLongitude).average().orElse(0);

        for (StepInfo step : steps) {
            String type = step.getType();

            List<String> foodTypes = new ArrayList<>();
            List<String> moods = new ArrayList<>();
            List<String> subtypes = new ArrayList<>();
            List<Integer> budgets = new ArrayList<>();

            for (InviteParticipantResponse r : responses) {
                r.getPreferences().stream()
                    .filter(p -> p.getType().equals(type))
                    .findFirst()
                    .ifPresent(p -> {
                        if (p.getFoodType() != null) foodTypes.add(p.getFoodType());
                        if (p.getMood() != null) moods.add(p.getMood());
                        if (p.getSubtype() != null) subtypes.add(p.getSubtype());
                        budgets.add(p.getBudget());
                    });
            }

            String topFoodType = getMostCommon(foodTypes);
            String topMood = getMostCommon(moods);
            String topSubtype = getMostCommon(subtypes);
            int avgBudget = budgets.stream().mapToInt(b -> b).sum() / Math.max(1, budgets.size());

            StepPreference preference = new StepPreference();
            preference.setType(type);
            preference.setFoodType(topFoodType);
            preference.setMood(topMood);
            preference.setBudget(avgBudget);
            preference.setSubtype(topSubtype);

            JSONObject place = kakaoMapClient.searchByStep(preference, avgLat, avgLon);

            Map<String, Object> stepResult = new HashMap<>();
            stepResult.put("type", type);
            stepResult.put("foodType", topFoodType);
            stepResult.put("mood", topMood);
            stepResult.put("subtype", topSubtype);
            stepResult.put("budget", avgBudget);
            stepResult.put("placeName", place.optString("place_name"));
            stepResult.put("address", place.optString("address_name"));
            stepResult.put("category", place.optString("category_group_name"));
            result.add(stepResult);
        }

        return result;
    }

    private String getMostCommon(List<String> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
