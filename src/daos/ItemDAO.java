package daos;

import entities.Item;
import javax.persistence.Query;
import java.util.List;

public class ItemDAO extends GeneralCRUD<Item>{
    public List<Item> getALL(){
        Query q = em.createNativeQuery("SELECT * FROM Item",Item.class);
        return q.getResultList();
    }


}
