package ImageHidingCode;

import java.util.Arrays;
import java.util.Random;

public class WeakCollisionResistanceViolation {
	public static void main(String[] args) {
        // Example message divided into 3 blocks (160 bits each)
        String[] xBlocks = {
            "1100110011001100110011001100110011001100110011001100110011001100", // M1
            "1010101010101010101010101010101010101010101010101010101010101010", // M2
            "0110011001100110011001100110011001100110011001100110011001100110"  // M3
        };

        // Compute the hash of x
        String hashX = computeHash(xBlocks);

        // Find a different message y with the same hash
        String[] yBlocks = findCollision(xBlocks, hashX);
        System.out.println("Original Message (x): " + String.join(", ", xBlocks));
        System.out.println("Collision Message (y): " + String.join(", ", yBlocks));
        System.out.println("Hash of x: " + hashX);
        System.out.println("Hash of y: " + computeHash(yBlocks));
    }

    public static String computeHash(String[] blocks) {
        String result = blocks[0];
        for (int i = 1; i < blocks.length; i++) {
            result = xorBinaryStrings(result, blocks[i]);
        }
        return result;
    }

    public static String[] findCollision(String[] xBlocks, String hashX) {
        String[] yBlocks = Arrays.copyOf(xBlocks, xBlocks.length);
        Random random = new Random();

        // Modify one block to create a collision
        int blockIndex = random.nextInt(yBlocks.length);
        yBlocks[blockIndex] = xorBinaryStrings(yBlocks[blockIndex], hashX);

        return yBlocks;
    }

    private static String xorBinaryStrings(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

}
