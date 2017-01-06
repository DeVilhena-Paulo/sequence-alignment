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
     * lcs (aFileName) : this methods computes the longest common subsequence between two sequences of
     * letters given in a file which he name is stored in the String aFileName.
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
     * from the fisrt i characters of the string s to the first j characters of the string t and by that
     * it manages the memoization process.
     */
    public static int[][] distanceEdition (String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] d = new int [n + 1][m + 1];
        int[][] path = new int [n + 1][m + 1];

        for (int i = 0; i < n + 1; i++)
            d[i][0] = i; // i deletions to pass from the first i characters of s to an empty string

        for (int j = 0; j < m + 1; j++)
            d[0][j] = j; // j insertions to pass from an empty string to the first j characters of t

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {

                d[i][j] = d[i-1][j-1];
                path[i][j] = 0;

                if (s.charAt(i-1) != t.charAt(j-1)) {
                    d[i][j] += 1;
                }
                if (d[i-1][j] + 1 < d[i][j]) {
                    d[i][j] = d[i - 1][j] + 1;
                    path[i][j] = 1;
                }
                if (d[i][j-1] + 1 < d[i][j]) {
                    d[i][j] = d[i][j - 1] + 1;
                    path[i][j] = 2;
                }
            }
        }
        return path;
    }

    // Task 3

    /**
     * distanceEdition (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences given in an input file which he name is stored in the String aFileName.
     */
    public static String distanceEdition (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        List<String> lines = Files.readAllLines(p, ENCODING);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);

        int n = s.length();
        int m = t.length();
        int[][] path = distanceEdition(s,t);

        StringBuilder sEdition = new StringBuilder();
        StringBuilder tEdition = new StringBuilder();
        StringBuilder optimalAlignment = new StringBuilder();

        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (path[i][j] == 0) {

                if (s.charAt(i-1) == t.charAt(j-1))
                    optimalAlignment.append(s.charAt(i-1));

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

        // Displaying: work later
        // System.out.println (sEdition.reverse());
        // System.out.println (tEdition.reverse());
        // System.out.println (optimalAlignment.reverse());

        return optimalAlignment.reverse().toString();
    }

}
