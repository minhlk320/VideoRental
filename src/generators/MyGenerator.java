package generators;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import entities.Customer;
import entities.Rental;
import entities.Title;

public class MyGenerator implements IdentifierGenerator {

	String prefixRental = "RETZNo.";
	String prefixCustomer = "CUSNo.";
	String prefixTitle = "TTLNo.";
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {


		if(object.getClass().getName()==(Rental.class.getName()))
		{
			String query = "SELECT e.id FROM Rental e' ";
			Stream<String> ids = session.createQuery(query, String.class).stream();
			Long max = ids.map(o -> o.replace(prefixRental, "")).mapToLong(Long::parseLong).max().orElse(0L);
			return prefixRental + (String.format("%06d", max + 1));
		}
		if(object.getClass().getName()==(Customer.class.getName()))
		{
			String query = "SELECT e.id FROM Customer e";
			Stream<String> ids = session.createQuery(query, String.class).stream();
			Long max = ids.map(o -> o.replace(prefixCustomer, "")).mapToLong(Long::parseLong).max().orElse(0L);
			return prefixCustomer + (String.format("%06d", max + 1));
		}
		if(object.getClass().getName()==(Title.class.getName()))
		{
			String query = "SELECT e.id FROM Title e";
			Stream<String> ids = session.createQuery(query, String.class).stream();
			Long max = ids.map(o -> o.replace(prefixTitle, "")).mapToLong(Long::parseLong).max().orElse(0L);
			return prefixTitle + (String.format("%06d", max + 1));
		}
		else
			return null;
	}

}
