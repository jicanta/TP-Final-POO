package backend.model;

import java.util.Objects;

public class Point {
    /* TODO: para mi las coordenadas x e y tienen que ser final, en las guias siempre decian eso como q no es que le cambias
    las coordenadas a un punto sino que creas un nuevo punto. Encima para girar, voltear, escalar, siempre hacemos eso de
    crear un nuevo punto y reemplazar el actual con este nuevo. Ojo que si cambiamos esto, hay que cambiar algo en el
    archivo PaintPane (buscar comentario porque de todas maneras creo q esta mal esa parte asi que la cambiariamos igual)
    Ademas creo q deberian ser private (o protected como mucho) pq sino los podes acceder directamente y la idea
    es usar los getters */

    private final double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Point p && Double.compare(p.x, x) == 0 && Double.compare(p.y, y) == 0);
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
}
