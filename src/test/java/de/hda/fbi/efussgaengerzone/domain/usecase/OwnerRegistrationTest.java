package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.owner.Owner;
import de.hda.fbi.efussgaengerzone.domain.model.owner.OwnerRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.OpeningHours;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.WeeklyOpeningHours;
import de.hda.fbi.efussgaengerzone.domain.usecase.abstractions.SecurePasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OwnerRegistrationTest {

    @Nested
    class login {
        @Mock
        private OwnerRepository ownerRepository;

        @Mock
        private ShopOrganization shopOrganization;

        @Mock
        private ShopBrowsing shopBrowsing;

        @Mock
        private SecurePasswordValidator passwordValidator;

        @InjectMocks
        private OwnerRegistration ownerRegistration;

        private Owner owner1 = new Owner("owner", "email", "s342$asdfioDA4!asE");
        private UUID shopId1;
        private Shop shop1;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            owner1 = new Owner("owner1", "owner1@email.com", "s342$asdfioDA4!asE");
        }

        @Test
        void returnsTrueIfUserWithMatchingCredentialsIsFound() {
            when(ownerRepository.findPredicate(any(Predicate.class)))
                    .thenReturn(Set.of(owner1));

            assertTrue(ownerRegistration.login(owner1.email(), owner1.password()));
        }

        @Test
        void returnsFalseIfNoUserWithMatchingCredentialsIsFound() {
            when(ownerRepository.findPredicate(any(Predicate.class)))
                    .thenReturn(Set.of());

            assertFalse(ownerRegistration.login(owner1.email(), owner1.password()));
        }
    }
}