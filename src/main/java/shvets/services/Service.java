package shvets.services;

public interface Service {
    String PUNCT_OR_SPACE_PATTERN = "[\\p{Punct} ]";
    String NOT_PUNCT_PATTERN = "[^\\p{Punct}]";

    long getUniqueWordsCount(String text);
    long getUniquePunctuationCount(String text);
    String getReversedWords(String text);
}
