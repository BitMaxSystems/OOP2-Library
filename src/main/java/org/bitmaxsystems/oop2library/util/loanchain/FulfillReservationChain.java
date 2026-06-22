package org.bitmaxsystems.oop2library.util.loanchain;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.reservations.Reservation;
import org.bitmaxsystems.oop2library.repository.ReservationRepository;
import org.bitmaxsystems.oop2library.util.contracts.ILoanChain;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FulfillReservationChain implements ILoanChain {

    private ILoanChain nextChain;
    private final ReservationRepository reservationRepository = new ReservationRepository();

    @Override
    public void setNextChain(ILoanChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(LoanDataDTO data) throws Exception {
        Book returnedBook = data.getInventory().getBook();

        List<Reservation> pendingReservations = reservationRepository.findPendingByBook(returnedBook);

        if (!pendingReservations.isEmpty()) {
            Reservation oldest = pendingReservations.get(0);

            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();

                Reservation managedReservation = session.merge(oldest);
                managedReservation.fulfil();
                session.merge(managedReservation);

                transaction.commit();

            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw e;
            }
        }

        if (nextChain != null) {
            nextChain.execute(data);
        }
    }
}