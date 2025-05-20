package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planeat.dto.*;
import planeat.service.InviteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createInvite(@RequestBody InviteCreateRequest request) {
        String code = inviteService.createInvite(request.getParticipantCount(), request.getSteps());
        Map<String, String> response = new HashMap<>();
        response.put("inviteCode", code);
        response.put("link", "https://planeat.com/invite/" + code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{inviteCode}/respond")
    public ResponseEntity<String> submitResponse(@PathVariable("inviteCode") String inviteCode,
                                                 @RequestBody InviteResponseRequest request) {
        boolean success = inviteService.addResponse(inviteCode, request);
        if (!success) return ResponseEntity.badRequest().body("Invalid invite code");
        return ResponseEntity.ok("Response submitted");
    }

    @GetMapping("/{inviteCode}")
    public ResponseEntity<Invite> getInvite(@PathVariable("inviteCode") String inviteCode) {
        return inviteService.getInvite(inviteCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{inviteCode}/result")
    public ResponseEntity<?> getResult(@PathVariable("inviteCode") String inviteCode) {
    	List<Map<String, Object>> result = inviteService.getRecommendations(inviteCode);
        if (result == null) {
            return ResponseEntity.badRequest().body("참여 인원이 아직 모두 응답하지 않았습니다.");
        }
        return ResponseEntity.ok(result); // TODO: 추천 로직 결과로 변경 예정
    }
}
