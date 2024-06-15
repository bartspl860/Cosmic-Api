package cosmic.conquest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {
    @Autowired
    private RankingRepo rankingRepo;

    public void AddScore(RankingScore rankingScore){
        var allRankings = rankingRepo.findAll();
        if (allRankings.size() < 10)
        {
            rankingRepo.save(rankingScore);
        }
        else{
            var minRanking = allRankings.stream().min(Comparator.comparingInt(RankingScore::getScore)).orElseThrow();
            if(minRanking.getScore() < rankingScore.getScore()){
                minRanking.toBuilder()
                        .nickname(rankingScore.getNickname())
                        .score(rankingScore.getScore())
                        .date(rankingScore.getDate())
                        .build();
                rankingRepo.save(minRanking);
            }
        }
    }

    public List<RankingScore> GetScores(){
        return rankingRepo.findAll();
    }
}
