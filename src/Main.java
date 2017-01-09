import java.io.IOException;

public class Main {

    public static void  task2 (String aFileName) throws IOException {
        System.out.println("Task 2 : Computation of one possible longest common subsequence (LCS)");
        System.out.println("Test file : " + aFileName);
        String lcs = Alignment.lcs(aFileName);
        System.out.println(lcs + "; size : " + lcs.length());
    }

    public static void  task3 (String aFileName) throws IOException {
        System.out.println("\nTask 3 : Minimizing the edition distance between sequences");
        System.out.println("Test file : " + aFileName);
        Edition.distance(aFileName);
    }

    public static void  task4 (String aFileName) throws IOException {
        System.out.println("\nTask 4 : Maximizing score of alignment with substitution matrix Blosum50");
        System.out.println("Test file : " + aFileName);
        Alignment.blosum50Similarity(aFileName);
    }

    public static void  task5 (String aFileName) throws IOException {

    }

    public static void  task6 (String aFileName) throws IOException {

    }

    public static void  task7 (String aFileName) throws IOException {

    }

    public static void  task8 (String aFileName) throws IOException {

    }

    public static void  main (String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            String aFileName = args[i];

            task2(aFileName);

            task3(aFileName);

            task4(aFileName);

        }
    }
}
