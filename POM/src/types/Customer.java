package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {

	private final StringProperty id;
	private final StringProperty name;
	private final StringProperty ranking;
	private final StringProperty comment;
	

	public Customer(String id, String name, String ranking,String comment) {
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.ranking = new SimpleStringProperty(ranking);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public Customer() {this(null,null,null,null);}

	public StringProperty idProperty() {
		return id;
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty rankingProperty() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking.set(ranking);
	}

	public StringProperty commentProperty() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment.set(comment);
	}

}
