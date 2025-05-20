package planeat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteResponseRequest {
    private String name;                    // 참여자 이름
    private List<StepPreference> preferences;    // 단계별 선호도
    private double latitude;               // 위치
    private double longitude;
}
