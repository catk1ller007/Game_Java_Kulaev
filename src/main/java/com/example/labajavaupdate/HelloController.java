package com.example.labajavaupdate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.List;

import static java.lang.Math.sqrt;


public class HelloController {
    public GameState state = GameState.OFF;
    public ArrowState arrowState = ArrowState.NOSHOOT;
    @FXML
    Circle Big_Circle;
    @FXML
    Circle Small_Circle;
    @FXML
    Polygon Strela;
    Thread gameThread;
    Arrow arrow = new Arrow(Strela);
    public int orientationBig = 1;
    public int orientationSmall = 1;
    public int shootCount = 0;
    public int shootScore = 0;

    @FXML
    Pane gameWindow;
    @FXML
    public Label shootsScoreLable;
    @FXML
    public Label CountshootsLable;

    @FXML
    public void initialize(){
        arrow = new Arrow(Strela, Strela.getLayoutX(), Strela.getLayoutY());
    }

    @FXML
    public void StartButton(){
        if (state == GameState.OFF){
            state = GameState.ON;
            gameThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (state == GameState.PAUSE)
                            pause();

                        Platform.runLater(this::runGame);

                        Thread.sleep(20);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            gameThread.setDaemon(true);
            gameThread.start();
        }
    }
    @FXML
    public void ShootButton(){
        if (state == GameState.ON && arrowState == ArrowState.NOSHOOT){
            arrowState = ArrowState.SHOOT;
            Platform.runLater(this::ShotCounter);
        }
    }

    @FXML
    void PauseButton() {
        if (state == GameState.ON) {
            state = GameState.PAUSE;
        } else if (state == GameState.PAUSE) {
            resume();
        }
    }
    synchronized void resume() {
        state = GameState.ON;
        this.notifyAll();
    }
    synchronized void pause() throws InterruptedException {
        this.wait();
    }
    @FXML
    void ExitButton(){
        if (state == GameState.ON){
            state = GameState.OFF;
            if (gameThread != null) {
                gameThread.interrupt();
                try {
                    gameThread.join(); // Дожидаемся завершения потока
                    shootCount = 0;
                    shootScore = 0;
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                if (!gameThread.isAlive()) {
                    Platform.runLater(() -> {
                        Big_Circle.setLayoutY(177);
                        Small_Circle.setLayoutY(177);
                        resetScoreAndCount();
                        arrow.resetPosArrow(74,  arrow);
                    });
                }
            }
        }
    }

    // Добавления очков за попадания в мишень
    void ScoreHitTarget(int num){
        shootScore = Integer.parseInt(shootsScoreLable.getText());
        shootsScoreLable.setText(Integer.toString(shootScore + num));
    }

    // Сброс таблицы счета
    void resetScoreAndCount(){
        shootScore = 0;
        shootsScoreLable.setText(Integer.toString(0));
        shootCount = 0;
        CountshootsLable.setText(Integer.toString(0));

    }
    // Счетчик выстрелов
    void ShotCounter(){
        shootCount = Integer.parseInt(CountshootsLable.getText());
        CountshootsLable.setText(Integer.toString(shootCount + 1));
    }
    void arrowShoot(){
        if (arrowState == ArrowState.SHOOT) {
            arrow.setLayoutX(arrow.getLayoutX() + 5);
            if (arrowHit(Big_Circle)) {
                ScoreHitTarget(1);
                arrowState = ArrowState.NOSHOOT;
                arrow.resetPosArrow(74.0, arrow);
            } else if (arrowHit(Small_Circle)) {
                ScoreHitTarget(2);
                arrowState = ArrowState.NOSHOOT;
                arrow.resetPosArrow(74.0, arrow);
            }
            else if (arrow.getLayoutX() > gameWindow.getWidth()) {
                arrowState = ArrowState.NOSHOOT;
                arrow.resetPosArrow(74.0, arrow);
            }
        }
    }
    void runGame() {
        orientationBig = orientCircle(Big_Circle, 2, gameWindow, orientationBig);
        orientationSmall = orientCircle(Small_Circle, 4, gameWindow, orientationSmall);

        Big_Circle.setLayoutY(Big_Circle.getLayoutY() + orientationBig * 2);
        Small_Circle.setLayoutY(Small_Circle.getLayoutY() + orientationSmall * 4);

        if (arrowState == ArrowState.SHOOT){
            arrowShoot();
        }
    }


    boolean arrowHit(Circle circle) {
        final double arrowX = arrow.getLayoutX();
        final double arrowY = arrow.getLayoutY();
        final double circleX = circle.getLayoutX();
        final double circleY = circle.getLayoutY();

        return sqrt((arrowX - circleX) * (arrowX - circleX) + (arrowY - circleY) * (arrowY - circleY)) < circle.getRadius();
    }
    // Функция для определения ориентации круга относительно высоты игрового окна
    // Возвращает -1, если круг находится выше границы окна, 1 - если ниже, и 0 - если находится на границе
    public int orientCircle (Circle circle,int moveSpeed, Pane gameWindow,int orientation){
        double circleTop = circle.getLayoutY() - circle.getRadius();
        double circleBottom = circle.getLayoutY() + circle.getRadius();

        // Проверяем, выходит ли верхняя или нижняя границы круга за границы игрового окна
        if (circleBottom + moveSpeed > gameWindow.getHeight() || circleTop - moveSpeed < 0) {
            // Если да, меняем ориентацию
            circle.setLayoutY(circle.getLayoutY() + (orientation * -1) * moveSpeed);
            return orientation * -1;
        } else {
            // Иначе, оставляем ориентацию без изменений
            circle.setLayoutY(circle.getLayoutY() + orientation * moveSpeed);
            return orientation;
        }
    }
}
