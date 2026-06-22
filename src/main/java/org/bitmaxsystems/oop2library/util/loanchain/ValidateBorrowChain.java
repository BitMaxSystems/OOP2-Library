package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;

import java.util.Date;

public class ValidateBorrowChain implements ILoanChain {

    private static final int MAX_ACTIVE_LOANS = 3;
    private ILoanChain nextChain;
    private final LoanRepository loanRepository = new LoanRepository();

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        if (data.getUser().getRole() != UserRole.READER) {
            throw new DataValidationException("Only approved readers can borrow books");
        }

        if (data.getInventory().getStatus() != BookStatus.AVAILABLE) {
            throw new DataValidationException("This copy is not available for borrowing");
        }

        if (data.getDueDate().before(new Date())) {
            throw new DataValidationException("Due date cannot be in the past");
        }

        int activeLoans = loanRepository.countActiveByUser(data.getUser());
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new DataValidationException(
                    "Reader has reached the maximum of " + MAX_ACTIVE_LOANS + " active loans");
        }

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }
}