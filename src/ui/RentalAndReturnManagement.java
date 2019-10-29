package ui;

import daos.ItemDAO;
import daos.RentalDAO;
import entities.Customer;
import entities.Item;
import entities.Rental;
import entities.RentalDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalAndReturnManagement {
    private RentalDAO rentalDAO = new RentalDAO();
    private ItemDAO itemDAO = new ItemDAO();
    /**
     *
     * @param customer
     * @param itemList
     * @return true/false
     * @description This function return a true value if a rental transaction was created successfully else return false
     */
    public boolean Rental(Customer customer, List<Item> itemList){
        Rental rental = new Rental(LocalDate.now());
        List<RentalDetail> rentalDetailList = new ArrayList<>();
        itemList.forEach(x->{
            rentalDetailList.add(new RentalDetail(rental, x, x.getItemClass().getRentalRate()));
        });
        rental.setCustomer(customer);
        rental.setItems(rentalDetailList);
        if(rentalDAO.save(rental))
            return true;
        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean Return(String id){
        Item item = itemDAO.getById(Item.class,id);

        return true;
    }
}
