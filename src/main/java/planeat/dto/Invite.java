package planeat.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Invite {
    private String inviteCode;
    private int targetCount;
    private List<StepInfo> steps;  // 초대 시 정해진 루트
    private List<InviteParticipantResponse> responses = new ArrayList<>();

    public Invite(String inviteCode, int targetCount, List<StepInfo> steps) {
        this.inviteCode = inviteCode;
        this.targetCount = targetCount;
        this.steps = steps;
    }

    public void addResponse(InviteParticipantResponse response) {
        responses.add(response);
    }

    public boolean isComplete() {
        return responses.size() >= targetCount;
    }
}
