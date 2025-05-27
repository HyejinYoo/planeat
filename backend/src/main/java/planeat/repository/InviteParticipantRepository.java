package planeat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import planeat.domain.InviteParticipant;

public interface InviteParticipantRepository extends JpaRepository<InviteParticipant, Long> {
}