package planeat.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Invite {
    private String inviteCode;
    private int targetCount;
    private List<StepType> steps;  
    private List<InviteParticipant> responses = new ArrayList<>();

    public Invite(String inviteCode, int targetCount, List<StepType> steps) {
        this.inviteCode = inviteCode;
        this.targetCount = targetCount;
        this.steps = steps;
    }

    public void addResponse(InviteParticipant response) {
        responses.add(response);
    }

    public boolean isComplete() {
        return responses.size() >= targetCount;
    }
}
