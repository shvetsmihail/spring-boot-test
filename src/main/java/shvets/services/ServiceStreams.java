package shvets.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceStreams implements Service {

    @Override
    public long getUniqueWordsCount(String message) {
        return getUniqueWords(message).size();
    }

    @Override
    public long getUniquePunctuationCount(String message) {
        String onlyPunctuation = message.replaceAll(NOT_PUNCT_PATTERN, "");
        return Stream.of(onlyPunctuation.split("")).distinct().count();
    }

    @Override
    public String getReversedWords(String text) {
        Set<String> uniqueWords = getUniqueWords(text);
        for (String word : uniqueWords) {
            String reversed = new StringBuffer(word).reverse().toString();
            text = text.replaceAll(word, reversed);
        }
        return text;
    }

    private Set<String> getUniqueWords(String text) {
        return Stream.of(text.split(PUNCT_OR_SPACE_PATTERN))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
