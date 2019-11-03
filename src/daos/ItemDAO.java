package daos;

import entities.Item;
import javax.persistence.Query;
import java.util.List;

public class ItemDAO extends GeneralCRUD<Item>{
    public List<Item> getAvailableItemForRentByTitle(String titleID){
        Query q = em.createNativeQuery("SELECT * FROM ITEM WHERE TitleID =:id AND status=:status",Item.class);
        q.setParameter("id",titleID);
        q.setParameter("status",Item.ON_SHELF);
        try {
            return q.getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
