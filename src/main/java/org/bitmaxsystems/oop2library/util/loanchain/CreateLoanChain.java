package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CreateLoanChain implements ILoanChain {

    private ILoanChain nextChain;

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        Loan loan = new Loan(data.getInventory(), data.getUser(), data.getDueDate());

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Inventory inventory = session.merge(data.getInventory());
            inventory.setStatus(BookStatus.BORROWED);

            session.persist(loan);
            session.merge(inventory);

            transaction.commit();

            data.setLoan(loan);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }
}