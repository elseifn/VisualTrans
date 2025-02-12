/*
 *  BIP39 library, a Java implementation of BIP39
 *  Copyright (C) 2017 Alan Evans, NovaCrypto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  Original source: https://github.com/NovaCrypto/BIP39
 *  You can contact the authors via github issues.
 */

package io.github.novacrypto.bip39;

import io.github.novacrypto.bip39.testjson.EnglishJson;
import io.github.novacrypto.bip39.testjson.TestVectorJson;
import io.github.novacrypto.bip39.testjson.TestVector;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public final class SeedCalculationTests {

    @Test
    public void bip39_english() throws Exception {
        assertEquals("2eea1e4d099089606b7678809be6090ccba0fca171d4ed42c550194ca8e3600cd1e5989dcca38e5f903f5c358c92e0dcaffc9e71a48ad489bb868025c907d1e1",
                calculateSeedHex("solar puppy hawk oxygen trip brief erase slot fossil mechanic filter voice"));
    }

    @Test
    public void bip39_english_with_passphrase() throws Exception {
        assertEquals("36732d826f4fa483b5fe8373ef8d6aa3cb9c8fb30463d6c0063ee248afca2f87d11ebe6e75c2fb2736435994b868f8e9d4f4474c65ee05ac47aad7ef8a497846",
                calculateSeedHex("solar puppy hawk oxygen trip brief erase slot fossil mechanic filter voice", "CryptoIsCool"));
    }

    @Test
    public void all_english_test_vectors() throws Exception {
        final EnglishJson data = EnglishJson.load();
        for (final String[] testCase : data.english) {
            assertEquals(testCase[2], calculateSeedHex(testCase[1], "TREZOR"));
        }
    }

    @Test
    public void passphrase_normalization() throws Exception {
        assertEquals(calculateSeedHex("solar puppy hawk oxygen trip brief erase slot fossil mechanic filter voice", "ｶ"),
                calculateSeedHex("solar puppy hawk oxygen trip brief erase slot fossil mechanic filter voice", "カ"));
    }

    @Test
    public void all_japanese_test_vectors() throws Exception {
        final TestVectorJson data = TestVectorJson.loadJapanese();
        for (final TestVector testVector : data.vectors) {
            assertEquals(testVector.seed, calculateSeedHex(testVector.mnemonic, testVector.passphrase));
        }
    }

    @Test
    public void all_french_test_vectors() throws Exception {
        final TestVectorJson data = TestVectorJson.loadFrench();
        for (final TestVector testVector : data.vectors) {
            assertEquals(testVector.entropy, testVector.seed, calculateSeedHex(testVector.mnemonic, testVector.passphrase));
        }
    }

    private static String calculateSeedHex(final String mnemonic) {
        return calculateSeedHex(mnemonic, "");
    }

    private static String calculateSeedHex(String mnemonic, String passphrase) {
        return toHex(new SeedCalculator()
                .calculateSeed(mnemonic, passphrase));
    }

    private static String toHex(byte[] array) {
        final BigInteger bi = new BigInteger(1, array);
        final String hex = bi.toString(16);
        final int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
