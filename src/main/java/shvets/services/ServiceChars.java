package shvets.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceChars implements Service {
    @Override
    public long getUniqueWordsCount(String text) {
        Set<String> uniqueWords = new HashSet<>();
        List<Integer> pucntOrSpaceIndexes = getPucntOrSpaceIndexes(text);
        int start = 0;
        for (int i : pucntOrSpaceIndexes) {
            if (i == start) {
                start = start + 1;
                continue;
            }
            if (i > start) {
                String word = text.substring(start, i);
                uniqueWords.add(word);
                start = i + 1;
            }
        }
        return uniqueWords.size();
    }

    @Override
    public long getUniquePunctuationCount(String text) {
        Set<Character> uniquePunctuation = new HashSet<>();
        List<Integer> pucntOrSpaceIndexes = getPucntOrSpaceIndexes(text);
        char[] chars = text.toCharArray();

        for (int i : pucntOrSpaceIndexes) {
            if (chars[i] != ' ') {
                uniquePunctuation.add(chars[i]);
            }
        }
        return uniquePunctuation.size();
    }

    @Override
    public String getReversedWords(String text) {
        StringBuilder stringBuilder = new StringBuilder(text);
        List<Integer> pucntOrSpaceIndexes = getPucntOrSpaceIndexes(text);
        int start = 0;
        for (int i : pucntOrSpaceIndexes) {
            if (i == start) {
                start = start + 1;
                continue;
            }
            if (i > start) {
                String word = text.substring(start, i);
                String reversed = new StringBuffer(word).reverse().toString();
                stringBuilder.replace(start, i, reversed);
                start = i + 1;
            }
        }
        return stringBuilder.toString();
    }

    private List<Integer> getPucntOrSpaceIndexes(String text) {
        List<Integer> indexes = new ArrayList<>();
        Pattern p = Pattern.compile(PUNCT_OR_SPACE_PATTERN);

        Matcher m = p.matcher(text);
        while (m.find()) {
            indexes.add(m.start());
        }
        return indexes;
    }
}
