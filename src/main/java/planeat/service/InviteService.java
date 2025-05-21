package planeat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planeat.client.KakaoMapClient;
import planeat.domain.Invite;
import planeat.domain.InviteParticipant;
import planeat.domain.StepType;
import planeat.dto.invite.InviteCreateRequest;
import planeat.dto.invite.InviteResponseRequest;
import planeat.dto.preference.StepPreference;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final Map<String, Invite> inviteMap = new HashMap<>();
    private final KakaoMapClient kakaoMapClient;

    public Map<String, String> createInvite(InviteCreateRequest request) {
        String code = UUID.randomUUID().toString();
        Invite invite = new Invite(code, request.getParticipantCount(), request.getSteps());
        inviteMap.put(code, invite);

        Map<String, String> response = new HashMap<>();
        response.put("inviteCode", code);
        response.put("link", "https://planeat.com/invite/" + code);
        return response;
    }

    public boolean addResponse(String inviteCode, InviteResponseRequest request) {
        Invite invite = inviteMap.get(inviteCode);
        if (invite == null) return false;

        invite.addResponse(request.toDomain());
        return true;
    }

    public Optional<Invite> getInvite(String inviteCode) {
        return Optional.ofNullable(inviteMap.get(inviteCode));
    }
}
