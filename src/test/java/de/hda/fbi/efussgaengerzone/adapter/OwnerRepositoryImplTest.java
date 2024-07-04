package de.hda.fbi.efussgaengerzone.adapter;

import de.hda.fbi.efussgaengerzone.domain.model.owner.Owner;
import de.hda.fbi.efussgaengerzone.domain.model.owner.OwnerRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class OwnerRepositoryImplTest {
    private OwnerRepository ownerRepository = new OwnerRepositoryImpl();

    @Test
    public void delete() {
        Owner owner = new Owner("test", "test@test.com", "1234");
        ownerRepository.save(owner);

        Owner foundOwner = ownerRepository.find(owner.email());
        assertNotNull(foundOwner);

        ownerRepository.delete(owner);
        assertNull(ownerRepository.find(owner.email()));
    }
}
