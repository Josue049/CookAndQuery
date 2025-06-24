package com.example.cookandquery;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Ventanas {

    private GraphicsContext graficos;
    private Image fondo;
    private int ancho, alto;
    private Canvas lienzo;
    private Scene escena;


    public Ventanas(Stage ventana, int ancho, int alto, String titulo) {
        this.ancho = ancho;
        this.alto = alto;
        inicializarVentana(ventana, titulo, null);
    }

    public Ventanas(Stage ventana, int ancho, int alto, String titulo, String rutaFondo) {
        this.ancho = ancho;
        this.alto = alto;
        this.fondo = new Image("file:" + rutaFondo);
        inicializarVentana(ventana, titulo, fondo);
    }

    public void dibujarFondo() {
        if (fondo != null) {
            graficos.drawImage(fondo, 0, 0, ancho, alto);
        }
    }

    public void dibujarVentanaConFondos(){
        if (fondo != null) {
            double mitadAlto = alto / 1.7;
            graficos.drawImage(fondo, 0, 0, ancho, mitadAlto);
        }
    }

    private void inicializarVentana(Stage ventana, String titulo, Image fondoInicial) {
        Group root = new Group();
        this.escena = new Scene(root, ancho, alto);

        lienzo = new Canvas(ancho, alto);
        root.getChildren().add(lienzo);

        this.graficos = lienzo.getGraphicsContext2D();

        dibujarFondo();

        ventana.setScene(escena);
        ventana.setTitle(titulo);
        ventana.show();
    }

    public Scene getEscena() {
        return escena;
    }

    public Canvas getLienzo() {
        return lienzo;
    }


    public GraphicsContext getGraficos() {
        return graficos;
    }

    public Image getFondo() {
        return fondo;
    }
}
