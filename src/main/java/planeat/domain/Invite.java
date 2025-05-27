package planeat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String inviteCode;

    private int targetCount;

    @ElementCollection(targetClass = StepType.class)
    @CollectionTable(name = "invite_steps", joinColumns = @JoinColumn(name = "invite_id"))
    @Column(name = "step")
    @Enumerated(EnumType.STRING)
    private List<StepType> steps;

    @OneToMany(mappedBy = "invite", cascade = CascadeType.ALL)
    private List<InviteParticipant> responses = new ArrayList<>();

    public boolean isComplete() {
        return responses.size() >= targetCount;
    }

    public void addResponse(InviteParticipant response) {
        responses.add(response);
        response.setInvite(this); // 양방향 매핑
    }
}

