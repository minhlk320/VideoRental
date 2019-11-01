package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
@Entity
public class Rental implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	@Column(name="RentalID")
	private String rentalID;
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;
	@Column(name="RentalDate")
	private LocalDate rentalDate;
	@OneToMany(mappedBy = "rental")
	@Cascade(value = { CascadeType.ALL })
	private List<RentalDetail> rentalDetails;
	
	public Rental() {
		super();
	}
	public Rental(LocalDate date) {
		super();
		this.rentalDate = date;
	}
	public String getRentalID() {
		return rentalID;
	}
	public void setRentalID(String rentalID) {
		this.rentalID = rentalID;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDate getDate() {
		return rentalDate;
	}
	public void setDate(LocalDate date) {
		this.rentalDate = date;
	}
	public List<RentalDetail> getItems() {
		return rentalDetails;
	}
	public void setItems(List<RentalDetail> items) {
		this.rentalDetails = items;
	}
	public double getTotal(){
		double total = 0;
		for (RentalDetail x : rentalDetails){
			total+=x.getUnitPrice();
		}
		return  total;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rentalID == null) ? 0 : rentalID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rental other = (Rental) obj;
		if (rentalID == null) {
			if (other.rentalID != null)
				return false;
		} else if (!rentalID.equals(other.rentalID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Rental [rentalID=" + rentalID + ", customer=" + customer + ", date=" + rentalDate + ", items=" + rentalDetails + "]";
	}
	
	
}
