package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.owner.Owner;
import de.hda.fbi.efussgaengerzone.domain.model.owner.OwnerRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.usecase.abstractions.SecurePasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OwnerRegistration {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerRegistration.class);
    private final OwnerRepository ownerRepository;
    private final ShopOrganization shopOrganization;
    private final ShopBrowsing shopBrowsing;
    private final SecurePasswordValidator passwordValidator;

    public OwnerRegistration(OwnerRepository ownerRepository, ShopOrganization shopOrganization, ShopBrowsing shopBrowsing, SecurePasswordValidator passwordValidator) {
        this.ownerRepository = ownerRepository;
        this.shopOrganization = shopOrganization;
        this.shopBrowsing = shopBrowsing;
        this.passwordValidator = passwordValidator;
    }

    public Owner findByEmail(String email) {
        LOG.info("Looking up owner {}", email);
        return ownerRepository.find(email);
    }

    public Owner register(Owner owner) throws InsecurePasswordException {
        LOG.info("Registering owner with email {}", owner.email());
        if(!passwordValidator.isPasswordSecure(owner.password())) {
            LOG.error("Insecure password for owner with email {}", owner.email());
            throw new InsecurePasswordException();
        }

        if(ownerRepository.find(owner.email()) != null) {
            LOG.warn("Owner with email {} already exists", owner.email());
            throw new OwnerAlreadyExistsException(owner.email());
        }

        ownerRepository.save(owner);
        return owner;
    }

    public boolean login(String username, String password) {
        LOG.debug("Login user {}", username);
        Set<Owner> owners = ownerRepository.findPredicate(
                owner -> owner.email().equals(username) && owner.password().equals(password));

        if (owners.isEmpty()) {
            LOG.info("Login failed for user {}", username);
        } else {
            LOG.info("Login successful for user {}", username);
        }

        return !owners.isEmpty();
    }

    public void updateOwner(String oldEmail, String newEmail, String newName) {
        Owner oldOwner = ownerRepository.find(oldEmail);
        ownerRepository.delete(oldOwner);

        Owner newOwner = new Owner(newName, newEmail, oldOwner.password());
        ownerRepository.save(newOwner);

        Shop shop = shopBrowsing.getShopByOwner(oldEmail);
        shopOrganization.updateShopEmail(shop.id(), newEmail);
    }
}
