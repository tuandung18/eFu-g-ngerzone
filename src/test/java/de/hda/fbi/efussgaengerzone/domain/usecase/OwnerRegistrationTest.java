package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.owner.Owner;
import de.hda.fbi.efussgaengerzone.domain.model.owner.OwnerRepository;
import de.hda.fbi.efussgaengerzone.domain.usecase.abstractions.SecurePasswordValidator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OwnerRegistrationTest {

    private static final Owner SAMPLE_OWNER = new Owner("owner", "email", "s342$asdfioDA4!asE");

    private final OwnerRepository ownerRepository = mock(OwnerRepository.class);
    private final ShopOrganization shopOrganization = mock(ShopOrganization.class);
    private final ShopBrowsing shopBrowsing = mock(ShopBrowsing.class);
    private final SecurePasswordValidator passwordValidator = mock(SecurePasswordValidator.class);

    private final OwnerRegistration sut = new OwnerRegistration(ownerRepository, shopOrganization, shopBrowsing, passwordValidator);

    @Nested
    class login {
        @Test
        void returnsTrueIfUserWithMatchingCredentialsIsFound() {
            when(ownerRepository.findPredicate(any(Predicate.class)))
                    .thenReturn(Set.of(SAMPLE_OWNER));

            assertTrue(sut.login(SAMPLE_OWNER.email(), SAMPLE_OWNER.password()));
        }

        @Test
        void returnsFalseIfNoUserWithMatchingCredentialsIsFound() {
            when(ownerRepository.findPredicate(any(Predicate.class)))
                    .thenReturn(Set.of());

            assertFalse(sut.login(SAMPLE_OWNER.email(), SAMPLE_OWNER.password()));
        }
    }

}