package ui;

import java.time.LocalDate;
import java.util.Arrays;

import daos.CustomerDAO;
import daos.ItemDAO;
import daos.RentalDAO;
import daos.TitleDAO;
import entities.Customer;
import entities.Item;
import entities.Rental;
import entities.RentalDetail;
import entities.Title;

public class Test {

	public static void main(String[] args) {
		Title title = new Title("Avatar", 4.0, null);
		title.setTitleID("1");
		Item item = new Item(title, Item.ON_SHELF);
		item.setItemID("1");
		Customer customer = new Customer("Minh", "Truong", "DN", "0987654321");
				customer.setCustomerID("1");
		Rental rental = new Rental(LocalDate.now());
		rental.setCustomer(customer);
		rental.setRentalID("1");
		RentalDetail detail = new RentalDetail(rental, item, 6.0);
		rental.setItems(Arrays.asList(detail));
		
		System.out.println(rental);
		CustomerDAO customerDAO = new CustomerDAO();
		TitleDAO titleDAO = new TitleDAO();
		RentalDAO rentalDAO = new RentalDAO();
		ItemDAO itemDAO = new ItemDAO();
		
		customerDAO.save(customer);
		titleDAO.save(title);
		itemDAO.save(item);
		rentalDAO.save(rental);
		System.out.println(customerDAO.getAll(Customer.class));

	}

}
