package daos;

import entities.Item;
import entities.Reservation;
import entities.Title;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class ReservationDAO extends  GeneralCRUD<Reservation> {
    private ItemDAO itemDAO = new ItemDAO();
    public Reservation getEldestReservationByTitleID(String id){
        Query q = em.createNativeQuery("SELECT TOP 1 * FROM Reservation WHERE TitleID =:titleID AND ItemID is null ORDER BY reservationID ASC",Reservation.class);
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
    public Reservation getReservationbyItemID(String id){
        Query q = em.createNativeQuery("SELECT * FROM Reservation WHERE ItemID =:itemID",Reservation.class);
        q.setParameter("itemID",id);
        try {
            return (Reservation) q.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
    public Reservation checkReservation(Item item){
        Title titleOfItem = item.getTitle();
        Reservation eldestReservation = this.getEldestReservationByTitleID(titleOfItem.getTitleID());
        item.setLastModifiedDate(LocalDate.now());
        if(eldestReservation!=null){
            item.setStatus(Item.ON_HOLD);
            eldestReservation.setItem(item);
            itemDAO.update(item);
            this.update(eldestReservation);
            return eldestReservation;
        }
        else{
            item.setStatus(Item.ON_SHELF);
            itemDAO.update(item);
            return null;
        }
    }
}
