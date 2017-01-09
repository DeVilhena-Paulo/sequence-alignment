import java.io.IOException;

public class Main {

    public static void  task2 (String aFileName) throws IOException {
        System.out.println("Task 2 : Computation of one possible longest common subsequence (LCS)");
        String lcs = Alignment.lcs(aFileName);
        System.out.println(lcs + "; size : " + lcs.length() + "\n");
    }

    public static void  task3 (String aFileName) throws IOException {
        System.out.println("\nTask 3 : Minimizing the edition distance between sequences");
        Edition.distance(aFileName);
    }

    public static void  task4 (String aFileName) throws IOException {
        System.out.println("\nTask 4 : Maximizing score of alignment with substitution matrix Blosum50");
        Alignment.blosum50Similarity(aFileName);
    }

    public static void  task5 (String aFileName) throws IOException {
        System.out.println("\nTask 5 : Maximizing score of semi global alignment (gaps at the beginning or at the end have no cost) with substitution matrix Blosum50 and addine gap penalty");
        float openCost = 10;
        float increaseCoest = 1;
        Alignment.affinePenalty(aFileName, openCost, increaseCoest);
    }

    public static void  task6 (String aFileName) throws IOException {
        System.out.println("\nTask 6 : Optimal local alignment");
        float openCost = 10;
        float increaseCoest = 1;
        LocalAlignment.optimalLocal(aFileName, openCost, increaseCoest);
    }

    public static void  task7 (String aFileName) throws IOException {
        System.out.println("\nTask 7 : Indices of perfect matches between words from Seeds and subwords of t");
        float th = 0.9f;
        int k = 4;
        Blast.perfectMatches(aFileName, th, k);
    }

    public static void  task8 (String aFileName) throws IOException {
        System.out.println("\nTask 8 : Blast heuristic to solve the local alignment problem");
        float th = 0.9f;
        float thl = 0.1f;
        int k = 4;
        Blast.highScoreAlignments(aFileName, th, thl , k);
    }

    public static void  main (String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            String aFileName = args[i];
            System.out.println("Test file : " + aFileName);

            task2(aFileName);

            task3(aFileName);

            task4(aFileName);

            task5(aFileName);

            task6(aFileName);

            task7(aFileName);

            task8(aFileName);

        }
    }
}
