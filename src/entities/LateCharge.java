package entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class LateCharge implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	private String lateChargeID;
	private LocalDate returnDate;
	private LocalDate dueOn;
	private LocalDate purchaseDate;
	private double totalAmount;
	@ManyToOne
	@JoinColumn(name = "TitleID")
	private Title title;
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;
	
	public LateCharge() {
		super();
	}
	
	public LateCharge(LocalDate returnDate,LocalDate dueOn, Title title, Customer customer, double totalAmount) {
		super();
		this.returnDate = returnDate;
		this.dueOn = dueOn;
		this.totalAmount = totalAmount;
		this.title = title;
		this.customer = customer;
		this.purchaseDate =null;
	}

	public String getLateChargeID() {
		return lateChargeID;
	}
	public void setLateChargeID(String lateChargeID) {
		this.lateChargeID = lateChargeID;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	public Title getItem() {	return title;}
	public void setItem(Title item) {this.title = item;	}
	public LocalDate getDueOn() { return dueOn;	}
	public void setDueOn(LocalDate dueOn) {	this.dueOn = dueOn;	}
	public double getTotalAmount() {return totalAmount;	}
	public void setTotalAmount(double totalAmount) {this.totalAmount = totalAmount;	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDate getPurchaseDate() {return purchaseDate;}
	public void setPurchaseDate(LocalDate purchaseDate) {this.purchaseDate = purchaseDate;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lateChargeID == null) ? 0 : lateChargeID.hashCode());
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
		LateCharge other = (LateCharge) obj;
		if (lateChargeID == null) {
			if (other.lateChargeID != null)
				return false;
		} else if (!lateChargeID.equals(other.lateChargeID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LateCharge{" +
				"lateChargeID='" + lateChargeID + '\'' +
				", returnDate=" + returnDate +
				", dueOn=" + dueOn +
				", purchaseDate=" + purchaseDate +
				", totalAmount=" + totalAmount +
				", Title=" + title +
				", customer=" + customer +
				'}';
	}
}
