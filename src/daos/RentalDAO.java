package daos;

import entities.Rental;
import entities.RentalDetail;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO extends GeneralCRUD<Rental> {
    public  Rental getLatestRentalByItemID(String id){
        Query q = em.createNativeQuery("SELECT * FROM RENTAL WHERE RENTALID =:rentalID",Rental.class);
        q.setParameter("rentalID",getLastestRentalIDbyItemID(id));
        try {
            return (Rental) q.getSingleResult();
        }catch (Exception e){
            return null;
        }

    }
    private String getLastestRentalIDbyItemID(String itemID){
        Query q = em.createNativeQuery("SELECT TOP 1 RENTALID FROM RentalDetail WHERE ItemID =:itemID ORDER BY RentalID DESC");
        q.setParameter("itemID",itemID);
        try{
            return (String) q.getSingleResult();
        }catch (Exception e){
            return null;
        }

    }
    public List<Rental> getAllOverDue(){
        try {
            List<Rental> rentalList = getAll(Rental.class);
            List<Rental> overDueList = new ArrayList<>();
            for(Rental rt : rentalList )
                for(RentalDetail detail : rt.getItems()){
                    LocalDate rentedDate = rt.getDate();
                    LocalDate currentDate = LocalDate.now();
                    if(currentDate.isAfter(rentedDate.plusDays(detail.getRentalPeriod())))
                        if(!overDueList.contains(rt))
                            overDueList.add(rt);
                }
            return overDueList;
        }
        catch (Exception e){
            return  null;
        }
    }
}
