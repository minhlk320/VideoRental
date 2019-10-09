package daos;
import java.util.List;
import entities.Customer;

public class CustomerDAO extends GeneralCRUD<Customer> {
	public Customer findbyPhone(String phone) {
		List<Customer> list = this.getAll(Customer.class);
		for (int i = 0; i < list.size(); i++) {
			Customer customer = list.get(i);
			if (customer.getPhoneNumber().equalsIgnoreCase(phone))
				return customer;
		}
		return null;
	}


}
