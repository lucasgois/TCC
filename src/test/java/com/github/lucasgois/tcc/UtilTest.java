package com.github.lucasgois.tcc;

import com.github.lucasgois.tcc.util.Util;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UtilTest {

    @Test
    void load_files_hash() throws Exception {
        final Path testDirectory = Path.of("src/test/resources/entrada");
        final String expectedHashEqual = "C284DD25302D560AF3D395F643CD9394C56D22CB673E23E10F7F1D5955638420";
        final String expectedHashDifferent = "87ECE68D5E6CD46732446320AA32A1CB0BCCBB78CC39CD4E43DBBEFFD1D5C56A";
        final String[] expectedFilePaths = {
                "\\Arquivo1.txt",
                "\\Arquivo2.txt",
                "\\Arquivo3.txt",
                "\\pasta\\Arquivo1.txt",
        };

        final List<Pair<String, String>> resultList = Util.listFilesWithHashes(testDirectory);

        assertEquals(4, resultList.size());
        for (int i = 0; i < expectedFilePaths.length; i++) {
            assertEquals(expectedFilePaths[i], resultList.get(i).getValue());
        }
        assertEquals(expectedHashEqual, resultList.get(0).getKey());
        assertEquals(expectedHashEqual, resultList.get(1).getKey());
        assertEquals(expectedHashDifferent, resultList.get(2).getKey());
    }

    @Test
    void calculate_hash_test() throws Exception {
        final byte[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        final String expected = "3E5718FEA51A8F3F5BACA61C77AFAB473C1810F8B9DB330273B4011CE92C787E";

        assertEquals(expected, Util.calcularHash(actual));
    }

    @Test
    void byte_to_hex_test() {
        final byte[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        final String expected = "000102030405060708090A0B0C0D0E0F10";

        assertEquals(expected, Util.byteToHex(actual));
    }
}