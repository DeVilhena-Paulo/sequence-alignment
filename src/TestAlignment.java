import java.io.IOException;

public class TestAlignment {

    private static void lcsTest (String aFileName, int target) throws IOException {
        String result = Alignment.lcs(aFileName);
        if (result.length() != target)
            System.out.println (aFileName + " : FAILED");
        else
            System.out.println (aFileName + " : PASSED");
    }

    public static void main (String[] arg) throws IOException {

        // Alignment.lcs
        System.out.println ("Testing Alignment.lcs method...");

        //Test 1
        lcsTest ("SubSeq_Res52.txt", 52);

        //Test 2
        lcsTest ("SubSeq_Res320.txt", 320);

        //Test 3
        lcsTest ("SubSeq_Res3275.txt", 3275);

        // scoreBlosum50
        System.out.println ("\nTesting Alignment.scoreBlosum50 method...");

        //Test 1
        Alignment.scoreBlosum50("OptSeq_Res8.txt");

        //Test 2
        Alignment.scoreBlosum50("OptSeq_Res10.txt");

        //Test 3
        Alignment.scoreBlosum50("OptSeq_Res13.txt");

        //Test 4
        Alignment.scoreBlosum50("OptSeq_Res.txt");

        // affinePenalty
        System.out.println ("Testing Alignment.affinePenalty method...");

        //Test 1
        Alignment.affinePenalty("OptSeq_Res8.txt", 10, 1);

        //Test 2
        Alignment.affinePenalty("OptSeq_Res10.txt", 10, 1);

        //Test 3
        Alignment.affinePenalty("OptSeq_Res13.txt", 10, 1);

        //Test 4
        Alignment.affinePenalty("OptSeq_Res.txt", 10, 1);

        //Test 5
        Alignment.affinePenalty("CartsCat.txt", 10, 1);

    }
}
