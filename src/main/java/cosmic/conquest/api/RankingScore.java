package cosmic.conquest.api;

import io.jsonwebtoken.Claims;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RankingScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;
    private String nickname;
    private LocalDate date;
    public static RankingScore fromClaims(Claims claims){
        var rankingScore = RankingScore.builder()
                .nickname(claims.get("nickname", String.class))
                .score(claims.get("score", Integer.class))
                .build();
        String dateString = claims.get("date", String.class);
        return rankingScore.toBuilder()
                .date(LocalDate.parse(dateString , DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();
    }
    @Override
    public String toString() {
        return "RankingScore{" +
                "score=" + score +
                ", nickname='" + nickname + '\'' +
                ", date=" + date +
                '}';
    }
}