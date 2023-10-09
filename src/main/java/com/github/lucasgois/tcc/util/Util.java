package com.github.lucasgois.tcc.util;

import com.github.lucasgois.tcc.exceptions.TccRuntimeException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
    private static List<Pair<String, String>> criarListaDeArquivoEHash(final String directoryPathMain, final Path directoryPath) throws IOException {
        final List<Pair<String, String>> fileList = new ArrayList<>();

        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {

            for (final Path filePath : directoryStream) {

                if (Files.isDirectory(filePath)) {
                    fileList.addAll(criarListaDeArquivoEHash(directoryPathMain, filePath));

                } else {
                    final String fileHash = calcularHash(filePath);
                    final String filePathString = filePath.toString().replace(directoryPathMain, "");

                    fileList.add(new Pair<>(fileHash, filePathString));
                }
            }
        }

        return fileList;
    }

    @NotNull
    public static List<Pair<String, String>> criarListaDeArquivoEHash(final Path directoryPath) throws IOException {
        return criarListaDeArquivoEHash(directoryPath.toString(), directoryPath);
    }

    @NotNull
    public static String calcularHash(byte[] content) {
        final MessageDigest digest256 = getMessageDigest();
        digest256.update(content);
        return byteToHex(digest256.digest());
    }

    @NotNull
    public static String calcularHash(@NotNull final Path arquivo) throws IOException {
        log.info("calculando hash para: {}", arquivo);

        final MessageDigest digest256 = getMessageDigest();

        try (final InputStream inputStream = Files.newInputStream(arquivo);
             final DigestInputStream dis = new DigestInputStream(inputStream, digest256)) {

            final byte[] buffer = new byte[4096];

            int bytesRead;
            do {
                bytesRead = dis.read(buffer);
            } while (bytesRead != -1);
        }

        byte[] digest = digest256.digest();

        return byteToHex(digest);
    }

    @NotNull
    public static String byteToHex(byte @NotNull [] byteArray) {
        final StringBuilder builder = new StringBuilder();

        for (final byte b : byteArray) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }

    public static void criarPasta(final Path pasta) {
        if (Files.notExists(pasta)) {
            try {
                Files.createDirectories(pasta);
            } catch (final IOException ex) {
                throw new TccRuntimeException("Não foi possível criar o diretório: " + pasta, ex);
            }
        }
    }

    public static byte @NotNull [] lerArquivo(final Path caminhoCompleto) {

        try (final FileInputStream fileInputStream = new FileInputStream(caminhoCompleto.toFile());
             final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            final byte[] buffer = new byte[4096];

            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();

        } catch (Exception ex) {
            throw new TccRuntimeException(ex);
        }
    }

    public static byte @NotNull [] gzipar(byte[] data) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
        } catch (IOException ex) {
            throw new TccRuntimeException(ex);
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static byte @NotNull [] ungzipar(final byte[] bytea) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytea);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (final GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            byte[] buffer = new byte[4096];

            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException ex) {
            throw new TccRuntimeException(ex);
        }

        return byteArrayOutputStream.toByteArray();
    }
}