package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planeat.service.RecommendationService;
import planeat.domain.Invite;
import planeat.service.InviteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final InviteService inviteService;
    private final RecommendationService recommendationService;

    @GetMapping("/{inviteCode}")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations(@PathVariable("inviteCode") String inviteCode) {
        return inviteService.getInvite(inviteCode)
                .filter(Invite::isComplete)
                .map(recommendationService::recommend)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
} 
