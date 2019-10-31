package daos;

import entities.Reservation;

import javax.persistence.Query;
import java.util.List;

public class ReservationDAO extends  GeneralCRUD<Reservation> {
    public Reservation getEldestReservationByTitleID(String id){
        Query q = em.createNativeQuery("SELECT TOP 1 * FROM Reservation WHERE TitleID =:titleID ORDER BY reservationID ASC",Reservation.class);
        q.setParameter("titleID",id);
        try {
            return (Reservation) q.getSingleResult();
        }catch (Exception e){
            return null;
        }

    }
    public List<Reservation> getReservationbyCustomerID(String id){
        Query q = em.createNativeQuery("SELECT * FROM Reservation WHERE CustomerID =:customerID",Reservation.class);
        q.setParameter("customerID",id);
       try {
           return  q.getResultList();
       }catch (Exception e){
           return null;
       }
    }
}
