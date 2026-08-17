package org.bitmaxsystems.oop2library.util.chain.userform.contract;

import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;

public interface IUserFormChain {

    void setNextChain (IUserFormChain chain);

    void execute(UserDataDTO formData) throws Exception;
}
