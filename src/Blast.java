import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Blast {
    final static Charset ENCODING = StandardCharsets.UTF_8;


    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    private static int[] computePrefixFunction  (String plainText) {
        int[] s = new int[plainText.length()];
        int border = 0;
        s[0] = 0;
        for (int i = 1; i < plainText.length(); i++) {
            while (border > 0 && plainText.charAt(i) != plainText.charAt(border))
                border = s[border - 1];
            if (plainText.charAt(i) == plainText.charAt(border))
                border += 1;
            else
                border = 0;
            s[i] = border;
        }
        return s;
    }

    private static Set<Integer> patternMatch (String pattern, String text) {
        String S = pattern + '$' + text;
        int [] s = computePrefixFunction(S);

        Set<Integer> indices = new HashSet<>();
        for (int i = 0; i < S.length(); i++)
            if (i > pattern.length() && s[i] == pattern.length())
                indices.add(i - 2*pattern.length());

        return indices;
    }

    private static Set<Integer> patternMatch (Set<String> patterns, String text) {
        Set<Integer> indices = new HashSet<>();

        for (String pattern : patterns)
            indices.addAll(patternMatch(pattern, text));

        return indices;
    }

    private static List<String> allPossible4LetterWords () {
        char[] letters = {'A', 'R', 'N', 'D', 'C' ,'Q' ,'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V'};

        List<String> possibleWords = new LinkedList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            word.append(letters[i]);
            for (int j = 0; j < 20; j++) {
                word.append(letters[j]);
                for (int k = 0; k < 20; k++) {
                    word.append(letters[k]);
                    for (int l = 0; l < 20; l++) {
                        word.append(letters[l]);
                        possibleWords.add(word.toString());
                    }
                }
            }
        }

        return possibleWords;

    }
    private static List<String> wordSeeds (String word, List<String> possibleWords, float th) {
        float scoreW = Alignment.computeScoreBlosum50(word, word);

        List<String> highScoreWords = new LinkedList<>();
        for (String possibleWord : possibleWords) {
            float score = Alignment.computeScoreBlosum50(word, possibleWord);
            if (score > th*scoreW)
                highScoreWords.add(possibleWord);
        }

        return highScoreWords;
    }

    private static Set<String> g4LetterWords (String g) {

        Set<String> words = new HashSet<>();
        for (int i = 0; i < g.length() - 3; i++) {
            String word = g.substring(i, i + 4);
            words.add(word);
        }


        return words;
    }

    private static Set<String> gSeeds (String g, float th) {
        List<String> possibleWords = allPossible4LetterWords();
        Set<String> words = g4LetterWords(g);

        Set<String> seeds = new HashSet<>();
        for (String word : words) {
            List<String> highScoreWords = wordSeeds(word, possibleWords, th);
            seeds.addAll(highScoreWords);
        }

        return seeds;
    }

    public static Set<Integer> perfectMatches (String aFileName, float th) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String g = lines.get(0);
        String t = lines.get(1);

        System.out.println(g4LetterWords(g).toString());
        Set<Integer> result = patternMatch(gSeeds(g, th), t);

        System.out.println(result.toString());
        return result;
    }
}
