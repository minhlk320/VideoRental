package daos;

import entities.Item;
import entities.Rate;

import javax.persistence.Query;
import java.util.List;

public class RateDAO extends GeneralCRUD<Rate>{
    public Rate getByName(String name) {
        Query q = em.createNativeQuery("SELECT * FROM Rate WHERE Rate.itemClassName =:name", Rate.class);
        q.setParameter("name",name);
        try {
            return (Rate) q.getSingleResult();

        }catch (Exception e){
            return null;
        }
    }
}
