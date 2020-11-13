package lab3.models;

public class Skill implements Cloneable{
    public final int id;
    public final String name;
    public final int complexity;

    public Skill(int id, String name, int complexity) {
        this.id = id;
        this.name = name;
        this.complexity = complexity;
    }

    public Skill clone() {
        return new Skill(id, name, complexity);
    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Skill otherSkill = (Skill) other;
        if (otherSkill.id != id || !otherSkill.name.equals(name) || otherSkill.complexity != complexity) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return id;
    }
}
