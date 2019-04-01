package shvets.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextCountResult {
    private long uniqueWordsCount;
    private long uniquePunctuationCount;
}
