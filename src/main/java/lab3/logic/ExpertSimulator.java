package lab3.logic;

import lab3.controllers.DBController;
import lab3.models.Skill;
import lab3.models.Specialization;
import lab3.models.SpecializationSummary;

import java.util.*;

public class ExpertSimulator {
    private DBController dbController;

    public ExpertSimulator(DBController dbController) {
        this.dbController = dbController;
    }

    public List<SpecializationSummary> getSpecializationsBySkillsArray(String[] skillsArray) {
        Set<Specialization> relevantSpecializations = getRelevantSpecializations(skillsArray);
        List<SpecializationSummary> specSummaries = getSpecSummaries(relevantSpecializations, skillsArray);

        specSummaries.sort((SpecializationSummary ss1, SpecializationSummary ss2) ->
           Double.compare(ss2.getPotentionalInPercentages(), ss1.getPotentionalInPercentages())
        );


        List<SpecializationSummary> topSpecializations;
        if (specSummaries.size() > 3) {
            topSpecializations = specSummaries.subList(0, 3);
        } else {
            topSpecializations = specSummaries.subList(0, specSummaries.size());
        }

        double currentWeight = -1;
        for (SpecializationSummary ss : topSpecializations) {
            if (ss.getPotentionalInPercentages() == currentWeight) {
                System.out.println("There is a match!");
                // TODO: 12.11.2020 Implement additional user prompt
            } else {
                currentWeight = ss.getPotentionalInPercentages();
            }
        }

        return topSpecializations;

    }

    private Set<Specialization> getRelevantSpecializations(String[] skillsArray) {
        Set<Specialization> relevantSpecializations = new HashSet<>();

        for (String skillName : skillsArray) {
            Set<Specialization> skillRelevantSpec = dbController.getRelevantSpecializationsForSkill(skillName);
            relevantSpecializations.addAll(skillRelevantSpec);
        }

        return relevantSpecializations;
    }

    private List<SpecializationSummary> getSpecSummaries(Set<Specialization> relevantSpecializations, String[] skillsArray) {
        List<SpecializationSummary> specSummaries = new ArrayList<>();
        for (Specialization specialization : relevantSpecializations) {
            SpecializationSummary specSummary = dbController.getSpecializationSummary(specialization);

            double currentComplexity = 0;
            for (String skillName : skillsArray) {
                Skill skill = dbController.getSkillByName(skillName);
                if (specSummary.hasSkill(skill)) {
                    currentComplexity += skill.complexity;
                }
            }
            specSummary.setCurrentComplexity(currentComplexity);
            specSummaries.add(specSummary);
        }

        return specSummaries;
    }
}
