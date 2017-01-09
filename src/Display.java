
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
        System.out.println ("Optimal alignment : " + aux + "; Distance edition : " + distEdit);
    }

    public static void printBlosum50Similarity (String s, String t) { // Task 4: Substitution matrix
        System.out.println(s);
        System.out.println (alignDisplay(s, t));
        System.out.println(t);
        System.out.println("Score of alignment with Blosum50: " + Computation.blosum50Score(s, t) + "\n");
    }

    public static void printAffineScore (String s, String t, float openCost, float increaseCost) { // Task 5: Affine Penalty
        System.out.println(s);
        System.out.println (alignDisplay(s, t));
        System.out.println(t);
        System.out.println("Score of alignment with affine penalty: " + Computation.blosum50Score(s, t, openCost, increaseCost) + "\n");
    }

    public static void printLocal (String s, String t, float maxValue) { // Task 6: Local Optimal
        System.out.println (s);
        System.out.println (alignDisplay(s, t));
        System.out.println (t);
        System.out.println("Score of alignment with affine penalty: " + maxValue + " ; size: " + s.length() + "\n");
    }

    public static void printIntermediateBlastResults (int index1, int index2, int indI, int indJ, float score) {
        System.out.println("indeces: (" + index1 + ", " + index2 + "); indI: " + indI + "; indJ: " + indJ + "; score: " + score );
    }
}
