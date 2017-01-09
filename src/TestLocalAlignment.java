import java.io.IOException;

public class TestLocalAlignment {
    public static void main (String[] args) throws IOException {
        // optimalLocal
        System.out.println ("Testing LocalAlignment.optimalLocal method...");

        //Test 1
        LocalAlignment.optimalLocal("LocSeq_Res6.txt", 10, 1);

        //Test 2
        LocalAlignment.optimalLocal("SubSeq_Res52.txt", 10, 1);
        String s1 = "CGGCTATATTTAGCAC-ACACAATTTTTAGGTCGCACGATCGGGATGGCGGCGCGCGATCAACAGCCTGCACACTTCTAAG-GAAAATAGTCA";
        String s2 = "CGAACCTGTTTCGAAAGGCTCAAGT----GG-C-CTCTATC-------CTACACG------ACAGACCACCCAGAACAAAGAGGATGTGGCCA";
        Display.printLocal(s1, s2, Computation.blosum50Score(s1, s2, 10, 1));

        //Test 3
        LocalAlignment.optimalLocal("LocSeq_Res5.txt", 10, 1);
    }
}
