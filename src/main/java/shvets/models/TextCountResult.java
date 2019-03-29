package shvets.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TextCountResult {
    private long uniqueWordsCount;
    private long uniquePunctuationCount;
}
