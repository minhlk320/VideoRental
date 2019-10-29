package ui;

import daos.ItemDAO;
import daos.LateChargeDAO;
import daos.RentalDAO;
import entities.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalAndReturnManagement {
    private RentalDAO rentalDAO = new RentalDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private LateChargeDAO lateChargeDAO = new LateChargeDAO();
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
     * @description record the return of one Item and check the return is
     * late or not and add late charge to the item's customer
     */
    public boolean Return(String id){
        Item item = itemDAO.getById(Item.class,id);
        Rental lastestRental = rentalDAO.getLatestRentalByItemID(id);
        Customer customer = lastestRental.getCustomer();
        RentalDetail rentalDetailofItem = null;
        List<RentalDetail> rentalDetailList = lastestRental.getItems();
        for(int i = 0; i<rentalDetailList.size();i++){
            if(rentalDetailList.get(i).getItem().equals(item)){
                rentalDetailofItem = rentalDetailList.get(i);
            }
        }
        LocalDate rentedDate = lastestRental.getDate();
        LocalDate currentDate = LocalDate.now();

        if(currentDate.isAfter(rentedDate.plusDays(rentalDetailofItem.getRentalPeriod()))){
            LocalDate dueOn = rentedDate.plusDays(rentalDetailofItem.getRentalPeriod());
            Duration diff = Duration.between(dueOn.atStartOfDay(), currentDate.atStartOfDay());
            int numOfOverDueDay = (int) diff.toDays();
            double totalAmout = numOfOverDueDay * rentalDetailofItem.getLateRate();
            addLateCharge(item,customer,totalAmout,dueOn);
        }
        //Activate 6b to check on-hold if return false set "on-shelf" to the item else "on-hold"
        return true;
    }

    /**
     *
     * @param item
     * @param customer
     * @description add late charge to customer along with the late return item
     */
    private void addLateCharge(Item item, Customer customer, double totalAmount, LocalDate dueOn){
        LateCharge lateCharge = new LateCharge(LocalDate.now(),dueOn,item,customer,totalAmount);
        if( lateChargeDAO.save(lateCharge))
            System.out.println("Late charge added!");
    }
}
