package lab3.controllers;

import lab3.models.Skill;
import lab3.models.Specialization;
import lab3.models.SpecializationSummary;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBController {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/expert-system-db";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "postgre5541";

    private Map<String, Skill> skillsCache = new HashMap<>();
    private Map<String, Set<Specialization>> relevantSpecCache = new HashMap<>();
    private Map<String, SpecializationSummary> specSummariesCache = new HashMap<>();

    public Skill getSkillByName(String skillName) {
        if (skillsCache.containsKey(skillName)) {
            return skillsCache.get(skillName).clone();
        } else {
            try {
                Class.forName(JDBC_DRIVER);

                String SQLQuery = "SELECT s.id, s.complexity FROM skills s where name = ?";
                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
                    preparedStatement.setString(1, skillName);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int skillId = resultSet.getInt(1);
                        int skillComplexity = resultSet.getInt(2);
                        Skill newSkill = new Skill(skillId, skillName, skillComplexity);
                        skillsCache.put(skillName, newSkill);

                        return newSkill.clone();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("There is a problem with driver class!");
                ex.printStackTrace();
            }

            return new Skill(0, "", 0);
        }
    }

    public Set<Specialization> getRelevantSpecializationsForSkill(String skillName) {
       if (relevantSpecCache.containsKey(skillName)) {
           return relevantSpecCache.get(skillName);
       } else {
           Set<Specialization> relevantSpecializations = new HashSet<>();

           try {
               Class.forName(JDBC_DRIVER);

               String SQLQuery = "SELECT DISTINCT sp.id, sp.name FROM specializations sp " +
                       "JOIN spec_requirements sr ON sp.id = sr.spec_id " +
                       "JOIN skills sk ON sk.id = sr.skill_id " +
                       "WHERE sk.name = ?";
               try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
                   preparedStatement.setString(1, skillName);

                   ResultSet resultSet = preparedStatement.executeQuery();

                   while(resultSet.next()) {
                       int specializationId = resultSet.getInt(1);
                       String specializationName = resultSet.getString(2);
                       Specialization specialization = new Specialization(specializationId, specializationName);
                       relevantSpecializations.add(specialization);
                   }
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
           } catch (ClassNotFoundException ex) {
               System.out.println("There is a problem with driver class!");
               ex.printStackTrace();
           }

           relevantSpecCache.put(skillName, relevantSpecializations);
           return relevantSpecializations;
       }
    }

    public SpecializationSummary getSpecializationSummary(Specialization specialization) {
        if (specSummariesCache.containsKey(specialization.name)) {
            return specSummariesCache.get(specialization.name).clone();
        } else {
            try {
                Class.forName(JDBC_DRIVER);

                String SQLQuery = "SELECT sk.id, sk.name, sk.complexity FROM specializations sp " +
                        "JOIN spec_requirements sr ON sp.id = sr.spec_id " +
                        "JOIN skills sk ON sk.id = sr.skill_id " +
                        "WHERE sp.name = ?";
                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery)) {
                    preparedStatement.setString(1, specialization.name);
                    SpecializationSummary specializationSummary = new SpecializationSummary(specialization);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int skillId = resultSet.getInt(1);
                        String skillName = resultSet.getString(2);
                        int skillComplexity = resultSet.getInt(3);
                        Skill newSkill = new Skill(skillId, skillName, skillComplexity);

                        specializationSummary.addSkillNeeded(newSkill);
                    }

                    return specializationSummary;
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("There is a problem with driver class!");
                ex.printStackTrace();
            }
        }

        return new SpecializationSummary(specialization);
    }


}
