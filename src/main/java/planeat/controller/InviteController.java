package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planeat.domain.Invite;
import planeat.dto.invite.InviteCreateRequest;
import planeat.dto.invite.InviteResponseRequest;
import planeat.service.InviteService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createInvite(@RequestBody InviteCreateRequest request) {
        return ResponseEntity.ok(inviteService.createInvite(request));
    }

    @PostMapping("/{inviteCode}/respond")
    public ResponseEntity<String> submitResponse(@PathVariable("inviteCode") String inviteCode,
                                                 @RequestBody InviteResponseRequest request) {
        return inviteService.addResponse(inviteCode, request)
                ? ResponseEntity.ok("Response submitted")
                : ResponseEntity.badRequest().body("Invalid invite code");
    }
    
    @GetMapping("/{inviteCode}")
    public ResponseEntity<Map<String, Object>> getInviteDetails(@PathVariable("inviteCode") String inviteCode) {
        Optional<Invite> inviteOpt = inviteService.getInvite(inviteCode);
        if (inviteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invite invite = inviteOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("inviteCode", invite.getInviteCode());
        response.put("targetCount", invite.getTargetCount());
        response.put("steps", invite.getSteps());

        return ResponseEntity.ok(response);
    }

}

