package planeat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteCreateRequest {
    private int participantCount;
    private List<StepInfo> steps;  // 방장이 정하는 루트 순서
}