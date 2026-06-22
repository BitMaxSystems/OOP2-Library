package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.loans.enums.LoanStatus;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;

public class ValidateReturnChain implements ILoanChain {

    private ILoanChain nextChain;
    private final LoanRepository loanRepository = new LoanRepository();

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        Loan activeLoan = loanRepository.findActiveByInventory(data.getInventory());

        if (activeLoan == null) {
            throw new DataValidationException("No active loan found for this copy");
        }

        if (activeLoan.getStatus() == LoanStatus.RETURNED) {
            throw new DataValidationException("This copy has already been returned");
        }

        if (!activeLoan.getUser().equals(data.getUser())) {
            throw new DataValidationException("This copy was not borrowed by this user");
        }

        data.setLoan(activeLoan);

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }
}