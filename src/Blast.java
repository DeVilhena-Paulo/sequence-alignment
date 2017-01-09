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

    public static class Triple <ItemA, ItemB, ItemC> {
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

    /**
     * perfectMatches (aFileName, th) : takes g, t from a input file and th as parameter and returns
     * all the indices that correspond to beginning of perfect matches between an element of Sg and
     * a subword of t.
     */
    public static Set<Integer> perfectMatches (String aFileName, float th, int k) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String g = lines.get(0);
        String t = lines.get(1);

        //int k = 4;

        float[][] scoreMatrix = wordsScoreMatrix (g, t, k);
        float[] threshold = alignScoreTh (g, k, th);
        Set<Pair<Integer, Integer>> set = highScores (scoreMatrix, threshold);

        Set<Integer> result = new HashSet<>();
        for (Pair<Integer, Integer> pair : set)
            result.add(pair.second);

        Display.printPerfectMatchIndices (result, t, k);
        return result;
    }

    /**
     * coupleScoreMatrix (g, t) : returns the Blosum50 score of all possible couples between a letter
     * of g and a letter of t.
     */
    private static float[][] coupleScoreMatrix (String g, String t) {
        float[][] result = new float[g.length()][t.length()];

        for (int i = 0; i < g.length(); i++)
            for (int j = 0; j < t.length(); j++)
                result[i][j] = Blosum50.getScore(g.charAt(i), t.charAt(j));

        return result;
    }

    /**
     * wordScore (oupleScore, i, j, k) : returns the Blosum50 score of alignment between a k letters
     * words of g starting at i and a k letters word of t starting at j.
     */
    private static float wordScore (float[][] coupleScore, int i, int j, int k) {
        if (i + k > coupleScore.length || j + k > coupleScore[0].length)
            throw new java.lang.IllegalArgumentException();

        float result = 0;
        for (int l = 0; l < k; l++)
            result += coupleScore[i + l][j + l];

        return result;
    }

    /**
     * wordsScoreMatrix (g, t, k) : returns a matrix with all possible score of alignment between a k
     * letters word of g and a k letters word of t.
     */
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
        HashSet<Pair<Integer, Integer>> indices = new HashSet<>();

        for (int j = 0; j < scores.length; j ++)
            if (scores[j] >= th)
                indices.add(new Pair (i, j));

        return indices;
    }

    /**
     * highScores (wordsScore, th) : if wordScore[i][j] > th[i], adds (i, j) to the output
     */
    private static Set<Pair<Integer, Integer>> highScores (float[][] wordsScore, float[] th) {
        HashSet<Pair<Integer, Integer>> indices = new HashSet<>();

        for (int i = 0; i < wordsScore.length; i ++)
            indices.addAll(highScores(wordsScore[i], th[i], i));

        return indices;
    }

    /**
     * alignScoreTh (g, k, th) : return an array with the Blosum50 alignment score of all k letters
     * words of the String g with itself times the threshold th.
     */
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

    /**
     * alignedCouples (g, t, indices, k) : finds the local alignment of g with t and returns the
     * Blosum50 scores of all the couples from this alignment. It also returns the index of the k
     * letters subword match match.
     */
    private static Pair<float[], Integer> alignedCouples (String g, String t, Pair<Integer, Integer> indices, int k) {
        int indI = indices.first;
        int indJ = indices.second;

        if (indI + k > g.length() || indJ + k > t.length())
            throw new java.lang.IllegalArgumentException();

        int leftSize = (indI < indJ) ? indI : indJ;
        int gRightSize = g.length() - indI - k;
        int tRightSize = t.length() - indJ - k;
        int rightSize = (gRightSize < tRightSize) ? gRightSize  : tRightSize;

        float[] result = new float [leftSize + k + rightSize];

        for (int l = 0; l < leftSize; l ++)
            result[l] = Blosum50.getScore(g.charAt(indI - leftSize + l), t.charAt(indJ - leftSize + l));

        for (int l = 0; l < k; l ++)
            result[leftSize + l] = Blosum50.getScore(g.charAt(indI + l), t.charAt(indJ + l));

        for (int l = 0; l < rightSize; l ++)
            result[leftSize + k + l] = Blosum50.getScore(g.charAt(indI + k + l), t.charAt(indJ + k + l));

        return new Pair(result, leftSize);
    }

    /**
     * costMatrix (a, ind, k) : builds a matrix result that stores at result[i][j] the score of
     * alignment between en extension of the matching by i letters to the left and j letters to
     * the right. It means that result[0][0] is the score of aligning a seed from g with the k
     * letters subword from t that matches this seed.
     */
    private static float[][] costMatrix (float[] a, int ind, int k) {
        if (k <= 0)
            throw new java.lang.IllegalArgumentException();

        float[][] result = new float [ind + 1][a.length - ind - k + 1];

        result[0][0] = 0;
        for (int l = 0; l < k; l ++)
            result[0][0] += a[ind + l];

        for (int l = 1; l < ind + 1; l ++)
            result[l][0] += result[l - 1][0] + a[ind - l];

        for (int l = 1; l < a.length - ind - k + 1; l ++)
            result[0][l] += result[0][l - 1] + a[ind + k - 1 + l];

        for (int i = 1; i < ind + 1; i++)
            for (int j = 1; j < a.length - ind - k + 1; j++)
                result[i][j] += result[i - 1][j] + a[ind - i];

        return result;
    }

    /**
     * findMaxindices (scores, th) : finds i and j in the double array scores that maximizes the
     * sum i + j and satisfies scores[i][j] >= th.
     */
    private static Triple<Integer, Integer, Float> findMaxindices (float[][] scores, float th) {
        if(scores.length < 1 || scores[0].length < 1)
            throw new java.lang.IllegalArgumentException();

        int indI = 0;
        int indJ = 0;
        float score = scores[0][0];
        for (int i = 0; i < scores.length; i ++)
            for (int j = 0; j < scores[0].length; j++)
                if (scores[i][j] >= th && i + j > indI + indJ) {
                    indI = i;
                    indJ = j;
                    score = scores[i][j];
                }

        Triple<Integer, Integer, Float> result = null;
        if (score >= th)
            result = new Triple<>(indI, indJ, score);
        return result;
    }

    // Task 8

    /**
     * highScoreAlignments (aFileName, th, thl) : takes g, t, from a input file, th and th` as parameters
     * and returns all the local alignments with sufficiently high scores.
     */
    public static Set<Triple<Integer, Integer, Float>> highScoreAlignments (String aFileName, float th, float thl, int k) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String g = lines.get(0);
        String t = lines.get(1);

        float[][] scoreMatrix = wordsScoreMatrix (g, t, k);
        float[] threshold = alignScoreTh (g, k, th);
        Set<Pair<Integer, Integer>> set = highScores (scoreMatrix, threshold);

        Set<Triple<Integer, Integer, Float>> result = new HashSet<>();
        float gScoreTh = Computation.blosum50Score(g, g) * thl;

        for (Pair<Integer, Integer> indices : set) {
            int gIndex = indices.first;
            int tIndex = indices.second;

            Pair<float[], Integer> pair = alignedCouples (g, t, indices, k);
            float[] alignedCouplesScores = pair.first;
            int matchIndex = pair.second;
            float[][] matrix = costMatrix(alignedCouplesScores, matchIndex, k);

            Triple<Integer, Integer, Float> triple = findMaxindices(matrix, gScoreTh);
            if (triple != null) {
                int leftExtension = triple.first;
                int rightExtension = triple.second;
                float score = triple.third;
                result.add(new Triple(tIndex - leftExtension, k + leftExtension + rightExtension, score));
                Display.printIntermediateBlastResults(g, t, gScoreTh, gIndex, tIndex, leftExtension, rightExtension, k, score);
            }
        }

        return result;
    }

}
