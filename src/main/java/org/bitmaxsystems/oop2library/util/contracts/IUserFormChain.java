package org.bitmaxsystems.oop2library.util.contracts;

import org.bitmaxsystems.oop2library.models.form.UserFormDTO;

public interface IUserFormChain {

    void setNextChain (IUserFormChain chain);

    void execute(UserFormDTO formData) throws Exception;
}
