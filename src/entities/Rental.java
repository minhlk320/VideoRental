package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
@Entity
public class Rental implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="RentalID")
	private String rentalID;
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;
	private LocalDate date;
	@OneToMany(mappedBy = "rental")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	private List<RentalDetail> details;
	
	public Rental() {
		super();
	}
	public Rental(LocalDate date) {
		super();
		this.date = date;
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
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public List<RentalDetail> getItems() {
		return details;
	}
	public void setItems(List<RentalDetail> items) {
		this.details = items;
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
		return "Rental [rentalID=" + rentalID + ", customer=" + customer + ", date=" + date + ", items=" + details + "]";
	}
	
	
}
