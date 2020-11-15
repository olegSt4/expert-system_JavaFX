package lab3.models;

import java.util.ArrayList;
import java.util.List;

public class SpecializationSummary implements Cloneable{
    private Specialization specialization;
    private List<Skill> skillsList;
    private double currentComplexity;
    private double specializationComplexity;

    public SpecializationSummary(Specialization specialization) {
        this.specialization = specialization;
        skillsList = new ArrayList<>();
    }

    public double getPotentionalInPercentages() throws IllegalStateException {
        if (specializationComplexity == 0) {
            throw new IllegalStateException("The specialization rate is not set yet!");
        }

        double potentional = currentComplexity / specializationComplexity;
        return potentional * 100;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setCurrentComplexity(double value) {
        currentComplexity = value;
    }


    public void addSkillNeeded(Skill skill) {
        skillsList.add(skill);
        specializationComplexity += skill.complexity;
    }

    public void removeSkill(Skill skill) {
        skillsList.remove(skill);
        specializationComplexity -= skill.complexity;
    }

    public boolean hasSkill(Skill skill) {
        return skillsList.contains(skill);
    }

    public SpecializationSummary clone() {
        SpecializationSummary clone = new SpecializationSummary(specialization);
        for (Skill skill : skillsList) {
            clone.addSkillNeeded(skill);
        }
        clone.setCurrentComplexity(currentComplexity);

        return clone;
    }
}
