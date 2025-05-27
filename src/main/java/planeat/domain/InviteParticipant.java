package planeat.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InviteParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<StepKeyword> preferences;

    private double latitude;
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    private Invite invite;
}
