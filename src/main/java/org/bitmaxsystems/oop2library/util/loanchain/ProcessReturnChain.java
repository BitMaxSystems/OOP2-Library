package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProcessReturnChain implements ILoanChain {

    private ILoanChain nextChain;

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        Loan loan = data.getLoan();
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Loan managedLoan = session.merge(loan);
            managedLoan.markReturned();

            Inventory managedInventory = session.merge(data.getInventory());

            if (data.getInventory().getStatus() == BookStatus.DAMAGED) {
                managedInventory.setStatus(BookStatus.DAMAGED);
            } else {
                managedInventory.setStatus(BookStatus.AVAILABLE);
            }

            session.merge(managedLoan);
            session.merge(managedInventory);

            transaction.commit();

            data.setLoan(managedLoan);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }
}