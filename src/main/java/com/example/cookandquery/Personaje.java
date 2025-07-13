package com.example.cookandquery;
import com.almasb.fxgl.procedural.HeightMapGenerator.HeightData;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Personaje {
    private double x, y;

    private Image sprite; // Para imagen estática
    private Image[] animaciones; // Para animaciones
    private int frameActual = 0;
    private long ultimoCambio = 0;
    private long velocidadAnimacion; // 150 ms por frame
    private boolean usarAnimacion = false;
    private GraphicsContext gc;
    private int alto;
    private int ancho;

    // Constructor para imagen estática
    public Personaje(double x, double y, String rutaImagen, int alto, int ancho) {
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
        this.sprite = new Image("file:" + rutaImagen, ancho, alto, true, true);
        this.usarAnimacion = false;
    }

    // Constructor para animación
    public Personaje(double x, double y, int tamx, int tamy, String carpetaSprites, int cantidadFrames, String nombrearchivos, int velocidadAnimacion) {
        this.x = x;
        this.y = y;
        this.animaciones = new Image[cantidadFrames];
        this.velocidadAnimacion = velocidadAnimacion * 1_000_000; // nanosegundos
        this.usarAnimacion = true;
        alto = tamy;
        ancho = tamx;
    }

    // Método para actualizar el frame de animación
    public void refrescarAnimacion(long ahora, GraphicsContext gc, Image[] animaciones) {
        this.animaciones = animaciones;
        if (usarAnimacion && ahora - ultimoCambio > velocidadAnimacion) {
            frameActual = (frameActual + 1) % animaciones.length;
            ultimoCambio = ahora;
        }
        dibujar(gc, animaciones);
    }

    // Método para dibujar (estático o animado)
    public void dibujar(GraphicsContext gc) {
        if (usarAnimacion) {
            gc.drawImage(animaciones[frameActual], x, y);
        } else {
            gc.drawImage(sprite, x, y);
        }
    }

    public void dibujar(GraphicsContext gc, Image[] animaciones) {
        if (usarAnimacion) {
            gc.drawImage(animaciones[frameActual], x, y);
        } else {
            gc.drawImage(sprite, x, y);
        }
    }

    // Opcional: movimiento simple
    public void mover(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void posicionarx(double x) {
        this.x = x;
    }

    // Getters y Setters
    public double getX() {
        return x;
    }


    public void setX(int x) {
        this.x=x;
    }

    public double getY() {
        return y;
    }

    public void setSprite(String sprite, int alto, int ancho, int x, int y) {
        this.sprite = new Image("file:" + sprite, alto, ancho, true, true);
        this.x = x;
        this.y = y;
        this.usarAnimacion = false; // Cambia a imagen estática
    }

    public Image getSprite() {
        return sprite;
    }

    public void cambioSprite(Image sprite) {
        this.sprite = sprite;
    }
}