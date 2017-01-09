import java.util.Set;

public class Display {

    private static String alignDisplay (String s, String t) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == t.charAt(i))
                result.append('|');
            else if (Blosum50.getScore(s.charAt(i), t.charAt(i)) >= 0)
                result.append(':');
            else
                result.append(' ');
        }

        return result.toString();
    }

    /*public static String computeAlignmentSeq (String s, String t) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        StringBuilder optimalAlignment = new StringBuilder();
        for (int i = 0; i < n; i++) {

            if (s.charAt(i) == t.charAt(i))
                optimalAlignment.append(s.charAt(i));

        }

        return optimalAlignment.toString();
    }*/

    /**
     * printEditionDistance (s, t): displays nicely one optimal alignment between sequences s and t
     */
    public static void printEditionDistance (String s, String t) { // Task 3
        System.out.println (s);
        System.out.println (alignDisplay(s, t));
        System.out.println (t);
        String aux = Computation.sequenceAlignment(s, t);
        int distEdit = s.length() - aux.length();
        System.out.println ("Optimal alignment : " + aux + "; Distance edition : " + distEdit + "\n");
    }

    public static void printBlosum50Similarity (String s, String t, float maxScore) { // Task 4: Substitution matrix
        System.out.println(s);
        System.out.println (alignDisplay(s, t));
        System.out.println(t);
        System.out.println("Score of alignment with Blosum50: " + maxScore + "; " + "\n");
    }

    public static void printAffineScore (String s, String t, float maxScore) { // Task 5: Affine Penalty
        System.out.println(s);
        System.out.println (alignDisplay(s, t));
        System.out.println(t);
        System.out.println("Score of alignment with affine penalty: " + maxScore + "; " + "\n");
    }

    public static void printLocal (String s, String t, float maxValue) { // Task 6: Local Optimal
        System.out.println (s);
        System.out.println (alignDisplay(s, t));
        System.out.println (t);
        System.out.println("Score of alignment with affine penalty: " + maxValue + " ; size: " + s.length() + "\n");
    }

    public static void printIntermediateBlastResults (String g, String t, float gScoreTh, int index1, int index2, int indI, int indJ, int k, float score) {
        System.out.println("--- *** ---");
        System.out.println("Match between (subword of g : " + g.substring(index1, index1 + k) + ") and (subword of t : " + t.substring(index2, index2 + k) + ")");
        System.out.println("Extended " + indI + " letters to the left and " + indJ + " to the right. Total length = " + (indI + k + indJ));
        System.out.println("Local alignment = " + t.substring(index2 - indI, index2) + " " + t.substring(index2, index2 + k) + " " + t.substring(index2 + k, index2 + k + indJ));
        System.out.println("Score of alignment = " + score + "; Threshold = " + gScoreTh);
    }

    public static void printPerfectMatchIndices (Set<Integer> indices, String t, int k) {
        for (Integer index : indices) {
            System.out.printf("%4d", index);
            System.out.println(" : " + t.substring(index, index + k));
        }
    }
}
