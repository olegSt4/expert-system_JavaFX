package lab3.models;

import javafx.scene.image.Image;

/**
 * Used to store the instance of position from DB
 */
public class Position {
    public final int id;
    public final String name;
    private final Image image;

    public Position(int id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Position otherPosition = (Position) other;
        return this.id == otherPosition.id && this.name.equals(otherPosition.name);
    }

    public int hashCode() {
        return this.id;
    }


}