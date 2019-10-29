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

	private String prefixRental = "RETZNo";
	private String prefixCustomer = "CUSNo";
	private String prefixTitle = "TTLNo";
	private String prefixItem = "ITMNo";
	private String prefixItemClass = "CLASSNo";
	private	String prefixLateCharge = "LATENo";
	private String prefixReservation = "PRESERNo";
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		if(object.getClass().getName()==(Rental.class.getName())) 
			return genID(prefixRental, "Rental", session);
		if(object.getClass().getName()==(Customer.class.getName()))
			return genID(prefixCustomer, "Customer", session);
		if(object.getClass().getName()==(Title.class.getName()))
			return genID(prefixTitle, "Title", session);
		if(object.getClass().getName()==(Item.class.getName()))
			return genID(prefixItem, "Item", session);
		if(object.getClass().getName()==(Rate.class.getName()))
			return genID(prefixItemClass, "Rate", session);
		if(object.getClass().getName()==(LateCharge.class.getName()))
			return genID(prefixLateCharge, "LateCharge", session);
		if(object.getClass().getName()==(Reservation.class.getName()))
			return genID(prefixReservation , "Reservation", session);
		else
			return null;
		
	}

	private String genID(String pre, String name, SharedSessionContractImplementor session) {
		String query = "SELECT e.id FROM " + name +" e";
		Stream<String> ids = session.createQuery(query, String.class).stream();
		Long max = ids.map(o -> o.replace(pre, "")).mapToLong(Long::parseLong).max().orElse(0L);
		return pre + (String.format("%06d", max + 1));
	}

}
