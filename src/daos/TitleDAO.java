package daos;

import entities.Title;

import javax.persistence.Query;
import java.util.List;

public class TitleDAO extends GeneralCRUD<Title>{
    public List<Title> getALL(){
        Query q = em.createNativeQuery("SELECT * FROM Title",Title.class);
        return q.getResultList();
    }
}
