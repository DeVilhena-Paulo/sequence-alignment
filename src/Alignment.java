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

    private static class Pair <ItemA, ItemB> {
        public ItemA first;
        public ItemB second;
        public Pair (ItemA first, ItemB second) {
            this.first = first;
            this.second = second;
        }
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
     * traceBack : does the traceBack of the array path to reconstruct one optimal
     * alignment between the sequences s and t
     */
    public static List<String> traceBack (String s, String t, int[][] path) {
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
     * maxinizeBlosum50Score (s, t) : this method computes the maximum score of the alignment between two sequences
     * of letters, s and t, using dynamic programming. The array S stores at S[i][j] the maximum score of the
     * alignment between the first i characters of the string s to the first j characters of the string t, being
     * able to manage the memoization process.
     */
    private static int[][] maxinizeBlosum50Score  (String s, String t) {
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

                float insertionScore = S[i][j - 1] +  Blosum50.getScore(t.charAt(j - 1), '-');

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
     * blosum50Similarity (aFileName) : this methods computes and displays one optimal alignment between two
     * sequences of amino acids given in a input file using the Blosum50 matrix.
     */
    public static void blosum50Similarity (String aFileName) throws IOException {
        List<String> lines = readFiles(aFileName);

        if (lines.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String s = lines.get(0);
        String t = lines.get(1);
        int[][] path = maxinizeBlosum50Score (s, t);

        List<String> editedSeqs = traceBack(s, t, path);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printBlosum50Similarity(sEdition, tEdition);
    }

    /**
     * maxinizeBlosum50Score (s, t, openCost, increaseCost) : this method computes the semi-global (no penalty
     * for end gaps) alignment between two sequences of letters, s and t, using the Gotoh's Algorithm with the
     * Blosum50 scoring matrix. The matrix D stores at D[i][j] scores the optimal semi-global alignment that
     * ends with an alignment between the i_th character of s and the j_th character of t. The matrix P stores
     * at P[i][j] scores the optimal semi-global alignment that ends with an alignment between the i_th character
     * of s and a hyphen in t.The matrix Q stores at Q[i][j] scores the optimal semi-global alignment that ends
     * with an alignment between a hyphen in s and the j_th character of t. The method returns traceBackD, traceBackP
     * and traceBackQ which encode the choices taken so far.
     */
    public static Pair<List<int[][]>, Pair<Integer, Integer>> maxinizeBlosum50Score  (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();

        float[][] D = new float [n + 1][m + 1];
        float[][] P = new float [n + 1][m + 1];
        float[][] Q = new float [n + 1][m + 1];

        int[][] traceBackD = new int [n + 1][m + 1];
        int[][] traceBackP = new int [n + 1][m + 1];
        int[][] traceBackQ = new int [n + 1][m + 1];

        D[0][0] = 0F;

        for (int i = 1; i < n + 1; i++) {
            D[i][0] = 0F;
            //Q[i][0] = - Float.MAX_VALUE;
        }

        for (int j = 1; j < m + 1; j++) {
            D[0][j] = 0F;
            //P[0][j] = - Float.MAX_VALUE;
        }


        for (int i = 1; i < n + 1; i ++)
            for (int j = 1; j < m + 1; j++) {
                // calculate P
                float tOpenGapCost = D[i - 1][j] - (openCost + increaseCost);
                float tExtendGapCost = P[i - 1][j] - increaseCost;

                P[i][j] = tOpenGapCost;
                traceBackP[i][j] = 1; // first option taken

                if (i > 1 && tExtendGapCost > P[i][j]) {
                    P[i][j] = tExtendGapCost;
                    traceBackP[i][j] = 2; // second option taken
                }

                // calculate Q
                float sOpenGapCost = D[i][j - 1] - (openCost + increaseCost);
                float sExtendGapCost = Q[i][j - 1] - increaseCost;

                Q[i][j] = sOpenGapCost;
                traceBackQ[i][j] = 1;

                if (j > 1 && sExtendGapCost > Q[i][j]) {
                    P[i][j] = sExtendGapCost;
                    traceBackQ[i][j] = 2;
                }

                // calculate D
                float continueAlignCost = D[i - 1][j - 1] + Blosum50.getScore(s.charAt(i - 1), t.charAt(j - 1));
                float tTrailGapCost = P[i][j];
                float sTrailGapCost = Q[i][j];

                D[i][j] = continueAlignCost;
                traceBackD[i][j] = 0;

                if (tTrailGapCost > D[i][j]) {
                    D[i][j] = tTrailGapCost;
                    traceBackD[i][j] = 1;
                }

                if (sTrailGapCost > D[i][j]) {
                    D[i][j] = sTrailGapCost;
                    traceBackD[i][j] = 2;
                }
            }


        float maxScore;
        int indMax;
        Pair<Integer, Integer> pair;

        if (n < m) {
            maxScore = D[n][0];
            indMax = 0;
            for (int j = 1; j < m + 1; j++)
                if (D[n][j] > maxScore) {
                    maxScore = D[n][j];
                    indMax = j;
                }
            pair = new Pair<>(n, indMax);
        }
        else {
            maxScore = D[0][m];
            indMax = 0;
            for (int i = 1; i < n + 1; i++)
                if (D[i][m] > maxScore) {
                    maxScore = D[i][m] ;
                    indMax = i;
                }
            pair = new Pair<>(indMax, m);
        }

        System.out.println("Max at " + indMax + ", value = " + maxScore);

        List<int[][]> result = new LinkedList<int[][]>();
        result.add(traceBackD);
        result.add(traceBackP);
        result.add(traceBackQ);

        return new Pair<>(result, pair);
    }

    /**
     * gotohTraceBack : does the traceBack of the matrices trD, trP and trQ to reconstruct one optimal
     * semi-global alignment between the sequences s and t
     */
    private static List<String> gotohTraceBack (String s, String t, int indI, int indJ, List<int[][]> traceBackMatrices) {
        if (traceBackMatrices.size() != 3)
            throw new java.lang.IllegalArgumentException();

        int[][] trD = traceBackMatrices.get(0);
        int[][] trP = traceBackMatrices.get(1);
        int[][] trQ = traceBackMatrices.get(2);

        int n = s.length();
        int m = t.length();

        StringBuilder sEdition = new StringBuilder();
        StringBuilder tEdition = new StringBuilder();

        int i = n, j = m;

        if (indI == n)
            while (j > indJ) {
                tEdition.append(t.charAt(j - 1));
                sEdition.append('-');
                j--;
            }

        else
            while (i > indI) {
                sEdition.append(s.charAt(i - 1));
                tEdition.append('-');
                i--;
            }

        char state = 'D'; // we start at matrix D

        while (i > 0 && j > 0) {
            switch (state) {
                case 'D':
                    if (trD[i][j] == 0) {
                        sEdition.append(s.charAt(i - 1));
                        tEdition.append(t.charAt(j - 1));
                        i--;
                        j--;
                    }
                    else if (trD[i][j] == 1) // Deleting was chosen
                        state = 'P';
                    else // Inserting was chosen
                        state = 'Q';

                    break;

                case 'P':
                    if (trP[i][j] == 1) {
                        sEdition.append(s.charAt(i - 1));
                        tEdition.append('-');
                        i--;
                        state = 'D';
                    }
                    else { // Inserting was chosen
                        sEdition.append(s.charAt(i - 1));
                        tEdition.append('-');
                        i--;
                    }
                    break;

                case 'Q':
                    if (trQ[i][j] == 1) { // Deleting was chosen
                        state = 'D';
                        tEdition.append(t.charAt(j - 1));
                        sEdition.append('-');
                        j--;
                    }
                    else { // Inserting was chosen
                        tEdition.append(t.charAt(j - 1));
                        sEdition.append('-');
                        j--;
                    }
                    break;

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

        Pair<List<int[][]>, Pair<Integer, Integer>> result = maxinizeBlosum50Score (s, t, openCost, increaseCost);
        List<int[][]> traceBackMatrices = result.first;
        Pair<Integer, Integer> indices = result.second;

        List<String> editedSeqs = gotohTraceBack(s, t, indices.first, indices.second, traceBackMatrices);

        if (editedSeqs.size() != 2)
            throw new java.lang.IllegalArgumentException();

        String sEdition = editedSeqs.get(0);
        String tEdition = editedSeqs.get(1);

        Display.printAffineScore(sEdition, tEdition, openCost, increaseCost);
    }

}
