package planeat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import planeat.client.KakaoMapClient;
import planeat.domain.Invite;
import planeat.domain.InviteParticipant;
import planeat.domain.StepKeyword;
import planeat.repository.InviteParticipantRepository;
import planeat.repository.InviteRepository;
import planeat.dto.invite.InviteCreateRequest;
import planeat.dto.invite.InviteResponseRequest;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final InviteParticipantRepository participantRepository;

    public Map<String, String> createInvite(InviteCreateRequest request) {
        String code = UUID.randomUUID().toString();
        Invite invite = Invite.builder()
                .inviteCode(code)
                .targetCount(request.getParticipantCount())
                .steps(request.getSteps())
                .build();
        inviteRepository.save(invite);

        Map<String, String> response = new HashMap<>();
        response.put("inviteCode", code);
        response.put("link", "https://planeat.com/invite/" + code);
        return response;
    }

    public boolean addResponse(String code, InviteResponseRequest request) {
        Optional<Invite> inviteOpt = inviteRepository.findByInviteCode(code);
        if (inviteOpt.isEmpty()) return false;

        Invite invite = inviteOpt.get();

        InviteParticipant participant = InviteParticipant.builder()
                .name(request.getName())
                .preferences(request.getPreferences())  // 🔁 getSteps() → getPreferences()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .invite(invite)
                .build();

        participantRepository.save(participant);
        return true;
    }

    public Optional<Invite> getInvite(String code) {
        return inviteRepository.findByInviteCode(code);
    }
}

