package generators;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import entities.Customer;
import entities.Item;
import entities.Rate;
import entities.LateCharge;
import entities.Rental;
import entities.Reservation;
import entities.Title;

public class MyGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String query = "SELECT e.id FROM " + object.getClass().getName() +" e";
		Stream<String> ids = session.createQuery(query, String.class).stream();
		Long max = ids.map(o -> o.replace("", "")).mapToLong(Long::parseLong).max().orElse(0L);
		return (String.format("%06d", max + 1));

	}

}
