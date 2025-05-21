package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planeat.domain.Invite;
import planeat.dto.invite.InviteCreateRequest;
import planeat.dto.invite.InviteResponseRequest;
import planeat.service.InviteService;

import java.util.Map;

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
    public ResponseEntity<String> submitResponse(@PathVariable String inviteCode,
                                                 @RequestBody InviteResponseRequest request) {
        return inviteService.addResponse(inviteCode, request)
                ? ResponseEntity.ok("Response submitted")
                : ResponseEntity.badRequest().body("Invalid invite code");
    }

    @GetMapping("/{inviteCode}")
    public ResponseEntity<Invite> getInvite(@PathVariable String inviteCode) {
        return inviteService.getInvite(inviteCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 

