package com.example.cookandquery;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;

import java.util.HashSet;
import java.util.Set;

public class Juego extends Application {

    int tam = 70;
    private GraphicsContext graficos;
    private Scene escena;

    private Image fondo;
    private Image[] animacionAbajo;
    private Image[] animacionArriba;
    private Image[] animacionIzquierda;
    private Image[] animacionDerecha;
    private Polygon areaPermitida;

    private int frameActual = 0;
    private long ultimoCambio = 0;
    private long velocidadAnimacion = 100_000_000; // 100ms

    private enum Direccion { ABAJO, ARRIBA, IZQUIERDA, DERECHA }
    private Direccion direccionActual = Direccion.ABAJO;

    private double personajeX = 500;
    private double personajeY = 300;
    private double velocidad = 3;

    private Set<KeyCode> teclasPresionadas = new HashSet<>();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage ventana) {
        Group root = new Group();
        escena = new Scene(root);
        Canvas lienzo = new Canvas(800, 750);
        root.getChildren().add(lienzo);
        graficos = lienzo.getGraphicsContext2D();

        cargarRecursos();
        gestionarEventos();
        bucleJuego();

        ventana.setScene(escena);
        ventana.setTitle("Query and Cook");
        ventana.show();
    }

    private void cargarRecursos() {
        fondo = new Image("file:src/main/resources/EscenarioNivel1.png");

        animacionAbajo = new Image[] {
                new Image("file:src/main/resources/SpriteChefFinal/down/1.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/2.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/3.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/4.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/5.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/6.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/down/7.png", tam, tam, true, true),
        };

        animacionArriba = new Image[] {
                new Image("file:src/main/resources/SpriteChefFinal/up/up1.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up2.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up3.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up4.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up5.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up6.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/up/up7.png", tam, tam, true, true),
        };

        animacionIzquierda = new Image[] {
                new Image("file:src/main/resources/SpriteChefFinal/left/left1.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left2.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left3.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left4.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left5.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left6.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/left/left7.png", tam, tam, true, true),
        };

        animacionDerecha = new Image[] {
                new Image("file:src/main/resources/SpriteChefFinal/right/right1.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right2.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right3.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right4.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right5.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right6.png", tam, tam, true, true),
                new Image("file:src/main/resources/SpriteChefFinal/right/right7.png", tam, tam, true, true),
        };
    }

    private void gestionarEventos() {
        escena.setOnKeyPressed((KeyEvent evento) -> teclasPresionadas.add(evento.getCode()));
        escena.setOnKeyReleased((KeyEvent evento) -> teclasPresionadas.remove(evento.getCode()));
    }

    private void bucleJuego() {
        // Definir las 4 esquinas del área permitida (puedes ajustar los valores)
        areaPermitida = new Polygon(
                226.0, 112.0,  // esquina superior izquierda
                600.0, 112.0,  // esquina superior derecha
                630.0, 355.0,  // esquina inferior derecha
                190.0, 355.0   // esquina inferior izquierda
        );

        new AnimationTimer() {
            @Override
            public void handle(long ahora) {
                actualizar(ahora);
                pintar();
            }
        }.start();
    }

    private void actualizar(long ahora) {
        double nuevaX = personajeX;
        double nuevaY = personajeY;

        if (teclasPresionadas.contains(KeyCode.W)) {
            nuevaY -= velocidad;
            direccionActual = Direccion.ARRIBA;
        }
        if (teclasPresionadas.contains(KeyCode.S)) {
            nuevaY += velocidad;
            direccionActual = Direccion.ABAJO;
        }
        if (teclasPresionadas.contains(KeyCode.A)) {
            nuevaX -= velocidad;
            direccionActual = Direccion.IZQUIERDA;
        }
        if (teclasPresionadas.contains(KeyCode.D)) {
            nuevaX += velocidad;
            direccionActual = Direccion.DERECHA;
        }

        // Validar si el centro del personaje estaría dentro del área permitida
        double centroX = nuevaX + tam / 2.0;
        double centroY = nuevaY + tam / 2.0;

        if (areaPermitida.contains(centroX, centroY)) {
            personajeX = nuevaX;
            personajeY = nuevaY;
        }

        // Controlar cambio de frame para animación
        if (ahora - ultimoCambio > velocidadAnimacion) {
            frameActual = (frameActual + 1) % getAnimacionActual().length;
            ultimoCambio = ahora;
        }
    }


    private Image[] getAnimacionActual() {
        return switch (direccionActual) {
            case ARRIBA -> animacionArriba;
            case ABAJO -> animacionAbajo;
            case IZQUIERDA -> animacionIzquierda;
            case DERECHA -> animacionDerecha;
        };
    }

    private void pintar() {
        graficos.clearRect(0, 0, 800, 600);

        graficos.drawImage(fondo, 0, 0, 800, 450);
        graficos.drawImage(getAnimacionActual()[frameActual], personajeX, personajeY);

        /*
        graficos.setStroke(javafx.scene.paint.Color.RED);
        graficos.setLineWidth(2);
        graficos.strokePolygon(
                new double[]{196, 600, 632, 160},
                new double[]{112, 112, 390, 390},
                4
        );*/

    }
}
