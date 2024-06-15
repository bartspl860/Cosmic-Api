package cosmic.conquest.api;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepo extends JpaRepository<RankingScore, Long> {
}
