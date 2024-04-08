package com.example.labajavaupdate;

import javafx.scene.shape.Polygon;

public class Arrow {
    private Polygon arrow;
    private double layoutX;
    private double layoutY;
    public Arrow(Polygon arrow) {
        this.arrow = arrow;
    }
    public Arrow(Polygon arrow, double layoutX, double layoutY) {
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.arrow = arrow;
    }
    public void move(int a){
        layoutX += a;
        arrow.setLayoutX(layoutX);
    }

    public double getLayoutX() {
        return arrow.getLayoutX();
    }

    public double getLayoutY() {
        return arrow.getLayoutY();
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
        arrow.setLayoutX(layoutX);
    }
    public void resetPosArrow(double x, Arrow arrow){
        arrow.setLayoutX(x);
    }
}
