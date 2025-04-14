package ImageHidingCode;

import java.util.ArrayList;
import java.util.List;

public class HashFunction {
    public static void main(String[] args) {
        // Example input messages
        String message1 = "This is a test message.";
        String message2 = "Another example message for hashing.";

        // Compute and display the hash for both messages
        System.out.println("Hash of message 1: " + computeHash(message1));
        System.out.println("Hash of message 2: " + computeHash(message2));
    }

    public static String computeHash(String message) {
        // Convert the message to a binary string
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }

        // Divide the binary message into 160-bit blocks
        List<String> blocks = new ArrayList<>();
        int blockSize = 160;
        for (int i = 0; i < binaryMessage.length(); i += blockSize) {
            if (i + blockSize <= binaryMessage.length()) {
                blocks.add(binaryMessage.substring(i, i + blockSize));
            } else {
                // Pad the last block with zeros if it's smaller than 160 bits
                String lastBlock = binaryMessage.substring(i);
                lastBlock = String.format("%-160s", lastBlock).replace(' ', '0');
                blocks.add(lastBlock);
            }
        }

        // Compute the hash by XORing all blocks
        String hash = blocks.get(0);
        for (int i = 1; i < blocks.size(); i++) {
            hash = xorBinaryStrings(hash, blocks.get(i));
        }

        return hash;
    }

    // Helper method to XOR two binary strings
    private static String xorBinaryStrings(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }
}