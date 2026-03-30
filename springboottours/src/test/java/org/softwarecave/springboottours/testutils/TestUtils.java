package org.softwarecave.springboottours.testutils;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestUtils {

    public static void assertIsConstraintViolationException(Throwable exception) {
        var cause = exception;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                return;
            }
            cause = cause.getCause();
        }
        assertFalse(false, "ConstraintViolationException expected but not thrown");
    }

}
