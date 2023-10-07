package com.github.lucasgois.tcc.util;

import com.github.lucasgois.tcc.exce.TccRuntimeException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Util {

    private static MessageDigest messageDigest;

    private Util() {
    }

    private static MessageDigest getMessageDigest() {
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                throw new TccRuntimeException("SHA-256 não pode ser carrado", ex);
            }
        }
        return messageDigest;
    }

    @NotNull
    private static List<Pair<String, String>> listFilesWithHashes(final String directoryPathMain, final Path directoryPath) throws IOException, NoSuchAlgorithmException {
        final List<Pair<String, String>> fileList = new ArrayList<>();

        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {

            for (final Path filePath : directoryStream) {

                if (Files.isDirectory(filePath)) {
                    fileList.addAll(listFilesWithHashes(directoryPathMain, filePath));

                } else {
                    final String fileHash = calcularHash(Files.readAllBytes(filePath));
                    final String filePathString = filePath.toString().replace(directoryPathMain, "");

                    log.info("{} {}", fileHash, filePathString);

                    fileList.add(new Pair<>(fileHash, filePathString));
                }
            }
        }

        return fileList;
    }

    @NotNull
    public static List<Pair<String, String>> listFilesWithHashes(final Path directoryPath) throws IOException, NoSuchAlgorithmException {
        log.info("directoryPath: {}", directoryPath);
        return listFilesWithHashes(directoryPath.toString(), directoryPath);
    }

    @NotNull
    public static String calcularHash(byte[] content) {
        final MessageDigest digest256 = getMessageDigest();
        digest256.update(content);
        return byteToHex(digest256.digest());
    }

    @NotNull
    public static String byteToHex(byte @NotNull [] byteArray) {
        final StringBuilder builder = new StringBuilder();

        for (final byte b : byteArray) {
            builder.append(String.format("%02x", b).toUpperCase());
        }

        return builder.toString();
    }
}