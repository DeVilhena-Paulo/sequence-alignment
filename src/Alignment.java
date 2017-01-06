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
     * distanceEdition (s, t) : this method computes the minimum number of edition operations (delete a
     * letter, insert or transform a letter) to go from the sequence of letters s to the sequence t using
     * dynamic programming. The array d stores at d[i][j] the minimum number of edition operations to go
     * from the first i characters of the string s to the first j characters of the string t, being able
     * to manage the memoization process.
     */
    private static int[][] distanceEdition (String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] d = new int [n + 1][m + 1];
        int[][] path = new int [n + 1][m + 1];

        for (int i = 0; i < n + 1; i++)
            d[i][0] = i; // i deletions to pass from the first i characters of s to an empty string

        for (int j = 0; j < m + 1; j++)
            d[0][j] = j; // j insertions to pass from an empty string to the first j characters of t

        for (int i = 1; i < n + 1; i++)
            for (int j = 1; j < m + 1; j++) {

                d[i][j] = d[i - 1][j - 1];
                path[i][j] = 0;

                if (s.charAt(i - 1) != t.charAt(j - 1))
                    d[i][j] += 1;

                if (d[i - 1][j] + 1 < d[i][j]) {
                    d[i][j] = d[i - 1][j] + 1;
                    path[i][j] = 1;
                }
                if (d[i][j - 1] + 1 < d[i][j]) {
                    d[i][j] = d[i][j - 1] + 1;
                    path[i][j] = 2;
                }
            }
        return path;
    }



    // Task 3

    /**
     * distanceEdition (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences given in an input file.
     */
    public static String distanceEdition (String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = distanceEdition(s, t);

        List<String> editedSeqs = optimalAlignmentBacktrack(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printAlignment(sEdition, tEdition);

        return Display.computeAlignment(sEdition, tEdition);
    }

    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    /**
     * optimalAlignmentBacktrack : does the backtracking of the array path to reconstruct one optimal
     * alignment between the sequences s and t
     */
    private static List<String> optimalAlignmentBacktrack (String s, String t, int[][] path) {
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

        List<String> editedSeqs = new LinkedList<>();
        editedSeqs.add(sEdition.reverse().toString());
        editedSeqs.add(tEdition.reverse().toString());

        return editedSeqs;
    }


    /**
     * scoreAlignment (s, t) : this method computes the maximum score of the alignment between two sequences
     * of letters, s and t, using dynamic programming. The array S stores at S[i][j] the maximum score of the
     * alignment between the first i characters of the string s to the first j characters of the string t, being
     * able to manage the memoization process.
     */
    private static int[][] scoreAlignment (String s, String t, float openCost, float increaseCost) {
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

                float alignCost = S[i - 1][j - 1] + Blosum50.getScore(s.charAt(i - 1), t.charAt(j - 1));

                float deletionCost = S[i - 1][j] +  Blosum50.getScore(s.charAt(i - 1), '-');
                if (j != m) { // if I'm opening a gap in the end of the sequence t, the penalty does not count
                    deletionCost += increaseCost;
                    if (i == 1 || path[i - 1][j] == 0)
                        deletionCost += openCost;
                }

                float insertionCost = S[i][j - 1] +  Blosum50.getScore(t.charAt(j - 1), '-');
                if (i != n) { // if I'm opening a gap in the end of the sequence s, the penalty does not count
                    insertionCost += increaseCost;
                    if (j == 1 || path[i][j - 1] == 0)
                        insertionCost += openCost;
                }

                S[i][j] = alignCost;
                path[i][j] = 0;

                if (deletionCost > S[i][j]) {
                    S[i][j] = deletionCost;
                    path[i][j] = 1;
                }

                if (insertionCost > S[i][j]) {
                    S[i][j] = insertionCost;
                    path[i][j] = 2;
                }
            }
        }
        return path;
    }

    // Task 4

    /**
     * scoreAlignment (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences of amino acids given in a input file using the Blosum50 matrix.
     */
    public static void scoreAlignment (String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = scoreAlignment(s, t, 0, 0);

        List<String> editedSeqs = optimalAlignmentBacktrack(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printScore(sEdition, tEdition);
    }

    // Task 5

    /**
     * scoreAlignment (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences of amino acids given in a input file using the Blosum50 matrix.
     */
    public static void affinePenalty (float openCost, float increaseCost, String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        /*int[][] path = scoreAlignment(s,t);

        List<String> editedSeqs = optimalAlignmentBacktrack(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printScore(sEdition, tEdition);*/
    }

}
