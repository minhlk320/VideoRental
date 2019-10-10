package ui;

import java.time.LocalDate;
import java.time.LocalTime;
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
		Item item = new Item(title, Item.ON_SHELF);
		Customer customer = new Customer("Minh", "Truong", "DN", "0987654321");
		Rental rental = new Rental(LocalDate.now());
		rental.setCustomer(customer);
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
		System.out.println(titleDAO.getAll(Title.class));
		LocalTime l0 = LocalTime.of(4,20);
		LocalTime l1 = LocalTime.now();
		System.out.println(l1.getHour()+l1.getMinute()/60f);

	}

}
