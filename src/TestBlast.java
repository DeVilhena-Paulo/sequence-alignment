import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestBlast {

    private static void blastTest (String aFileName, Set<Integer> target, float th, int k) throws IOException {
        Set<Integer> result = Blast.perfectMatches(aFileName, th, k);
        System.out.println ("Expected : " + target.toString() + "; Result : " + result.toString());
        if (result.containsAll(target))
            System.out.println (aFileName + " : PASSED");
        else
            System.out.println (aFileName + " : FAILED");
    }

    private static void localAlignTest (String aFileName, float th, float thl, int k) throws IOException {
        System.out.println ("" + aFileName + " :");
        Set<Blast.Triple<Integer, Integer, Float>> result = Blast.highScoreAlignments(aFileName, th, thl, k);
        for (Blast.Triple<Integer, Integer, Float> triple : result) {
            System.out.println("index: " + triple.first + "; length: " + triple.second + "; score: " + triple.third + ";");
        }
        System.out.println ();
    }

    public static void main (String[] args) throws IOException {
        // perfectMatches
        System.out.println ("--------------------------------------");
        System.out.println ("Testing Blast.perfectMatches method...");

        Set<Integer> result;
        Set<Integer> target;

        //Test 1
        target = new HashSet<>();
        target.add (1); target.add (2); target.add (11); target.add (12);
        blastTest ("Blast_Test1.txt", target, 0.9f, 4);

        //Test 2
        target = new HashSet<>();
        target.add (17); target.add (53); target.add (54); target.add (40);
        blastTest ("Blast_Test2.txt", target, 0.9f, 4);

        //Test 3
        target = new HashSet<>();
        target.add (5); target.add (50);
        blastTest ("Blast_Test3.txt", target, 0.88f, 4);

        // highScoreAlignments
        System.out.println ("\n-------------------------------------------");
        System.out.println (  "Testing Blast.highScoreAlignments method...");

        //Test 1
        localAlignTest("Blast_Test1.txt", 0.9f, 0.1f, 4);

        //Test 2
        localAlignTest("Blast_Test2.txt", 0.9f, 0.1f, 4);

        //Test 3
        localAlignTest("Blast_Test3.txt", 0.88f, 0.1f, 4);
    }

}
