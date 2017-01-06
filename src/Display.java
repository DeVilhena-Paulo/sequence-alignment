import java.util.LinkedList;
import java.util.List;

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
}
