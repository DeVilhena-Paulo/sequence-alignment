import java.io.IOException;

public class TestLocalAlignment {
    public static void main (String[] args) throws IOException {
        // optimalLocal
        System.out.println ("Testing LocalAlignment.optimalLocal method...");

        //Test 1
        LocalAlignment.optimalLocal("LocSeq_Res6.txt", 10, -4);
        System.out.println("Matlab score = " + Alignment.computeScoreBlosum50("GMASGYD", "GKAS-YD", 10, -4));

        //Test 2
        LocalAlignment.optimalLocal("SubSeq_Res52.txt", 10, 1);
        Display.printLocal("CACACAATTTTTAGGTCGCACGATCGGGATGGCGGCGCGCGATCA--ACAGCCTGCACACTTCTAAG-GAAAATAGTCA", "CGAAC-CTGTTTCGAAAG-GC--TCAAG-TGGCCTCTATCCTACACGACAGACCACCCAGAACAAAGAGGATGTGGCCA", 10, 1);

        //Test 3
        LocalAlignment.optimalLocal("LocSeq_Res5.txt", 10, 1);
    }
}
