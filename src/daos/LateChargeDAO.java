package daos;

import entities.LateCharge;
import entities.Reservation;

import javax.persistence.Query;
import java.util.List;

public class LateChargeDAO extends GeneralCRUD<LateCharge> {
    public List<LateCharge> getLateChargeByCustomerID(String id){
        Query q = em.createNativeQuery("SELECT * FROM LateCharge WHERE CustomerID =:customerID",LateCharge.class);
        q.setParameter("customerID",id);
        return  q.getResultList();
    }

}
