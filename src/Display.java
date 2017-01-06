
public class Display {

    /**
     * printAlignment (s, t): displays nicely one optimal alignment between sequences s and t
     */
    public static void printAlignment (String s, String t) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        StringBuilder alignDisplay = new StringBuilder();
        StringBuilder optimalAlignment = new StringBuilder();
        for (int i = 0; i < n; i++) {

            if (s.charAt(i) == t.charAt(i)) {
                alignDisplay.append('|');
                optimalAlignment.append(s.charAt(i));
            }
            else
                alignDisplay.append(' ');
        }

        System.out.println (s);
        System.out.println (alignDisplay.toString());
        System.out.println (t);
        String aux = optimalAlignment.toString();
        System.out.println ("Optimal alignment : " + aux + "; Size : " + aux.length());
    }

    public static String computeAlignment (String s, String t) {
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
    }

    public static float computeAlignmentScore (String s, String t) {
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

    public static void printScore (String s, String t) {
        System.out.println(s);
        System.out.println(t);
        System.out.println("Score of aligment : " + computeAlignmentScore(s, t));
    }
}
