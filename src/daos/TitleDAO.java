package daos;

import entities.Title;

import javax.persistence.Query;
import java.util.List;

public class TitleDAO extends GeneralCRUD<Title>{
    public List<Title> getTitleListByName(String name) {
        Query q = em.createNativeQuery("SELECT * FROM Title WHERE titleName like '%"+name+"%'", Title.class);
        try {
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
