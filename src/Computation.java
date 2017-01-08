
public class Computation {

    /**
     * computeScoreBlosum50 (s, t, openCost, increaseCost) : this method computes the score of the alignment of
     * s and t using the Blosum 50 as the substitution matrix. It does not take into account the gap penalties
     */
    public static float blosum50Score (String s, String t) {
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

    /**
     * blsum50Score (s, t, openCost, increaseCost) : this method computes the score of the alignment
     * of s and t using the Blosum 50 as the substitution matrix. It takes into account the gap penalties
     */
    public static float blosum50Score (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        float score = 0F;
        for (int i = 0; i < n; i++)
            if (s.charAt(i) != '-' && t.charAt(i) != '-')
                score += Blosum50.getScore(s.charAt(i), t.charAt(i));

        score -= gapPenalty(s, t, openCost, increaseCost);

        return score;
    }

    public static float gapPenalty (String s, String t, float openCost, float increaseCost) {
        int n = s.length();
        int m = t.length();

        if (n != m) {
            System.out.println ("Error: string sizes do not match");
            throw new java.lang.IllegalArgumentException();
        }

        float penalty = 0F;
        float sAux = 0F;
        float tAux = 0F;
        boolean sOpen = false;
        boolean tOpen = false;

        for (int i = 1; i < n; i++) {
            if (s.charAt(i - 1) != '-' && s.charAt(i) == '-') {
                sOpen = true;
                sAux = openCost;
            }
            else if (s.charAt(i - 1) == '-' && s.charAt(i) != '-') {
                penalty += sAux;
                sOpen = false;
            }
            if (sOpen) sAux += increaseCost;

            if (t.charAt(i - 1) != '-' && t.charAt(i) == '-') {
                tOpen = true;
                tAux = openCost;
            }
            else if (t.charAt(i - 1) == '-' && t.charAt(i) != '-') {
                penalty += tAux;
                tOpen = false;
            }
            if (tOpen) tAux += increaseCost;
        }

        return penalty;
    }

    public static String sequenceAlignment (String s, String t) {
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
