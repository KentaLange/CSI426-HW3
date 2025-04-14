package ImageHidingCode;

import java.util.Arrays;

public class StrongCollisionResistanceViolation {
    public static void main(String[] args) {
        // Example message divided into 3 blocks (160 bits each)
        String[] xBlocks = {
            "1100110011001100110011001100110011001100110011001100110011001100", // M1
            "1010101010101010101010101010101010101010101010101010101010101010", // M2
            "0110011001100110011001100110011001100110011001100110011001100110"  // M3
        };

        // Compute the hash of x
        String hashX = computeHash(xBlocks);

        // Find a collision (y) such that h(x) = h(y) and x != y
        String[] yBlocks = findCollision(xBlocks);

        // Display results
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

    public static String[] findCollision(String[] xBlocks) {
        String[] yBlocks = Arrays.copyOf(xBlocks, xBlocks.length);

        // Modify two blocks to create a collision
        int block1 = 0; // First block to modify
        int block2 = 1; // Second block to adjust

        // Flip all bits in the first block
        yBlocks[block1] = flipBits(xBlocks[block1]);

        // Adjust the second block to cancel out the changes
        yBlocks[block2] = xorBinaryStrings(xBlocks[block2], xorBinaryStrings(xBlocks[block1], yBlocks[block1]));

        return yBlocks;
    }

    private static String xorBinaryStrings(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    private static String flipBits(String block) {
        StringBuilder flipped = new StringBuilder();
        for (char bit : block.toCharArray()) {
            flipped.append(bit == '0' ? '1' : '0');
        }
        return flipped.toString();
    }
}
