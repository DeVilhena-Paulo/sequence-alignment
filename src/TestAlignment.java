import java.io.IOException;

public class TestAlignment {

    private static void lcsTest (String aFileName, int target) throws IOException {
        String result = Alignment.lcs(aFileName);
        if (result.length() != target)
            System.out.println (aFileName + " : FAILED");
        else
            System.out.println (aFileName + " : PASSED");
    }

    private static void distanceEditionTest (String aFileName, int target) throws IOException {
        String result = Alignment.distanceEdition(aFileName);
        if (result.length() != target)
            System.out.println (aFileName + " : FAILED\n");
        else
            System.out.println (aFileName + " : PASSED\n");
    }

    public static void main (String[] arg) throws IOException {

        String result;

        // lcs
        System.out.println ("Testing lcs method...");

        //Test 1
        lcsTest ("SubSeq_Res52.txt", 52);

        //Test 2
        lcsTest ("SubSeq_Res320.txt", 320);

        //Test 3
        lcsTest ("SubSeq_Res3275.txt", 3275);

        // distanceEdition
        System.out.println ("\nTesting distanceEdition method...");

        //Test 1
        distanceEditionTest("OptSeq_Res8.txt", 8);

        // Test 2
        distanceEditionTest("OptSeq_Res10.txt", 10);

        // Test 3
        distanceEditionTest("OptSeq_Res13.txt", 13);

        // scoreAlignment
        System.out.println ("Testing scoreAlignment method...");

        //Test 1
        Alignment.scoreAlignment("OptSeq_Res8.txt");

        //Test 2
        Alignment.scoreAlignment("OptSeq_Res10.txt");

        //Test 3
        Alignment.scoreAlignment("OptSeq_Res13.txt");

        // affinePenalty
        System.out.println ("Testing affinePenalty method...");

        //Test 1
        Alignment.affinePenalty("OptSeq_Res8.txt", 10, 1);

        //Test 2
        Alignment.affinePenalty("OptSeq_Res10.txt", 10, 1);

        //Test 3
        Alignment.affinePenalty("OptSeq_Res13.txt", 10, 1);

    }
}
