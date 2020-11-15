package lab3.controllers;

import javafx.scene.image.Image;
import lab3.models.Position;
import lab3.models.Skill;
import lab3.models.PositionSummary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class DBController {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/expert-system-db";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "postgre5541";

    private Map<String, Skill> skillsCache = new HashMap<>();
    private Map<Skill, Set<Position>> relevantPositionsCache = new HashMap<>();
    private Map<String, PositionSummary> positionSummariesCache = new HashMap<>();

    public List<Skill> getSkillsListByStringArray(String[] skillsName) {
        List<Skill> skillsList = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);

            for (String skillName : skillsName) {
                if (skillsCache.containsKey(skillName)) {
                    skillsList.add(skillsCache.get(skillName));
                } else {
                    String SQLQuery = "SELECT s.id FROM skills s where s.name = ?";
                    try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                        PreparedStatement ps = connection.prepareStatement(SQLQuery)) {
                        ps.setString(1, skillName);

                        ResultSet resultSet = ps.executeQuery();
                        resultSet.next();

                        Skill newSkill = new Skill(resultSet.getInt(1), skillName);
                        skillsCache.put(skillName, newSkill);

                        skillsList.add(newSkill);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("There is a problem with driver class!");
            ex.printStackTrace();
        }

        return skillsList;
    }

    public Set<Position> getRelevantPositionsForSkills(List<Skill> skills) {
        Set<Position> relevantPositions = new HashSet<>();

        try {
            Class.forName(JDBC_DRIVER);

            for (Skill skill : skills) {
                if (relevantPositionsCache.containsKey(skill)) {
                    relevantPositions.addAll(relevantPositionsCache.get(skill));
                } else {
                    String SQLQuery = "SELECT DISTINCT p.id, p.name, p.image FROM positions p " +
                            "JOIN pos_requirements pr ON p.id = pr.pos_id " +
                            "JOIN skills sk ON sk.id = pr.skill_id " +
                            "WHERE sk.name = ?";
                    try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                        PreparedStatement ps = connection.prepareStatement(SQLQuery)) {
                        ps.setString(1, skill.name);

                        ResultSet resultSet = ps.executeQuery();

                        Set<Position> skillRelevantPositions = new HashSet<>();
                        while(resultSet.next()) {
                            int posId = resultSet.getInt(1);
                            String posName = resultSet.getString(2);
                            InputStream imageByteStream = new ByteArrayInputStream(resultSet.getBytes(3));
                            Image posImage = new Image(imageByteStream);

                            Position newPosition = new Position(posId, posName, posImage);
                            skillRelevantPositions.add(newPosition);
                        }

                        if (skillRelevantPositions.size() > 0) {
                            relevantPositions.addAll(skillRelevantPositions);
                            relevantPositionsCache.put(skill, skillRelevantPositions);
                        }


                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("There is a problem with driver class!");
            ex.printStackTrace();
        }

          return relevantPositions;
       }

    public PositionSummary getPositionSummary(Position position)  {
        if (positionSummariesCache.containsKey(position.name)) {
            return positionSummariesCache.get(position.name);
        } else {
            PositionSummary positionSummary = new PositionSummary(position);
            try {
                Class.forName(JDBC_DRIVER);

                String SQLQuery = "SELECT sk.id, sk.name, pr.worth FROM positions p " +
                        "JOIN pos_requirements pr ON p.id = pr.pos_id " +
                        "JOIN skills sk ON sk.id = pr.skill_id " +
                        "WHERE p.name = ?";
                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
                    preparedStatement.setString(1, position.name);

                    Map<Skill, Double> worthOfSkills = new HashMap<>();
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int skillId = resultSet.getInt(1);
                        String skillName = resultSet.getString(2);
                        double skillWorth = resultSet.getDouble(3);
                        Skill newSkill = new Skill(skillId, skillName);

                        if (!skillsCache.containsKey(skillName)) {
                            skillsCache.put(skillName, newSkill);
                        }

                        worthOfSkills.put(newSkill, skillWorth);
                    }

                    positionSummary.setWorthOfSkills(worthOfSkills);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("There is a problem with driver class!");
                ex.printStackTrace();
            }

            positionSummariesCache.put(position.name, positionSummary);
            return positionSummary;
        }
    }


}
