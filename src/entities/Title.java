package entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class Title implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	@Column(name="TitleID")
	private String titleID;
	private String titleName;
	private double rentalRate;
	private LocalDate rentalPeriod;
	
	public Title() {
		super();
	}
	public Title(String titleName, double rentalRate, LocalDate rentalPeriod) {
		super();
		this.titleName = titleName;
		this.rentalRate = rentalRate;
		this.rentalPeriod = rentalPeriod;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public double getRentalRate() {
		return rentalRate;
	}
	public void setRentalRate(double rentalRate) {
		this.rentalRate = rentalRate;
	}
	public LocalDate getRentalPeriod() {
		return rentalPeriod;
	}
	public void setRentalPeriod(LocalDate rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}
	
	public String getTitleID() {
		return titleID;
	}
	public void setTitleID(String titleID) {
		this.titleID = titleID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((titleID == null) ? 0 : titleID.hashCode());
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
		Title other = (Title) obj;
		if (titleID == null) {
			if (other.titleID != null)
				return false;
		} else if (!titleID.equals(other.titleID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Title [titleID=" + titleID + ", titleName=" + titleName + ", rentalRate=" + rentalRate
				+ ", rentalPeriod=" + rentalPeriod + "]";
	}
	

}
