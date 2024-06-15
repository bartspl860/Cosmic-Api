package cosmic.conquest.api;

import cosmic.conquest.api.config.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RankingService rankingService;
    @PostMapping("/submit")
    public ResponseEntity<String> submitScore(HttpServletRequest request) throws IOException {
        var token = jwtUtils.getJwtFromCookies(request);
        if(!jwtUtils.validateJwtToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Hey there,\n" +
                            "Trying to sneak in a fake score, huh? We're all about fair play here, so let's keep it legit. Feel free to try your best for a real spot on the leaderboard!\n" +
                            "If you need any help or have questions, give us a shout.\n" +
                            "Cheers,\n" +
                            "Square Games Team"
            );
        }
        Claims claims = jwtUtils.getClaimsFromToken(token);
        rankingService.AddScore(RankingScore.fromClaims(claims));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<RankingScore>> getScores(){
        var scores = rankingService.GetScores();
        scores.sort(Comparator.comparing(RankingScore::getScore));
        return ResponseEntity.ok(scores);
    }
}
