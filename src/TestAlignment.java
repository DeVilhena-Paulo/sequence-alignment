import java.io.IOException;

public class TestAlignment {

    public static void main (String[] arg) throws IOException {

        String result;

        // lcs
        System.out.println ("Testing lcs method...");

        //Test 1
        result = Alignment.lcs("SubSeq_Res52.txt");
        if (result.length() != 52)
            System.out.println ("SubSeq_Rec52: FAILED");
        else
            System.out.println ("SubSeq_Rec52: PASSED");

        //Test 2
        result = Alignment.lcs("SubSeq_Res320.txt");
        if (result.length() != 320)
            System.out.println ("SubSeq_Rec320: FAILED");
        else
            System.out.println ("SubSeq_Rec320: PASSED");

        //Test 3
        result = Alignment.lcs("SubSeq_Res3275.txt");
        if (result.length() != 3275)
            System.out.println ("SubSeq_Rec3275: FAILED");
        else
            System.out.println ("SubSeq_Rec3275: PASSED");

        // distanceEdition
        System.out.println ("\nTesting distanceEdition method...");

        //Test 1
        result = Alignment.distanceEdition("OptSeq_Res8.txt");
        if (result.length() != 8)
            System.out.println ("OptSeq_Res8: FAILED");
        else
            System.out.println ("OptSeq_Res8: PASSED");

        // scoreAlignment
        System.out.println ("\nTesting scoreAlignment method...");

        //Test 1
        Alignment.scoreAlignment("OptSeq_Res8.txt");
    }
}
