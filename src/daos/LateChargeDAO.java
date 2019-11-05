package daos;

import entities.Customer;
import entities.Item;
import entities.LateCharge;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class LateChargeDAO extends GeneralCRUD<LateCharge> {
    public List<LateCharge> getLateChargeByCustomerID(String id){
        Query q = em.createNativeQuery("SELECT * FROM LateCharge WHERE CustomerID =:customerID",LateCharge.class);
        q.setParameter("customerID",id);
        try {
            return q.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    public double getTotalLatechargeListByCustomerID(String id) {
        Query q = em.createNativeQuery("SELECT * FROM LateCharge WHERE CustomerID =:customerID", LateCharge.class);
        q.setParameter("customerID", id);
        try {
            double total = 0;
            List<LateCharge> list = q.getResultList();
            if (list.isEmpty()) return 0.0;
            for (LateCharge x : list) {
                total += x.getTotalAmount();
            }
            return total;
        } catch (Exception e) {
            return 0.0;
        }

    }

    public void recordLateChargePayment(LateCharge lateCharge) {
        lateCharge.setPurchaseDate(LocalDate.now());
        this.update(lateCharge);
    }
    public void addLateCharge(Item item, Customer customer, double totalAmount, LocalDate dueOn){
        LateCharge lateCharge = new LateCharge(LocalDate.now(),dueOn,item.getTitle(),customer,totalAmount);
        if (this.save(lateCharge))
            System.out.println("Late charge added!");
    }

}
