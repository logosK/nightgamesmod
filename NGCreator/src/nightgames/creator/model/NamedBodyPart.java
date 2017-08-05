package nightgames.creator.model;

import java.util.Arrays;
import java.util.Collection;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.BodyPartMod;

public class NamedBodyPart {

	private SimpleObjectProperty<BodyPart> part;
	private SimpleStringProperty name;
	
	public NamedBodyPart() {
		this(null, "<empty>");
	}
	
	public NamedBodyPart(BodyPart part, String name, BodyPartMod... mods) {
		part.getMods().addAll(Arrays.asList(mods));
		this.part = new SimpleObjectProperty<>(part);
		this.name = new SimpleStringProperty(name);
	}

	public final SimpleObjectProperty<BodyPart> partProperty() {
		return this.part;
	}
	

	public final BodyPart getPart() {
		return this.partProperty().get();
	}
	

	public final void setPart(final BodyPart part) {
		this.partProperty().set(part);
	}
	

	public final SimpleStringProperty nameProperty() {
		return this.name;
	}
	

	public final String getName() {
		return this.nameProperty().get();
	}
	

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	
	
	
	
}
