package ImageHidingCode;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Arrays;

public class FileHashing {
    public static void main(String[] args) throws Exception {
        // Input file paths
        String originalFilePath = "/Users/kl/dev/ICSI426/ICSI426_HW3/HW3/2000CharsCopy.txt";
        String modifiedFilePath = "/Users/kl/dev/ICSI426/ICSI426_HW3/HW3/Mod2000CharsCopy.txt";

        // Compute hash of the original file
        byte[] originalHash = computeFileHash(originalFilePath, "SHA-256");
        System.out.println("Original File Hash: " + bytesToHex(originalHash));

        // Modify a single character in the file
        modifyFile(originalFilePath, modifiedFilePath);

        // Compute hash of the modified file
        byte[] modifiedHash = computeFileHash(modifiedFilePath, "SHA-256");
        System.out.println("Modified File Hash: " + bytesToHex(modifiedHash));

        // Compare the two hashes and calculate the percentage of bit changes
        double bitChangePercentage = calculateBitChangePercentage(originalHash, modifiedHash);
        System.out.printf("Percentage of Bit Changes: %.2f%%%n", bitChangePercentage);
    }

    // Method to compute the hash of a file
    public static byte[] computeFileHash(String filePath, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        return digest.digest(fileBytes);
    }

    // Method to modify a single character in the file
    public static void modifyFile(String originalFilePath, String modifiedFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(originalFilePath)));
        // Modify the first character (or any character)
        char modifiedChar = content.charAt(0) == 'A' ? 'B' : 'A';
        content = modifiedChar + content.substring(1);
        Files.write(Paths.get(modifiedFilePath), content.getBytes());
    }

    // Method to calculate the percentage of bit changes between two hashes
    public static double calculateBitChangePercentage(byte[] hash1, byte[] hash2) {
    	 if (hash1.length != hash2.length) {
    	        throw new IllegalArgumentException("Hash lengths must be equal for comparison.");
    	 }
        int bitChanges = 0;
        for (int i = 0; i < hash1.length; i++) {
            bitChanges += Integer.bitCount(hash1[i] ^ hash2[i]);
        }
        
        return (bitChanges / (double) 2000) * 100;
    }

    // Helper method to convert bytes to a hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
