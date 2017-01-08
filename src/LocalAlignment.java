import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalAlignment {

    final static Charset ENCODING = StandardCharsets.UTF_8;

    private static List<String> readFiles (String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllLines(p, ENCODING);
    }

    private static class Pair <ItemA, ItemB> {
        public ItemA first;
        public ItemB second;
        public Pair (ItemA first, ItemB second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * optimalLocal (s, t, openCost, increaseCost) : this method computes the optimal local alignment between
     * two sequences of letters, s and t, using the Smith-Waterman Algorithm modified to take into account the
     * Blosum50 score between couples with one hyphen and one amino acid as specified in the section 3.1. The
     * array H stores at H[i][j] the score of the optimal local alignment that ends with an alignment between
     * the i_th character of s and the j_th character of t and the array path encodes at path[i][j] the choices
     * taken so far.
     */

    private static Pair<int[][], float[][]> optimalLocal (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();
        float[][] H = new float[n + 1][m + 1];
        int[][] path = new int[n + 1][m + 1];

        for (int i = 0; i < n; i++)
            H[i][0] = 0F; // the optimal local between a sequence s and an empty sequence t is 0

        for (int j = 0; j < m; j++)
            H[0][j] = 0F; // the optimal local between a sequence s and an empty sequence t is 0

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {

                float alignScore = H[i - 1][j - 1] + Blosum50.getScore(s.charAt(i - 1), t.charAt(j - 1));

                float deletionScore = 0;
                for (int k = 1; k <= i; k++) {
                    float gapPenalty = openCost + k*increaseCost;
                    if (H[i - k][j] - gapPenalty > deletionScore)
                        deletionScore = H[i - k][j] - gapPenalty;
                }

                float insertionScore = 0;
                for (int k = 1; k <= j; k++) {
                    float gapPenalty = openCost + k*increaseCost;
                    if (H[i][j - k] - gapPenalty > insertionScore)
                        insertionScore = H[i][j - k] - gapPenalty;
                }

                H[i][j] = 0;
                path[i][j] = -1;

                if (alignScore > H[i][j]) {
                    H[i][j] = alignScore;
                    path[i][j] = 0;
                }

                if (deletionScore > H[i][j]) {
                    H[i][j] = deletionScore;
                    path[i][j] = 1;
                }

                if (insertionScore > H[i][j]) {
                    H[i][j] = insertionScore;
                    path[i][j] = 2;
                }

            }
        }

        return new Pair (path, H);
    }

    /**
     * findMaxIndeces (matrix): finds and returns the indeces of the maximum value of the matrix
     */
    private static Pair<Integer, Integer> findMaxIndeces (float[][] matrix){
        if(matrix.length < 1)
            throw new java.lang.IllegalArgumentException();

        if(matrix[0].length < 1)
            throw new java.lang.IllegalArgumentException();

        float max = matrix[0][0];
        int maxI = 0, maxJ = 0;

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                if (matrix[i][j] > max) {
                    max = matrix[i][j];
                    maxI = i;
                    maxJ = j;
                }

        return new Pair (maxI, maxJ);
    }

    /**
     * backtracking (s, t, path) : does the backtracking of the array path to reconstruct one optimal
     * local alignment between the sequences s and t.
     */
    private static Pair<String, String> traceBack (String s, String t, Pair<int[][], float[][]> matrices){
        int[][] path = matrices.first;
        float[][] H = matrices.second;

        Pair<Integer, Integer> indeces = findMaxIndeces(H);
        int i = indeces.first;
        int j = indeces.second;

        System.out.println("Max value = " + H[i][j]);

        StringBuilder sLocal = new StringBuilder();
        StringBuilder tLocal = new StringBuilder();


        while (H[i][j] != 0 && path[i][j] != -1) {
            if (path[i][j] == 0) {
                sLocal.append(s.charAt(i-1));
                tLocal.append(t.charAt(j-1));
                i--;
                j--;
            }
            else if (path[i][j] == 1){
                sLocal.append(s.charAt(i-1));
                tLocal.append('-');
                i--;
            }
            else{
                sLocal.append('-');
                tLocal.append(t.charAt(j-1));
                j--;
            }
        }

        return new Pair (sLocal.reverse().toString(), tLocal.reverse().toString());
    }


    //Task 6

    /**
     * optimalLocal (aFileName, openCost, increaseCost) : this method computes/displays one optimal local
     * alignment between two sequences of amino acids given in a input file using the Blosum50 matrix and
     * with affine gap penalty.
     */
    public static void optimalLocal (String aFileName, float openCost, float increaseCost) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        Pair<int[][], float[][]> matrices = optimalLocal(s, t, openCost, increaseCost);

        Pair<String, String> localSeqs = traceBack(s, t, matrices);

        String sLocal = localSeqs.first;
        String tLocal = localSeqs.second;

        Display.printLocal(sLocal, tLocal, openCost, increaseCost);
    }
}
