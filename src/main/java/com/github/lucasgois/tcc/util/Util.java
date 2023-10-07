package com.github.lucasgois.tcc.util;

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

    private Util() {
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

                    fileList.add(
                            new Pair<>(
                                    fileHash,
                                    filePathString
                            )
                    );
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
    public static String calcularHash(byte[] content) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(content);
        return byteToHex(md.digest());
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