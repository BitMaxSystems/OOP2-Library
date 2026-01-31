package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class UpdatePasswordChain implements IUserFormChain {
    private IUserFormChain nextChain;
    private GenericRepository<Credentials> credentialsGenericRepository = new GenericRepository<>(Credentials.class);

    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {
        if (formData.isNewPassword())
        {
            Credentials credentials = formData.getUser().getCredentials();

            credentials.setPassword(BCrypt.hashpw(formData.getPasswordField(),BCrypt.gensalt()));

            credentialsGenericRepository.update(credentials);
        }

        if (nextChain != null)
        {
            nextChain.execute(formData);
        }
    }
}
