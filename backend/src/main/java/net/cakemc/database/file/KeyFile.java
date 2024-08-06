package net.cakemc.database.file;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * The type Key file.
 */
public class KeyFile {

    /**
     * The constant ALGORITHM.
     */
    public static final String ALGORITHM = "AES";
    /**
     * The constant KEY_SIZE.
     */
    public static final int KEY_SIZE = 256;

    private final File folderWalker;

    /**
     * Instantiates a new Key file.
     *
     * @param folderWalker the folder walker
     */
    public KeyFile(File folderWalker) {
        this.folderWalker = folderWalker;
    }

    /**
     * Load key secret key.
     *
     * @return the secret key
     */
    public SecretKey loadKey() {
        File keyFile = new File(folderWalker, "database.key.txt");
        if (!keyFile.exists()) {

            try {
                return writeNewKey();
            } catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return this.loadKeyFromTextFile(keyFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey writeNewKey() throws NoSuchAlgorithmException, IOException {
        File keyFile = new File(folderWalker, "database.key.txt");
        SecretKey newKey = generateKey();

        this.saveKeyAsTextFile(newKey, keyFile);
        return newKey;
    }

    /**
     * Generate key secret key.
     *
     * @return the secret key
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    private void saveKeyAsTextFile(SecretKey key, File file) throws IOException {
        byte[] keyBytes = key.getEncoded();
        int rows = (int) Math.ceil((double) keyBytes.length / 16);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 16; j++) {
                    int index = i * 16 + j;
                    if (index < keyBytes.length) {
                        writer.write(String.format("%02X ", keyBytes[index]));
                    } else {
                        writer.write("   "); // Padding if needed
                    }
                }
                writer.newLine();
            }
        }
    }

    private SecretKey loadKeyFromTextFile(File file) throws IOException {
        byte[] keyBytes;
        int numBytes = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] hexValues = line.trim().split("\\s+");
                numBytes += hexValues.length;
            }
        }

        keyBytes = new byte[numBytes];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int byteIndex = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] hexValues = line.trim().split("\\s+");
                for (String hex : hexValues) {
                    if (!hex.isEmpty()) {
                        keyBytes[byteIndex++] = (byte) Integer.parseInt(hex, 16);
                    }
                }
            }
        }

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

}
