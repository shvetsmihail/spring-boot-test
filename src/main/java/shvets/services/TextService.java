package shvets.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class TextService {
    private static final String PUNCT_AND_SPACE_PATTERN = "[,.!?:;\\-\\p{Space}]";
    private static final String NOT_PUNCT_PATTERN = "[^,.!?:;\\-]";

    //ASCII from 0 to 127 char
    private static final String ENGLISH_PATTERN = "\\p{ASCII}";

    public long getUniqueWordsCount(String text) {
        return Stream.of(text.split(PUNCT_AND_SPACE_PATTERN))
                .filter(s -> !s.isEmpty())
                .distinct().count();
    }

    public long getAllPunctuationCount(String message) {
        return message.replaceAll(NOT_PUNCT_PATTERN, "").length();
    }

    public String getReversedWords(String text) {
        StringBuilder reversedText = new StringBuilder(text);
        List<Integer> indexes = getPucntOrSpaceIndexes(text);
        indexes.add(text.length());

        int start = 0;
        for (int i : indexes) {
            if (i == start) {
                start = start + 1;
            } else {
                String word = text.substring(start, i);
                String reversed = new StringBuffer(word).reverse().toString();
                reversedText.replace(start, i, reversed);
                start = i + 1;
            }
        }
        return reversedText.toString();
    }

    public boolean isEnglish(String text) {
        return text.replaceAll(ENGLISH_PATTERN, "").length() == 0;
    }

    private List<Integer> getPucntOrSpaceIndexes(String text) {
        List<Integer> indexes = new ArrayList<>();
        Pattern p = Pattern.compile(PUNCT_AND_SPACE_PATTERN);

        Matcher m = p.matcher(text);
        while (m.find()) {
            indexes.add(m.start());
        }
        return indexes;
    }
}
