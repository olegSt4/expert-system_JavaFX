package lab3.models;

public class Skill implements Cloneable{
    public final int id;
    public final String name;

    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Skill otherSkill = (Skill) other;
        return otherSkill.id == id && otherSkill.name.equals(name);
    }

    public int hashCode() {
        return id;
    }
}
