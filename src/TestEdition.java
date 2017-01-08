import java.io.IOException;

public class TestEdition {

    private static void distanceEditionTest (String aFileName, int target) throws IOException {
        String result = Edition.distance(aFileName);
        if (result.length() != target)
            System.out.println (aFileName + " : FAILED\n");
        else
            System.out.println (aFileName + " : PASSED\n");
    }

    public static void main (String[] args) throws IOException {
        // Edition.distance
        System.out.println ("\nTesting Edition.distance method...");

        //Test 1
        distanceEditionTest("OptSeq_Res8.txt", 8);
        // Test 2
        distanceEditionTest("OptSeq_Res10.txt", 10);
        // Test 3
        distanceEditionTest("OptSeq_Res13.txt", 13);
    }
}
