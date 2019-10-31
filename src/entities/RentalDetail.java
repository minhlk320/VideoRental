package entities;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(RentalDetailPK.class)
public class RentalDetail implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne
	@JoinColumn(name = "RentalID", referencedColumnName = "RentalID")
	private Rental rental;
	@Id
	@ManyToOne
	@JoinColumn(name = "ItemID", referencedColumnName = "ItemID")
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	private Item item;
	private double unitPrice;
	private int rentalPeriod;
	private double lateRate;
	public RentalDetail() {
		super();
	}

	public RentalDetail(Rental rental, Item item, double unitPrice) {
		super();
		this.rental = rental;
		this.item = item;
		this.unitPrice = unitPrice;
	}

	public Rental getRental() {
		return rental;
	}
	public void setRental(Rental rental) {
		this.rental = rental;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getRentalPeriod() {	return rentalPeriod;}
	public void setRentalPeriod(int rentalPeriod) {	this.rentalPeriod = rentalPeriod; }
	public double getLateRate() {	return lateRate;}
	public void setLateRate(double lateRate) { this.lateRate = lateRate;	}

	@Override
	public String toString() {
		return "RentalDetail [item=" + item + ", unitPrice=" + unitPrice + "]";
	}
}
