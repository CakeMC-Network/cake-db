package net.cakemc.database.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The type Database compression.
 */
public class DatabaseCompression {

    private final Deflater deflater;
    private final Inflater inflater;

    /**
     * Instantiates a new Database compression.
     */
    public DatabaseCompression() {
        deflater = new Deflater(Deflater.BEST_COMPRESSION);
        inflater = new Inflater();
    }

    /**
     * Compress byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] compress(byte[] data) throws IOException {
        deflater.reset();

        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }

    /**
     * Decompress byte [ ].
     *
     * @param compressedData the compressed data
     * @return the byte [ ]
     * @throws IOException         the io exception
     * @throws DataFormatException the data format exception
     */
    public byte[] decompress(byte[] compressedData) throws IOException, DataFormatException {
        inflater.reset();

        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);

        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();

        return outputStream.toByteArray();
    }

}
