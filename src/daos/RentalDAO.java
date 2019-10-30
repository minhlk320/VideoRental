package daos;

import entities.Rental;

import javax.persistence.Query;

public class RentalDAO extends GeneralCRUD<Rental> {
    public  Rental getLatestRentalByItemID(String id){
        Query q = em.createNativeQuery("SELECT * FROM RENTAL WHERE RENTALID =:rentalID",Rental.class);
        q.setParameter("rentalID",getLastestRentalIDbyItemID(id));
        return (Rental) q.getSingleResult();
    }
    private String getLastestRentalIDbyItemID(String itemID){
        Query q = em.createNativeQuery("SELECT TOP 1 RENTALID FROM RentalDetail WHERE ItemID =:itemID ORDER BY RentalID DESC");
        q.setParameter("itemID",itemID);
        return (String) q.getSingleResult();
    }


}
