package com.example.lab6.task1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;


//test
//        a: 1.0
//        b: 2.0
//        f(x): "Math.sin(x)"
//        g(x): "Math.cos(x)"
//        Range From: 0.0
//        Range To: 10.0

public class FunctionPlotter extends Application {

    private TextField textFieldA;
    private TextField textFieldB;
    private TextField textFieldF;
    private TextField textFieldG;
    private TextField textFieldRangeFrom;
    private TextField textFieldRangeTo;
    private LineChart<Number, Number> lineChart;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Function Plotter");

        // Створення елементів інтерфейсу
        textFieldA = new TextField();
        textFieldB = new TextField();
        textFieldF = new TextField();
        textFieldG = new TextField();
        textFieldRangeFrom = new TextField();
        textFieldRangeTo = new TextField();
        Button plotButton = new Button("Plot");
        Button clearButton = new Button("Clear");

        // Створення графіку
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Function Plot");

        plotButton.setOnAction(event -> plotFunctions());
        clearButton.setOnAction(event -> clearInputs());

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("a:"), 0, 0);
        gridPane.add(textFieldA, 1, 0);
        gridPane.add(new Label("b:"), 0, 1);
        gridPane.add(textFieldB, 1, 1);
        gridPane.add(new Label("f(x):"), 0, 2);
        gridPane.add(textFieldF, 1, 2);
        gridPane.add(new Label("g(x):"), 0, 3);
        gridPane.add(textFieldG, 1, 3);
        gridPane.add(new Label("Range From:"), 0, 4);
        gridPane.add(textFieldRangeFrom, 1, 4);
        gridPane.add(new Label("Range To:"), 0, 5);
        gridPane.add(textFieldRangeTo, 1, 5);
        gridPane.add(plotButton, 0, 6);
        gridPane.add(clearButton, 1, 6);
        gridPane.add(lineChart, 0, 7, 2, 1);

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void plotFunctions() {
        try {
            double a = Double.parseDouble(textFieldA.getText());
            double b = Double.parseDouble(textFieldB.getText());
            String f = textFieldF.getText();
            String g = textFieldG.getText();
            double rangeFrom = Double.parseDouble(textFieldRangeFrom.getText());
            double rangeTo = Double.parseDouble(textFieldRangeTo.getText());

            lineChart.getData().clear();

            try (Context context = Context.create()) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName("h(x) = f(x + a) + g(x - b)");
                for (double x = rangeFrom; x <= rangeTo; x += 0.1) {
                    double fx = evaluateFunction(context, f, x + a);
                    double gx = evaluateFunction(context, g, x - b);
                    double hx = fx + gx;
                    series.getData().add(new XYChart.Data<>(x, hx));
                }

                lineChart.getData().add(series);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private double evaluateFunction(Context context, String function, double x) {
        String expression = function.replace("x", String.valueOf(x));
        Value result = context.eval("js", expression);
        return result.asDouble();
    }

    private void clearInputs() {
        textFieldA.clear();
        textFieldB.clear();
        textFieldF.clear();
        textFieldG.clear();
        textFieldRangeFrom.clear();
        textFieldRangeTo.clear();
        lineChart.getData().clear();
    }
}
