package planeat.controller;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import planeat.dto.RecommendedPlace;
import planeat.dto.RoutePreferenceRequest;
import planeat.service.RoutePlannerService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoutePlannerController {

    private final RoutePlannerService routePlannerService;

    @PostMapping("/route")
    public ResponseEntity<List<RecommendedPlace>> planRoute(@RequestBody RoutePreferenceRequest request) {
        List<RecommendedPlace> route = routePlannerService.planRoute(request);
        return ResponseEntity.ok(route);
    }
}
