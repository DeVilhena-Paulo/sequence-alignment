import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestBlast {

    private static void blastTest (String aFileName, Set<Integer> target, float th) throws IOException {
        Set<Integer> result = Blast.perfectMatches(aFileName, th);
        System.out.println ("Expected : " + target.toString() + "; Result : " + result.toString());
        if (result.containsAll(target))
            System.out.println (aFileName + " : PASSED");
        else
            System.out.println (aFileName + " : FAILED");
    }

    public static void main (String[] args) throws IOException {
        // perfectMatches
        System.out.println ("Testing Blast.perfectMatches method...");

        Set<Integer> result;
        Set<Integer> target;

        //Test 1
        target = new HashSet<>();
        target.add (1); target.add (2); target.add (11); target.add (12);
        blastTest ("Blast_Test1.txt", target, 0.9f);

        //Test 2
        target = new HashSet<>();
        target.add (17); target.add (53); target.add (54); target.add (40);
        blastTest ("Blast_Test2.txt", target, 0.9f);

        //Test 3
        target = new HashSet<>();
        target.add (5); target.add (50);
        blastTest ("Blast_Test3.txt", target, 0.88f);
    }

}
