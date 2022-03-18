/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point : set) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Point2D> result = new LinkedList<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (set.isEmpty()) {
            return null;
        }
        Point2D nearestPoint = new Point2D(0.0, 0.0);
        double nearestDist = 2.0;
        for (Point2D point : set) {
            double tmpDist = p.distanceSquaredTo(point);
            if (tmpDist < nearestDist) {
                nearestDist = tmpDist;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    //
    // public static void main(String[] args) {
    //
    // }
}
