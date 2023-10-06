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
        final Path testDirectory = Path.of("src/test/resources/files");
        final String expectedHashEqual = "9F0FA1CD32380E2541D81AD69B0D0B5B6BF4C56533E760F29520E4EFD7346288";
        final String expectedHashDifferent = "A21BE189EBC1DC423117B0FEF4C5C0B74D819BE0B667625E83A190F234D9A89D";
        final String[] expectedFilePaths = {
                "\\File1.txt",
                "\\File2.txt",
                "\\File3.txt",
        };

        List<Pair<String, String>> resultList = Util.listFilesWithHashes(testDirectory);

        assertEquals(3, resultList.size());
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

        assertEquals(expected, Util.calculateHash(actual));
    }

    @Test
    void byte_to_hex_test() {
        final byte[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        final String expected = "000102030405060708090A0B0C0D0E0F10";

        assertEquals(expected, Util.byteToHex(actual));
    }
}