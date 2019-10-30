package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Item implements Serializable{
	/**
	 * 
	 */
	public static final String ON_HOLD = "ON_HOLD";
	public static final String ON_SHELF = "ON_SHELF";
	public static final String RENTED = "RENTED";
	public static final String LOST_DAMAGE = "LOST_DAMAGE";
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "sequence_pr_id", strategy = "generators.MyGenerator")
	@GeneratedValue(generator = "sequence_pr_id")
	@Column(name="ItemID")
	private String itemID;
	@ManyToOne
	@JoinColumn(name = "TitleID")
	private Title title;
	private String status;
	@ManyToOne
	@JoinColumn(name = "ItemClassID")
	private Rate itemClass;
	
	public Item(Title title, String status, Rate itemClass) {
		super();
		this.title = title;
		this.status = status;
		this.itemClass = itemClass;
	}
	public Item() {
		super();
	}
	public Title getTitle() {
		return title;
	}
	public void setTitle(Title title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	
	
	public Rate getItemClass() {
		return itemClass;
	}
	public void setItemClass(Rate itemClass) {
		this.itemClass = itemClass;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemID == null) ? 0 : itemID.hashCode());
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
		Item other = (Item) obj;
		if (itemID == null) {
			if (other.itemID != null)
				return false;
		} else if (!itemID.equals(other.itemID))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Item{" +
				"itemID='" + itemID + '\'' +
				", title=" + title +
				", status=" + status +
				", itemClass=" + itemClass +
				'}';
	}
}
