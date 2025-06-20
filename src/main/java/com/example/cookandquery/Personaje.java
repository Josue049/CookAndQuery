package com.example.cookandquery;
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

    // Constructor para imagen estática
    public Personaje(double x, double y, String rutaImagen) {
        this.x = x;
        this.y = y;
        this.sprite = new Image("file:" + rutaImagen, 70, 70, true, true);
        this.usarAnimacion = false;
    }

    // Constructor para animación
    public Personaje(double x, double y, String carpetaSprites, int cantidadFrames, String nombrearchivos, int velocidadAnimacion) {
        this.x = x;
        this.y = y;
        this.animaciones = new Image[cantidadFrames];
        this.velocidadAnimacion = velocidadAnimacion * 1_000_000; // nanosegundos
        this.usarAnimacion = true;
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

    // Getters y Setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
// Nota de Josué para Boris: Asegúrate de que las rutas de las imágenes sean
// correctas y que los archivos existan en el directorio especificado, no hay
// manejo de errores para eso.
// Tambien puedes ajustar la velocidad de animación y el tamaño del sprite, pero
// yo lo estoy dejando fijo por el momento xd.