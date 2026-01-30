package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;

public class SaveFormChain implements IUserFormChain {
    private IUserFormChain nextChain;
    private GenericRepository<UserForm> userFormRepository = new GenericRepository<>(UserForm.class);

    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {
        UserForm form = new UserForm(formData.getUser());
        userFormRepository.save(form);

        if (nextChain != null)
        {
            nextChain.execute(formData);
        }
    }
}
