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

    private static class Pair <ItemA, ItemB> {
        public ItemA first;
        public ItemB second;
        public Pair (ItemA first, ItemB second) {
            this.first = first;
            this.second = second;
        }
    }

    private static class Triple <ItemA, ItemB, ItemC> {
        public ItemA first;
        public ItemB second;
        public ItemC third;
        public Triple (ItemA first, ItemB second, ItemC third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }


    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    // Task 7

    public static Set<Integer> perfectMatches (String aFileName, float th) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String g = lines.get(0);
        String t = lines.get(1);

        float[][] scoreMatrix = wordsScoreMatrix (g, t, 4);
        float[] threshold = alignScoreTh (g, 4, th);
        Set<Pair<Integer, Integer>> set = highScores (scoreMatrix, threshold);

        Set<Integer> result = new HashSet<>();
        for (Pair<Integer, Integer> pair : set)
            result.add(pair.second);

        return result;
    }

    // Task 8

    public static Set<Triple<Integer, Integer, Float>> highScoreAlignments (String aFileName, float th, float thl) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String g = lines.get(0);
        String t = lines.get(1);

        return null;
    }

    private static float[][] coupleScoreMatrix (String g, String t) {
        float[][] result = new float[g.length()][t.length()];

        for (int i = 0; i < g.length(); i++)
            for (int j = 0; j < t.length(); j++)
                result[i][j] = Blosum50.getScore(g.charAt(i), t.charAt(j));

        return result;
    }

    private static float wordScore (float[][] coupleScore, int i, int j, int k) {
        if (i + k > coupleScore.length || j + k > coupleScore[0].length)
            throw new java.lang.IllegalArgumentException();

        float result = 0;
        for (int l = 0; l < k; l++)
            result += coupleScore[i + l][j + l];

        return result;
    }

    private static float[][] wordsScoreMatrix (String g, String t, int k) {
        if (k > g.length() || k > t.length())
            throw new java.lang.IllegalArgumentException();

        float[][] coupleScore = coupleScoreMatrix (g, t);
        float[][] result = new float[g.length() - k + 1][t.length() - k + 1];

        for (int i = 0; i < g.length() - k + 1; i++)
            for (int j = 0; j < t.length() - k + 1; j++)
                result[i][j] = wordScore(coupleScore, i, j, k);

        return result;
    }

    private static Set<Pair<Integer, Integer>> highScores (float[] scores, float th, int i) {
        HashSet<Pair<Integer, Integer>> indeces = new HashSet<>();

        for (int j = 0; j < scores.length; j ++)
            if (scores[j] >= th)
                indeces.add(new Pair (i, j));

        return indeces;
    }

    private static Set<Pair<Integer, Integer>> highScores (float[][] wordsScore, float[] th) {
        HashSet<Pair<Integer, Integer>> indeces = new HashSet<>();

        for (int i = 0; i < wordsScore.length; i ++)
            indeces.addAll(highScores(wordsScore[i], th[i], i));

        return indeces;
    }

    private static float[] alignScoreTh (String g, int k, float th) {
        float[] result = new float[g.length() - k + 1];

        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
            for (int l = 0; l < k; l ++)
                result[i] += Blosum50.getScore(g.charAt(i + l), g.charAt(i + l));
            result[i] *= th;
        }

        return result;
    }

    //private static float[][]

    private static Triple<Integer, Integer, Float> findMaxIndeces (float[][] scores, float th) {

        Triple<Integer, Integer, Float> result = new Triple<>(-1, -1, 0F);

        for (int i = 0; i < scores.length; i ++)
            for (int j = 0; j < scores[0].length; j++)
                if (scores[i][j] > th && i + j > result.first + result.second) {
                    result.first = i;
                    result.second = j;
                    result.third = scores[i][j];
                }

        return result;
    }

}
