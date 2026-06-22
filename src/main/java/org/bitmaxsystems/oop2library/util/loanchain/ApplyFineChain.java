package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ApplyFineChain implements ILoanChain {

    private static final int POINTS_ON_TIME = 10;
    private static final int POINTS_PER_DAY_OVERDUE = 5;
    private static final int MAX_PENALTY = 30;

    private ILoanChain nextChain;

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        Loan loan = data.getLoan();
        User user = data.getUser();

        int pointsDelta = calculatePointsDelta(loan);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User managedUser = session.merge(user);
            managedUser.updateLoyaltyPoints(pointsDelta);
            session.merge(managedUser);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }

    private int calculatePointsDelta(Loan loan) {
        long daysOverdue = loan.getDaysOverdue();

        if (daysOverdue == 0) {
            return POINTS_ON_TIME;
        }

        int penalty = (int) (daysOverdue * POINTS_PER_DAY_OVERDUE);
        return -Math.min(penalty, MAX_PENALTY);
    }
}