package daos;

import entities.Item;
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
    public int getNumberAllcopies(String titleID){
        Query q = em.createNativeQuery("SELECT Count(*)  FROM Item WHERE titleID =:titleID ");
        q.setParameter("titleID",titleID);
        try {
            return (int) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }
    public int getNumbercopiesByStatus(String titleID, String status){
        Query q = em.createNativeQuery("SELECT Count(*)  FROM Item WHERE titleID =:titleID and status=:status ");
        q.setParameter("titleID",titleID);
        q.setParameter("status", status);
        try {
            return (int) q.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }
}
