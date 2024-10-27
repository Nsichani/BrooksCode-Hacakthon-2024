import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StudentInfo {
    int grade;
    String major;
    String university1;
    String university2;
    String university3;

    StudentInfo(int grade, String major, String university1, String university2, String university3) {
        this.grade = grade;
        this.major = major;
        this.university1 = university1;
        this.university2 = university2;
        this.university3 = university3;
    }
}

public class CrowdsourcingApp2 extends Application {

    private List<StudentInfo> students = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Crowdsourcing Data Collector");

        TextField gradeField = new TextField();
        TextField majorField = new TextField();
        TextField uniField1 = new TextField();
        TextField uniField2 = new TextField();
        TextField uniField3 = new TextField();

        Label responseLabel = new Label();
        Button addButton = new Button("Add Student Info");
        Button saveButton = new Button("Save Data & University Percentages");

        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setVgap(8);
        inputGrid.setHgap(10);

        inputGrid.add(new Label("Grade:"), 0, 0);
        inputGrid.add(gradeField, 1, 0);
        inputGrid.add(new Label("Major:"), 0, 1);
        inputGrid.add(majorField, 1, 1);
        inputGrid.add(new Label("Top University 1:"), 0, 2);
        inputGrid.add(uniField1, 1, 2);
        inputGrid.add(new Label("Top University 2:"), 0, 3);
        inputGrid.add(uniField2, 1, 3);
        inputGrid.add(new Label("Top University 3:"), 0, 4);
        inputGrid.add(uniField3, 1, 4);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(inputGrid, addButton, saveButton, responseLabel);

        addButton.setOnAction(e -> {
            try {
                int grade = Integer.parseInt(gradeField.getText());
                if (grade < 10 || grade > 12) {
                    responseLabel.setText("Please enter a valid grade (10, 11, or 12).");
                    return;
                }
                String major = majorField.getText();
                String university1 = uniField1.getText();
                String university2 = uniField2.getText();
                String university3 = uniField3.getText();
                students.add(new StudentInfo(grade, major, university1, university2, university3));
                responseLabel.setText("Student information added successfully.");
                gradeField.clear();
                majorField.clear();
                uniField1.clear();
                uniField2.clear();
                uniField3.clear();
            } catch (NumberFormatException ex) {
                responseLabel.setText("Grade must be a number.");
            }
        });

        saveButton.setOnAction(e -> {
            saveDataToFile(responseLabel);
            saveUniversityPercentages(responseLabel);
        });

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveDataToFile(Label responseLabel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("crowdsourced_data.txt"))) {
            writer.write("--- Crowdsourced Student Information ---\n");
            for (StudentInfo student : students) {
                writer.write("Grade: " + student.grade + "\n");
                writer.write("Desired Major: " + student.major + "\n");
                writer.write("Top 3 Universities:\n");
                writer.write("1. " + student.university1 + "\n");
                writer.write("2. " + student.university2 + "\n");
                writer.write("3. " + student.university3 + "\n");
                writer.write("---------------------------------------\n");
            }
            responseLabel.setText("Data saved to crowdsourced_data.txt.");
        } catch (IOException ex) {
            responseLabel.setText("Error saving data to file.");
        }
    }

    private void saveUniversityPercentages(Label responseLabel) {
        Map<String, Integer> universityCount = new HashMap<>();
        int totalSelections = students.size() * 3;

        for (StudentInfo student : students) {
            universityCount.put(student.university1, universityCount.getOrDefault(student.university1, 0) + 1);
            universityCount.put(student.university2, universityCount.getOrDefault(student.university2, 0) + 1);
            universityCount.put(student.university3, universityCount.getOrDefault(student.university3, 0) + 1);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("university_percentages.txt"))) {
            writer.write("--- University Selection Percentages ---\n");
            for (Map.Entry<String, Integer> entry : universityCount.entrySet()) {
                double percentage = (entry.getValue() / (double) totalSelections) * 100;
                writer.write(entry.getKey() + ": " + String.format("%.2f", percentage) + "%\n");
            }
            responseLabel.setText("University selection percentages saved to university_percentages.txt.");
        } catch (IOException ex) {
            responseLabel.setText("Error saving university percentages to file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
