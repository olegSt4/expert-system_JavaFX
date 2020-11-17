package lab3.models;

/** Used to store the instance of Skill from DB*/
public class Skill implements Cloneable{
    public final int id;
    public final String name;
    public final String type;

    public Skill(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Skill otherSkill = (Skill) other;
        return otherSkill.id == id && otherSkill.name.equals(name) && otherSkill.type.equals(type);
    }

    public int hashCode() {
        return id;
    }
}
