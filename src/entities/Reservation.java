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
public class Reservation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	private String reservationID;
	private LocalDate reservationDate;
	@ManyToOne
	@JoinColumn(name = "CustomerID")
	private Customer customer;
	@ManyToOne
	@JoinColumn(name = "TitleID")
	private Title title;
	private String comment;
	public LocalDate getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reservationID == null) ? 0 : reservationID.hashCode());
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
		Reservation other = (Reservation) obj;
		if (reservationID == null) {
			if (other.reservationID != null)
				return false;
		} else if (!reservationID.equals(other.reservationID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reservation [reservationID=" + reservationID + ", reservationDate=" + reservationDate + ", customer="
				+ customer + ", title=" + title + ", comment=" + comment + "]";
	}
	

}
