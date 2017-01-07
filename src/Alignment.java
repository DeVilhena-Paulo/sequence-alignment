import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.LinkedList;

public class Alignment {

    final static Charset ENCODING = StandardCharsets.UTF_8;

    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    /**
     * lcs (s, t): this method computes the longest common subsequence between two sequences of letters,
     * the first has length n and the second m, using dynamic programming. The array M stores at M[i][j]
     * the length longest common subsequence between the first i letters of the sequence s and the first
     * j letters of the sequence t.
     */
    private static int[][] lcs(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] M = new int[n + 1][m + 1];
        int[][] path = new int [n + 1][m + 1];

        for (int i = 0; i < n + 1; i++)
            M[i][0] = 0; // lcs between any sequence and an empty sequence is 0

        for (int j = 0; j < m + 1; j++)
            M[0][j] = 0; // lcs between any sequence and an empty sequence is 0

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {

                if (s.charAt(i - 1) == t.charAt(j - 1)) { // indices translated by 1 when accessing the strings
                    M[i][j] = 1 + M[i - 1][j - 1];
                    path[i][j] = 0;
                }

                else if (M[i - 1][j] >= M[i][j - 1]) {
                    M[i][j] = M[i - 1][j];
                    path[i][j] = 1;
                }

                else {
                    M[i][j] = M[i][j - 1];
                    path[i][j] = 2;
                }
            }
        }
        return path;
    }


    // Task 2

    /**
     * lcs (aFileName) : this methods computes the longest common subsequence between two sequences of
     * letters given in a input file.
     */
    public static String lcs (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        List<String> lines = Files.readAllLines(p, ENCODING);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);

        int n = s.length();
        int m = t.length();
        int[][] path = lcs(s,t);

        StringBuilder result = new StringBuilder();

        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (path[i][j] == 0) {
                result.append(s.charAt(i-1));
                i--;
                j--;
            }
            else if (path[i][j] == 1)
                i--;
            else
                j--;
        }

        return result.reverse().toString();
    }

    /**
     * backtracking : does the backtracking of the array path to reconstruct one optimal
     * alignment between the sequences s and t
     */
    public static List<String> backtracking (String s, String t, int[][] path) {
        int n = s.length();
        int m = t.length();

        StringBuilder sEdition = new StringBuilder();
        StringBuilder tEdition = new StringBuilder();

        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (path[i][j] == 0) {
                sEdition.append(s.charAt(i-1));
                tEdition.append(t.charAt(j-1));
                i--;
                j--;
            }
            else if (path[i][j] == 1) { // Deleting was chosen
                sEdition.append(s.charAt(i-1));
                tEdition.append('-');
                i--;
            }
            else { // Inserting was chosen
                tEdition.append(t.charAt(j-1));
                sEdition.append('-');
                j--;
            }
        }

        if (i == 0)
            while (j > 0) {
                tEdition.append(t.charAt(j-1));
                sEdition.append('-');
                j--;
            }

        else
            while (i > 0) {
                sEdition.append(s.charAt(i-1));
                tEdition.append('-');
                i--;
            }

        List<String> editedSeqs = new LinkedList<>();
        editedSeqs.add(sEdition.reverse().toString());
        editedSeqs.add(tEdition.reverse().toString());

        return editedSeqs;
    }


    /**
     * scoreBlosum50 (s, t) : this method computes the maximum score of the alignment between two sequences
     * of letters, s and t, using dynamic programming. The array S stores at S[i][j] the maximum score of the
     * alignment between the first i characters of the string s to the first j characters of the string t, being
     * able to manage the memoization process.
     */
    private static int[][] scoreBlosum50 (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();
        float[][] S = new float [n + 1][m + 1];
        int[][] path = new int [n + 1][m + 1];

        S[0][0] = 0F;

        for (int i = 1; i < n + 1; i++)
            S[i][0] = S[i-1][0] + Blosum50.getScore(s.charAt(i - 1), '-'); // deletion of i_th character from s

        for (int j = 1; j < m + 1; j++)
            S[0][j] = S[0][j-1] + Blosum50.getScore(t.charAt(j - 1), '-'); // insertion of j_th character from t in s

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {

                float alignScore = S[i - 1][j - 1] + Blosum50.getScore(s.charAt(i - 1), t.charAt(j - 1));

                float deletionScore = S[i - 1][j] +  Blosum50.getScore(s.charAt(i - 1), '-');
                if (j != m) { // if I'm opening a gap in the end of the sequence t, the penalty does not count
                    deletionScore -= increaseCost;
                    if (i == 1 || path[i - 1][j] == 0)
                        deletionScore -= openCost;
                }

                float insertionScore = S[i][j - 1] +  Blosum50.getScore(t.charAt(j - 1), '-');
                if (i != n) { // if I'm opening a gap in the end of the sequence s, the penalty does not count
                    insertionScore -= increaseCost;
                    if (j == 1 || path[i][j - 1] == 0)
                        insertionScore -= openCost;
                }

                S[i][j] = alignScore;
                path[i][j] = 0;

                if (deletionScore > S[i][j]) {
                    S[i][j] = deletionScore;
                    path[i][j] = 1;
                }

                if (insertionScore > S[i][j]) {
                    S[i][j] = insertionScore;
                    path[i][j] = 2;
                }
            }
        }
        return path;
    }

    // Task 4

    /**
     * scoreBlosum50 (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences of amino acids given in a input file using the Blosum50 matrix.
     */
    public static void scoreBlosum50 (String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = scoreBlosum50(s, t, 0, 0);

        List<String> editedSeqs = backtracking(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printScore(sEdition, tEdition);
    }

    // Task 5

    /**
     * affinePenalty (aFileName, openCost, increaseCost) : this method takes two additional parameters for
     * opening gap penalty and the increasing gap penalty and computes/displays one optimal alignment between
     * two sequences of amino acids given in a input file using the Blosum50 matrix.
     */
    public static void affinePenalty (String aFileName, float openCost, float increaseCost) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = scoreBlosum50(s, t, openCost, increaseCost);

        List<String> editedSeqs = backtracking(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printAffineScore(sEdition, tEdition, openCost, increaseCost);
    }

    /**
     * computeScoreBlosum50 (s, t, openCost, increaseCost) : this method computes the score of the alignment
     * of s and t using the Blosum 50 as the substitution matrix. It takes into account the gap penalties
     */
    public static float computeScoreBlosum50 (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        float score = 0F;
        float penalty = 0F;
        float sAux = 0F;
        float tAux = 0F;
        boolean sOpen = false;
        boolean tOpen = false;
        if (n > 0)
            score += Blosum50.getScore(s.charAt(0), t.charAt(0));
        for (int i = 1; i < n; i++) {
            if (s.charAt(i - 1) != '-' && s.charAt(i) == '-') {
                sOpen = true;
                sAux = openCost;
            }
            else if (s.charAt(i - 1) == '-' && s.charAt(i) != '-') {
                penalty += sAux;
                sOpen = false;
            }
            if (sOpen) sAux += increaseCost;

            if (t.charAt(i - 1) != '-' && t.charAt(i) == '-') {
                tOpen = true;
                tAux = openCost;
            }
            else if (t.charAt(i - 1) == '-' && t.charAt(i) != '-') {
                penalty += tAux;
                tOpen = false;
            }
            if (tOpen) tAux += increaseCost;


            score += Blosum50.getScore(s.charAt(i), t.charAt(i));
        }
        score -= penalty;

        return score;
    }

    /**
     * computeScoreBlosum50 (s, t, openCost, increaseCost) : this method computes the score of the alignment of
     * s and t using the Blosum 50 as the substitution matrix. It does not take into account the gap penalties
     */
    public static float computeScoreBlosum50 (String s, String t) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        float score = 0F;
        for (int i = 0; i < n; i++)
            score += Blosum50.getScore(s.charAt(i), t.charAt(i));

        return score;
    }

    /**
     * optimalLocal (s, t, openCost, increaseCost) : this method computes the optimal local alignment between
     * two sequences of letters, s and t, using dynamic programming (Smith-Waterman Algorithm). The array H
     * stores at H[i][j] the maximum score of the alignment so far and the array path encodes at path[i][j] the
     * choices taken.
     */

    private static List<float[][]> optimalLocal (String s, String t, float openCost, float increaseCost) {
        return null;
    }

    /**
     * backtracking (s, t, score, path) : does the backtracking of the array path to reconstruct one optimal
     * local alignment between the sequences s and t.
     */
    private static List<String> backtracking (String s, String t, float[][] score, float[][] path){
        return null;
    }


    //Task 6

    /**
     * optimalLocal (aFileName, openCost, increaseCost) : this method computes/displays one optimal local
     * alignment between two sequences of amino acids given in a input file using the Blosum50 matrix and
     * with affine gap penalty.
     */
    public static void optimalLocal (String aFileName, float openCost, float increaseCost) throws IOException {

    }

}
