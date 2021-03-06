package lab3.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to store the full info about position
 * including available user skills
 */
public class PositionSummary {
    private Position position;
    /** Stores skills needed for this position and their values*/
    private Map<Skill, Double> worthOfSkills;

    /** Stores users skills in order to count conformity*/
    private List<Skill> availableSkills;

    /** Shows how the users skills fit this position*/
    private double conformity;

    public PositionSummary(Position position) {
        this.position = position;

        worthOfSkills = new HashMap<>();
        availableSkills = new ArrayList<>();
    }

    public Position getPosition() {
        return this.position;
    }

    public List<Skill> getAvailableSkills() {
        return availableSkills;
    }

    public double getConformity() {
        return this.conformity;
    }

    public void setWorthOfSkills(Map<Skill, Double> worthOfSkills) {
        this.worthOfSkills = worthOfSkills;

        if (availableSkills.size() > 0) {
            conformity = 0;
            for (Skill skill : availableSkills) {
                if (worthOfSkills.containsKey(skill)) {
                    conformity += worthOfSkills.get(skill);
                }
            }
        }
    }

    public void setAvailableSkills(List<Skill> availableSkills) {
        this.availableSkills = availableSkills;

        if (worthOfSkills.size() > 0) {
            conformity = 0;
            for (Skill skill : availableSkills) {
                if (worthOfSkills.containsKey(skill)) {
                    conformity += worthOfSkills.get(skill);
                }
            }
        }
    }

    public void addAvailableSkill(Skill newSkill) {
        if (!availableSkills.contains(newSkill)) {
            availableSkills.add(newSkill);
            if (worthOfSkills.containsKey(newSkill)) {
                conformity += worthOfSkills.get(newSkill);
            }
        }
    }

    /** @return list of skills unoccupied by user*/
    public List<Skill> getFreeSkills() {
        List<Skill> freeSkills = new ArrayList<>();

        if (worthOfSkills.size() > 1) {
            for (Map.Entry<Skill, Double> worthOfSkill : worthOfSkills.entrySet()) {
                if (!availableSkills.contains(worthOfSkill.getKey())) {
                    freeSkills.add(worthOfSkill.getKey());
                }
            }
        }


        return freeSkills;
    }
}