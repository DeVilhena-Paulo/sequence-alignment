import java.io.IOException;

public class TestSubsequence {

    public static void main (String[] arg) throws IOException {

        String result;

        //Test 1
        result = Subsequence.lcs("SubSeq_Res52.txt");
        if (result.length() != 52)
            System.out.println ("SubSeq_Rec52: FAILED");
        else
            System.out.println ("SubSeq_Rec52: PASSED");

        //Test 2
        result = Subsequence.lcs("SubSeq_Res320.txt");
        if (result.length() != 320)
            System.out.println ("SubSeq_Rec320: FAILED");
        else
            System.out.println ("SubSeq_Rec320: PASSED");

        //Test 3
        result = Subsequence.lcs("SubSeq_Res3275.txt");
        if (result.length() != 3275)
            System.out.println ("SubSeq_Rec3275: FAILED");
        else
            System.out.println ("SubSeq_Rec3275: PASSED");
    }
}
