package entities;

import java.io.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javafx.scene.image.Image;
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
	private String description;
	@Column(name="Image", unique = false, length = 800000)
	private byte[] image;

	public Title() {
		super();
	}

	public Title(String titleName, String description, File fimage) {
		super();
		this.titleName = titleName;
		this.description = description;
		setImage(fimage);
	}

	public String getDesciption() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getImage() {
		return new Image(new ByteArrayInputStream(image));
	}

	public void setImage(File fimage) {
		byte[] bFile = new byte[(int)fimage.length()];
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStream = new FileInputStream(fimage);
			fileInputStream.read(bFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.image = bFile;
	}

	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
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
		return "Title [titleID=" + titleID + ", titleName=" + titleName + ", description=" + description + "]";
	}



}
