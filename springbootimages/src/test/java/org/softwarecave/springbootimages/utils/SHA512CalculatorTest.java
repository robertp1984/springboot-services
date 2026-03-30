package org.softwarecave.springbootimages.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SHA512CalculatorTest {

    @Test
    void returnsExpectedHashForKnownInput() throws Exception {
        SHA512Calculator calc = new SHA512Calculator();
        byte[] input = "abc".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String expected = "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f";
        assertEquals(expected, calc.getHash(input));
    }

    @Test
    void returnsExpectedHashForEmptyInput() throws Exception {
        SHA512Calculator calc = new SHA512Calculator();
        byte[] input = new byte[0];
        String expected = "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e";
        assertEquals(expected, calc.getHash(input));
    }

    @Test
    void throwsNullPointerExceptionForNullInput() {
        SHA512Calculator calc = new SHA512Calculator();
        Executable exec = () -> calc.getHash(null);
        assertThrows(NullPointerException.class, exec);
    }

    @Test
    void producedHashIsLowercaseHexOfLength128() throws Exception {
        SHA512Calculator calc = new SHA512Calculator();
        String hex = calc.getHash(new byte[]{0, 1, 2, 3});
        assertEquals(128, hex.length());
        assertTrue(hex.matches("[0-9a-f]{128}"));
    }

    @Test
    void differentInputsProduceDifferentHashes() throws Exception {
        SHA512Calculator calc = new SHA512Calculator();
        byte[] a = "hello".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] b = "hello.".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String ha = calc.getHash(a);
        String hb = calc.getHash(b);
        assertNotEquals(ha, hb);
    }

}
