package elte.heating;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by dobreffandras on 2017. 05. 22..
 */
@Immutable
public class Point {
    public final int x;
    public final int y;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
