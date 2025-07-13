package com.example.cookandquery;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class tutorial2 extends Application {
    private Image[] animaciones = new Image[8];
    final int anchoVentana = 800;
    final int altoVentana = 450;

    private boolean Start = true;
    private long tiempoInicio; // ⬅️ Declaración

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Tutorial");

        Personaje fondoPlay = new Personaje(0, 0, "src/main/resources/tutorial/2.png", altoVentana, anchoVentana);

        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        tiempoInicio = System.currentTimeMillis(); // ⬅️ Guardamos el tiempo de inicio

        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, anchoVentana, altoVentana);
                fondoPlay.dibujar(gc);

                long tiempoActual = System.currentTimeMillis(); // ⬅️ Tiempo actual

                if ((teclasActivas.contains(KeyCode.SPACE)) && (tiempoActual - tiempoInicio > 500)) {
                    if (Start) {
                        try {
                            Start = false;
                            new tutorial3().start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
