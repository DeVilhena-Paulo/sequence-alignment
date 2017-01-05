import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Subsequence {

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
     * lcs (aFileName) : this methods computes the longest common subsequence between two sequences of letters
     * given in a file which he name is stored in the String aFileName
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
}
