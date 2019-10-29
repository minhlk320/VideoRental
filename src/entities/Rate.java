package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class Rate implements Serializable{
	/**
	 * 
	 */
	public static final String MOVIE = "Movie", VIDEOGAME = "Video Game";
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	private String itemClassID;
	private String itemClassName;
	private double rentalRate;
	private int rentalPeriod;
	private double lateRate;
	
	public Rate() {
		super();
	}
	public Rate(String itemClassName, double rentalRate, int rentalPeriod, double lateRate) {
		super();
		this.itemClassName = itemClassName;
		this.rentalRate = rentalRate;
		this.rentalPeriod = rentalPeriod;
		this.lateRate = lateRate;
	}
	public String getItemClassID() {
		return itemClassID;
	}
	public void setItemClassID(String itemClassID) {
		this.itemClassID = itemClassID;
	}
	public String getItemClassName() {
		return itemClassName;
	}
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}
	public double getRentalRate() {
		return rentalRate;
	}
	public void setRentalRate(double rentalRate) {
		this.rentalRate = rentalRate;
	}
	public int getRentalPeriod() {
		return rentalPeriod;
	}
	public void setRentalPeriod(int rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}
	public double getLateRate() {
		return lateRate;
	}
	public void setLateRate(double lateRate) {
		this.lateRate = lateRate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemClassID == null) ? 0 : itemClassID.hashCode());
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
		Rate other = (Rate) obj;
		if (itemClassID == null) {
			if (other.itemClassID != null)
				return false;
		} else if (!itemClassID.equals(other.itemClassID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ItemClass [itemClassID=" + itemClassID + ", itemClassName=" + itemClassName + ", rentalRate="
				+ rentalRate + ", rentalPeriod=" + rentalPeriod + ", lateRate=" + lateRate + "]";
	}
	

}
