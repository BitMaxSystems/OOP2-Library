package org.bitmaxsystems.oop2library.util;

import org.bitmaxsystems.oop2library.exceptions.FormAlreadyApprovedException;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class ApproveUserFormService {
    private GenericRepository<UserForm> userFormGenericRepository = new GenericRepository<>(UserForm.class);

    public void approveUser(UserForm form) throws FormAlreadyApprovedException
    {
        if (!form.approveForm())
        {
            throw new FormAlreadyApprovedException("This user is already approved!");
        }
        else
        {
            userFormGenericRepository.update(form);
        }

    }
}
