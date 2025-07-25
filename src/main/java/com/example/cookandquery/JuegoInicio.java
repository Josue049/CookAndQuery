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

public class JuegoInicio extends Application {
    private Image[] animaciones = new Image[8];
    final int anchoVentana = 800;
    final int altoVentana = 450;
    String path = getClass().getResource("/SwitchWithMeTheme.wav").toExternalForm();
    String pathNext = getClass().getResource("/next.mp3").toExternalForm();
    Media media = new Media(path);
    Media mediaEffect = new Media(pathNext);
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    MediaPlayer EffectNext = new MediaPlayer(mediaEffect);
    final int aumento = 300;

    private boolean Start = true;

    

    @Override
    public void start(Stage ventana) {

        // Crear ventana con fondo
        Ventanas escenaJuego = new Ventanas(ventana, anchoVentana, altoVentana, "Cook And Query: Inicio");
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        Personaje fondoPlay = new Personaje(0, 0, "src/main/resources/frame1.png", altoVentana, anchoVentana);
        Personaje fondoExit = new Personaje(0, 0, "src/main/resources/frame2.png", altoVentana, anchoVentana);

        // Obtener el contexto gráfico
        GraphicsContext gc = escenaJuego.getGraficos();
        Set<KeyCode> teclasActivas = new HashSet<>();

        escenaJuego.getEscena().setOnKeyPressed(e -> teclasActivas.add(e.getCode()));
        escenaJuego.getEscena().setOnKeyReleased(e -> teclasActivas.remove(e.getCode()));

        // LOOP DEL JUEGO
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, anchoVentana, altoVentana);

                if (Start) {
                    fondoPlay.dibujar(gc);
                } else {
                    fondoExit.dibujar(gc);
                }

                // LÓGICA FLECHA ARRIBA
                if ((teclasActivas.contains(KeyCode.UP) || teclasActivas.contains(KeyCode.W))) {
                    if (!Start) {
                        Start = true;
                        MediaPlayer efecto = new MediaPlayer(mediaEffect);
                        efecto.play();
                    }
                }

                // LÓGICA FLECHA ABAJO
                if ((teclasActivas.contains(KeyCode.DOWN) || teclasActivas.contains(KeyCode.S))) {
                    if (Start) {
                        Start = false;
                        MediaPlayer efecto = new MediaPlayer(mediaEffect);
                        efecto.play();
                    }
                }


                if ((teclasActivas.contains(KeyCode.SPACE) )) {
                    if (Start) {
                        try {
                            Start=false;
                            mediaPlayer.stop();
                            new tutorial1().start(new Stage());
                            ventana.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // Salir del juego
                        ventana.close(); // o Platform.exit();
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
