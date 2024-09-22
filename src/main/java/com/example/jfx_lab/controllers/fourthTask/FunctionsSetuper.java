package com.example.jfx_lab.controllers.fourthTask;

import javafx.scene.chart.XYChart;

public class FunctionsSetuper {
    public static XYChart.Series<Number, Number> sinFunc() {
        XYChart.Series<Number, Number> sinSeries = new XYChart.Series<>();

        for (double x = -3; x <=3; x += 0.05) {
            sinSeries.getData().add(new XYChart.Data<>(x, Math.sin(x)));
        }
        return sinSeries;
    }
    public static XYChart.Series<Number, Number> cosFunc() {
        XYChart.Series<Number, Number> cosSeries = new XYChart.Series<>();

        for (double x = -3; x <=3; x += 0.05) {
            cosSeries.getData().add(new XYChart.Data<>(x, Math.cos(x)));
        }
        return cosSeries;
    }
    public static XYChart.Series<Number, Number> expFunc() {
        XYChart.Series<Number, Number> expSeries = new XYChart.Series<>();

        for (double x = -3; x <=3; x += 0.05) {
            expSeries.getData().add(new XYChart.Data<>(x, Math.exp(x)));
        }
        return expSeries;
    }
}
