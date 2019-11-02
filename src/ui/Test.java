package ui;

import daos.*;
import entities.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String[] args) {

//		createTitle();
//		System.out.println(new RentalDAO().getLatestRentalByItemID("000003"));

//		RentalAndReturnManagement rentalAndReturnManagement = new RentalAndReturnManagement();
//		rentalAndReturnManagement.Return("000002");

//        Customer customer = new CustomerDAO().getById(Customer.class,"000006");
//
//
//		Item it1 =  new ItemDAO().getById(Item.class,"000002");
//		Item it2 =  new ItemDAO().getById(Item.class,"000001");
//
//       RentalAndReturnManagement rentalAndReturnManagement = new RentalAndReturnManagement();
//       rentalAndReturnManagement.rental(customer,Arrays.asList(it1,it2));
	}
public static void createDataBase() {
	Rate itemClass = new Rate(Rate.MOVIE, 2.0, 7, 1.5);
	Title title = new Title("AVATAR", "Movie has the best profit in history", new File("D:\\XDPM\\misc\\Images\\avatar.jpg"),itemClass);
	Item item = new Item(title, Item.ON_SHELF);
	Customer customer = new Customer("Minh", "Truong", "DN", "0987654321",LocalDate.now(),true);
	Rental rental = new Rental(LocalDate.now());
	rental.setCustomer(customer);
	RentalDetail detail = new RentalDetail(rental, item, 3,1,3);
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
	Rate itemClass = new Rate(Rate.MOVIE, 2.0, 3, 1.5);
    Rate itemClass1 = new Rate(Rate.VIDEOGAME, 3, 4, 1.5);
	new RateDAO().save(itemClass);
    new RateDAO().save(itemClass1);
	Title title = new Title("AVATAR", "Movie has the best profit in history", new File("D:\\XDPM\\misc\\Images\\avatar.jpg"),itemClass);
	Title title1 = new Title("AQUAMAN", "A movie from DCU has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\aquaman.jpg"),itemClass);
	Title title2 = new Title("BLADE RUNNER", "Announced on 2016", new File("D:\\XDPM\\misc\\Images\\bladeRunner.jpg"),itemClass);
	Title title3= new Title("END GAME", "A movie from MCU has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\endgame.jpg"),itemClass);
	Title title4 = new Title("FAST AND FURIOUS: HOBBS AND SHAWN", "Announced in 2019", new File("D:\\XDPM\\misc\\Images\\ff9.jpg"),itemClass);
	Title title5 = new Title("HOW TO TRAIN YOUR DRAGON 3", "A movie from Dreamwork has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\howtotrain.jpg"),itemClass);
	Title title6 = new Title("MAX STEEL", "A movie has been announced in 2016", new File("D:\\XDPM\\misc\\Images\\maxsteel.jpg"),itemClass);
	Title title7 = new Title("REPLICAS", "A scientic fiction movie", new File("D:\\XDPM\\misc\\Images\\replicas.jpg"),itemClass);
	Title title8 = new Title("SILENT HILL", "An honor movie", new File("D:\\XDPM\\misc\\Images\\silenthill.jpg"),itemClass);
	Title title9 = new Title("WRECK IT RAPTH", "A cartoon movie has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\wreckIt.jpg"),itemClass);
	List<Title> titles = Arrays.asList(title,title1,title2,title3,title4,title5,title6,title7,title8,title9);
	titles.forEach(x->System.out.println(new TitleDAO().save(x)));
}
}

