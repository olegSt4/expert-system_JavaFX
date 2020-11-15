package lab3.logic;

import lab3.controllers.DBController;
import lab3.models.Position;
import lab3.models.Skill;
import lab3.models.PositionSummary;

import java.util.*;

public class ExpertSimulator {
    private DBController dbController;

    public ExpertSimulator(DBController dbController) {
        this.dbController = dbController;
    }

    public List<PositionSummary> getTopPositionsBySkillsArray(String[] skillsArray, int topAmount) {
        List<Skill> skillsList = dbController.getSkillsListByStringArray(skillsArray);
        Set<Position> relevantPositions = dbController.getRelevantPositionsForSkills(skillsList);
        List<PositionSummary> posSummaries = getPositionSummaries(relevantPositions, skillsList);

        posSummaries.sort((PositionSummary ss1, PositionSummary ss2) ->
           Double.compare(ss2.getConformity(), ss1.getConformity())
        );


        List<PositionSummary> topPositions;
        if (posSummaries.size() > topAmount) {
            topPositions = posSummaries.subList(0, 3);
        } else {
            topPositions = posSummaries.subList(0, posSummaries.size());
        }

        double currentWeight = -1;
        for (PositionSummary ss : topPositions) {
            if (ss.getConformity() == currentWeight) {
                System.out.println("There is a match!");
                // TODO: 12.11.2020 Implement additional user prompt
            } else {
                currentWeight = ss.getConformity();
            }
        }

        return topPositions;

    }

    private List<PositionSummary> getPositionSummaries(Set<Position> relevantPositions, List<Skill> skillsList) {
        List<PositionSummary> posSummaries = new ArrayList<>();

        for (Position position : relevantPositions) {
            PositionSummary posSummary = dbController.getPositionSummary(position);
            posSummary.setAvailableSkills(skillsList);

            posSummaries.add(posSummary);

        }

        return posSummaries;
    }
}
