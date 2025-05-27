package planeat.dto.invite;

import lombok.Getter;
import lombok.Setter;
import planeat.domain.StepType;

import java.util.List;

@Getter
@Setter
public class InviteCreateRequest {
    private int participantCount;
    private List<StepType> steps;  
}