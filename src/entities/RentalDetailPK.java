package entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RentalDetailPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rental;
	private String item;
	
	public RentalDetailPK(String rental, String item) {
		super();
		this.rental = rental;
		this.item = item;
	}
	public RentalDetailPK() {
		super();
	}
	public String getRental() {
		return rental;
	}
	public void setRental(String rental) {
		this.rental = rental;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((rental == null) ? 0 : rental.hashCode());
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
		RentalDetailPK other = (RentalDetailPK) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (rental == null) {
			if (other.rental != null)
				return false;
		} else if (!rental.equals(other.rental))
			return false;
		return true;
	}
	
	
}
