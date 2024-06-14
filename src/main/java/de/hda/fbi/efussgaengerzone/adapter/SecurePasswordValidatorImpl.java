package de.hda.fbi.efussgaengerzone.adapter;

import de.hda.fbi.efussgaengerzone.domain.usecase.abstractions.SecurePasswordValidator;
import org.passay.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AlphabeticalSequenceRule implements Rule {

    @Override
    public RuleResult validate(PasswordData passwordData) {
        String password = passwordData.getPassword();
        Matcher matcher = Pattern.compile("[a-zA-Z]{3}").matcher(password);
        if (matcher.find()) {
            return new RuleResult(false, new RuleResultMetadata());
        }
        return new RuleResult(true);
    }
}

public class SecurePasswordValidatorImpl implements SecurePasswordValidator {

    private static final PasswordValidator validator = new PasswordValidator(
            new LengthRule(8, 16),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
            new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
            new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
            new WhitespaceRule(),
            new AlphabeticalSequenceRule()
    );


    @Override
    public boolean isPasswordSecure(String password) {
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }
}
