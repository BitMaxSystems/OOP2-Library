package org.bitmaxsystems.oop2library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;
import org.bitmaxsystems.oop2library.util.loanchain.*;

public class LoanService {

    private static final Logger logger = LogManager.getLogger(LoanService.class);
    private final LoanRepository loanRepository = new LoanRepository();

    public void borrowBook(LoanDataDTO data) throws Exception {
        ILoanChain validate = new ValidateBorrowChain();
        ILoanChain create = new CreateLoanChain();

        validate.setNextChain(create);

        validate.execute(data);
        logger.info("Loan created for user {} on inventory item {}",
                data.getUser().getFirstName(),
                data.getInventory().getId());
    }

    public void returnBook(LoanDataDTO data) throws Exception {
        ILoanChain validate = new ValidateReturnChain();
        ILoanChain process = new ProcessReturnChain();
        ILoanChain fine = new ApplyFineChain();
        ILoanChain fulfil = new FulfillReservationChain();

        validate.setNextChain(process);
        process.setNextChain(fine);
        fine.setNextChain(fulfil);

        validate.execute(data);
        logger.info("Loan returned for user {} on inventory item {}",
                data.getUser().getFirstName(),
                data.getInventory().getId());
    }

    public void markOverdueLoans() {
        loanRepository.findOverdue().forEach(loan -> {
            loan.markOverdue();
            loanRepository.update(loan);
            logger.warn("Loan {} marked as overdue", loan.getId());
        });
    }
}