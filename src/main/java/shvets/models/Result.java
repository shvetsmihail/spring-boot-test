package shvets.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private String uniqueWordsCount;
    private String uniquePunctuationCount;
    private String reversedWords;
}
