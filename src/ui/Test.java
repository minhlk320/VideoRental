package ui;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import daos.CustomerDAO;
import daos.ItemClassDAO;
import daos.ItemDAO;
import daos.RentalDAO;
import daos.TitleDAO;
import entities.Customer;
import entities.Item;
import entities.ItemClass;
import entities.Rental;
import entities.RentalDetail;
import entities.Title;

public class Test {

	public static void main(String[] args) {
		//createDataBase();
		createTitle();

	
	}
public static void createDataBase() {
	ItemClass itemClass = new ItemClass(ItemClass.MOVIE, 2.0, 7, 1.5);
	Title title = new Title("AVATAR", "Movie has the best profit in history", new File("D:\\XDPM\\misc\\Images\\avatar.jpg"));
	Item item = new Item(title, Item.ON_SHELF, itemClass);
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
	ItemClassDAO itemClassDAO = new ItemClassDAO();
	itemClassDAO.save(itemClass);
	customerDAO.save(customer);
	titleDAO.save(title);
	itemDAO.save(item);
	rentalDAO.save(rental);
	System.out.println(titleDAO.getAll(Title.class));
	
}
public static void createTitle() {
	Title title1 = new Title("AQUAMAN", "A movie from DCU has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\aquaman.jpg"));
	Title title2 = new Title("BLADE RUNNER", "Announced on 2016", new File("D:\\XDPM\\misc\\Images\\bladeRunner.jpg"));
	Title title3= new Title("END GAME", "A movie from MCU has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\endgame.jpg"));
	Title title4 = new Title("FAST AND FURIOUS: HOBBS AND SHAWN", "Announced in 2019", new File("D:\\XDPM\\misc\\Images\\ff9.jpg"));
	Title title5 = new Title("HOW TO TRAIN YOUR DRAGON 3", "A movie from Dreamwork has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\howtotrain.jpg"));
	Title title6 = new Title("MAX STEEL", "A movie has been announced in 2016", new File("D:\\XDPM\\misc\\Images\\maxsteel.jpg"));
	Title title7 = new Title("REPLICAS", "A scientic fiction movie", new File("D:\\XDPM\\misc\\Images\\replicas.jpg"));
	Title title8 = new Title("SILENT HILL", "An honor movie", new File("D:\\XDPM\\misc\\Images\\silenthill.jpg"));
	Title title9 = new Title("WRECK IT RAPTH", "A cartoon movie has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\wreckIt.jpg"));
	List<Title> titles = Arrays.asList(title1,title2,title3,title4,title5,title6,title7,title8,title9);
	titles.forEach(x->System.out.println(new TitleDAO().save(x)));
}
}

