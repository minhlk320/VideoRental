package ui;

import daos.ItemDAO;
import daos.LateChargeDAO;
import daos.RentalDAO;
import daos.ReservationDAO;
import entities.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class RentalAndReturnManagement {
    private RentalDAO rentalDAO = new RentalDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private LateChargeDAO lateChargeDAO = new LateChargeDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    /**
     *
     * @param customer
     * @param itemList
     * @return true/false
     * @description This function return a true value if a rental transaction was created successfully else return false
     */
//    public boolean rental(Customer customer, List<Item> itemList){
//        Rental rental = new Rental(LocalDate.now());
//        List<RentalDetail> rentalDetailList = new ArrayList<>();
//        itemList.forEach(x->{
//            x.setStatus(Item.RENTED);
//            rentalDetailList.add(new RentalDetail(rental, x, x.getTitle().getItemClass().getRentalRate(),x.getTitle().getItemClass().getRentalPeriod(),x.getTitle().getItemClass().getLateRate()));
//        });
//        rental.setCustomer(customer);
//        rental.setItems(rentalDetailList);
//        if(rentalDAO.save(rental)){
//            itemList.forEach(x->new ItemDAO().update(x));
//            return true;
//        }
//        return false;
//    }

    /**
     *
     * @param id
     * @return
     * @description record the return of one Item and check the return is
     * late or not and add late charge to the item's customer
     */


    /**
     *
     * @param item
     * @param customer
     * @description add late charge to customer along with the late return item
     */







}
