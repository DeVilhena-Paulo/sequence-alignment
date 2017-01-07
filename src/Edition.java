import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Edition {

    final static Charset ENCODING = StandardCharsets.UTF_8;


    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    /**
     * distance (s, t) : this method computes the minimum number of distance operations (delete a
     * letter, insert or transform a letter) to go from the sequence of letters s to the sequence t using
     * dynamic programming. The array d stores at d[i][j] the minimum number of distance operations to go
     * from the first i characters of the string s to the first j characters of the string t, being able
     * to manage the memoization process.
     */
    private static int[][] distance (String s, String t) {
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
     * distance (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences given in an input file.
     */
    public static String distance (String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = distance(s, t);

        List<String> editedSeqs = Alignment.backtracking(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printAlignment(sEdition, tEdition);

        return Display.computeAlignmentSeq(sEdition, tEdition);
    }
}
