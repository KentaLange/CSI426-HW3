package ImageHidingCode;

import java.util.Arrays;

public class OneWayViolation {
    public static void main(String[] args) {
        // Example message divided into 3 blocks (160 bits each)
        String[] blocks = {
            "1100110011001100110011001100110011001100110011001100110011001100", // M1
            "1010101010101010101010101010101010101010101010101010101010101010", // M2
            null // M3 is missing
        };

        // Compute the hash (XOR of all blocks)
        String hash = "0110011001100110011001100110011001100110011001100110011001100110"; // h(M)

        // Recover the missing block (M3)
        String recoveredBlock = recoverMissingBlock(blocks, hash);
        System.out.println("Recovered Block (M3): " + recoveredBlock);
    }

    public static String recoverMissingBlock(String[] blocks, String hash) {
        String result = hash;
        for (String block : blocks) {
            if (block != null) {
                result = xorBinaryStrings(result, block);
            }
        }
        return result;
    }

    private static String xorBinaryStrings(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }
}