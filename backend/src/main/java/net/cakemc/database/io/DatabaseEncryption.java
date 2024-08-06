package net.cakemc.database.io;

import net.cakemc.database.file.KeyFile;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * The type Database encryption.
 */
public class DatabaseEncryption {

    private final Cipher decrypt;
    private final Cipher encrypt;

    /**
     * Instantiates a new Database encryption.
     *
     * @param keyFile the key file
     */
    public DatabaseEncryption(KeyFile keyFile) {

        SecretKey secretKey = keyFile.loadKey();

        try {
            encrypt = Cipher.getInstance(KeyFile.ALGORITHM);
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        try {
            decrypt = Cipher.getInstance(KeyFile.ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Encrypt byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public byte[] encrypt(byte[] data) {
        try {
            return encrypt.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt byte [ ].
     *
     * @param encryptedData the encrypted data
     * @return the byte [ ]
     */
    public byte[] decrypt(byte[] encryptedData) {
        try {
            return decrypt.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }


}
