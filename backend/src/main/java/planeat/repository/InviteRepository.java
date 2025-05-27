package planeat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import planeat.domain.Invite;

import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
	Optional<Invite> findByInviteCode(String inviteCode);
}
