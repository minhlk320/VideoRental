package daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public abstract class GeneralCRUD<T> {

	protected EntityManager em;

	public GeneralCRUD() {
		em = MyEntityManagerFactory.getInstance().getEntityManager();
	}

	public boolean save(T t) {
		EntityTransaction tr = em.getTransaction();
		try {
			tr.begin();
			em.persist(t);
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
		}
		return false;
	}

	public boolean delete(T t) {
		EntityTransaction tr = em.getTransaction();
		try {
			tr.begin();
			em.remove(t);
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
		}
		return false;
	}

	public void deleteByID(Class<T> t, Object o) {
		EntityTransaction tr = em.getTransaction();
		try {
			tr.begin();
			em.remove(getById(t, o));
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
		}
	}

	public void update(T t) {
		EntityTransaction tr = em.getTransaction();
		try {
			tr.begin();
			em.merge(t);
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(Class<T> t) {
		String s = "From " + t.getName();
		Query q = em.createQuery(s);
		return q.getResultList();
	}

	public T getById(Class<T> t, Object o) {
		return em.find(t, o);
	}
}
