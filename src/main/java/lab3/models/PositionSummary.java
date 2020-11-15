package lab3.models;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionSummary {
    private Position position;
    private Map<Skill, Double> worthOfSkills;
    private List<Skill> availableSkills;
    private double conformity;

    public PositionSummary(Position position) {
        this.position = position;

        worthOfSkills = new HashMap<>();
        availableSkills = new ArrayList<>();
    }

    public Position getPosition() {
        return this.position;
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
}