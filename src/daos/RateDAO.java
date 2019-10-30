package daos;

import entities.Item;
import entities.Rate;

import javax.persistence.Query;
import java.util.List;

public class RateDAO extends GeneralCRUD<Item>{
    public List<Rate> getALL(){
        Query q = em.createNativeQuery("SELECT * FROM Rate", Rate.class);
        return q.getResultList();
    }

    public Rate getByName(String name) {
        Query q = em.createNativeQuery("SELECT * FROM Rate WHERE Rate.itemClassName =:titlename", Rate.class);
        q.setParameter("titlename",name);
        return (Rate) q.getSingleResult();
    }
}
