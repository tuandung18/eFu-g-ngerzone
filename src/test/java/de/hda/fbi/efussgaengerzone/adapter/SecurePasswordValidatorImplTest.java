package de.hda.fbi.efussgaengerzone.adapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SecurePasswordValidatorImplTest {
    private static final SecurePasswordValidatorImpl passwordValidator = new SecurePasswordValidatorImpl();

    @Test
    void isPasswordSecure() {
        assertFalse(passwordValidator.isPasswordSecure("9.RuCPAK,pU@.-pT"));
        assertFalse(passwordValidator.isPasswordSecure("F%%5r3N~GxxxLp'"));
        assertFalse(passwordValidator.isPasswordSecure("123"));
        assertFalse(passwordValidator.isPasswordSecure("1aAaa"));
        assertFalse(passwordValidator.isPasswordSecure("3N~GxxxLp'"));
        assertTrue(passwordValidator.isPasswordSecure("3N~G1x3Lp'"));
        assertFalse(passwordValidator.isPasswordSecure("9eRu9P9K3N~'"));
    }
}