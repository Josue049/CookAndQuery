package com.example.cookandquery;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JuegoTablas extends Application {
    private Image[] animaciones = new Image[8];
    final int anchoVentana = 800;
    final int altoVentana = 500;
    final int aumento = 35;
    String path = getClass().getResource("/CriticalTheme.wav").toExternalForm();
    Media media = new Media(path);
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    private boolean Start = true;

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Tabla Nivel 1");
        Personaje MantelFondo = new Personaje(0, 0, anchoVentana + aumento, altoVentana + aumento,
                "src/main/resources/FondosSeleccion", 8, "fondo", 150);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        Personaje tablaNivel1 = new Personaje(208, 51, "src/main/resources/Tabla.png", 396, 382);
        

        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // Cargar animaciones
        Image[] MantelMov = new Image[8];
        
        cargarAnimacion("src/main/resources/FondosSeleccion", "fondo", MantelMov, anchoVentana + aumento,
                altoVentana + aumento);


        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                MantelFondo.refrescarAnimacion(now, gc, MantelMov);
                tablaNivel1.dibujar(gc);

                if ((teclasActivas.contains(KeyCode.ENTER) )) {
                    if (Start) {
                        try {
                            Start=false;
                            mediaPlayer.stop();
                            new Juego().start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }

    public void cargarAnimacion(String carpetaSprites, String nombreArchivos, Image[] listaAnimacion, int ancho,
            int alto) {
        for (int i = 0; i < animaciones.length; i++) {
            String ruta = String.format("file:%s/%s%d.png", carpetaSprites, nombreArchivos, i + 1);
            listaAnimacion[i] = new Image(ruta, ancho, alto, true, true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
