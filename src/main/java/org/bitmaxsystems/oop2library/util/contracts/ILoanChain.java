package org.bitmaxsystems.oop2library.util.contracts;

import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;

public interface ILoanChain {
    void setNextChain(ILoanChain chain);
    void execute(LoanDataDTO data) throws Exception;
}