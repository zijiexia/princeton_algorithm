/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> result = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            aux[i] = points[i];
        }
        Arrays.sort(aux);
        checkDuplicate(aux);
        for (int i = 0; i < aux.length; i++) {
            Point p = aux[i];
            Point[] slopeOrder = aux.clone();
            Arrays.sort(slopeOrder, p.slopeOrder());
            int n = 1;
            while (n < slopeOrder.length) {
                LinkedList<Point> candidates = new LinkedList<>();
                final double SLOPE_OF_P = p.slopeTo(slopeOrder[n]);
                do {
                    candidates.add(slopeOrder[n++]);
                } while (n < slopeOrder.length && p.slopeTo(slopeOrder[n]) == SLOPE_OF_P);

                if (candidates.size() >= 3 && p.compareTo(candidates.peek()) < 0) {
                    Point max = candidates.removeLast();
                    result.add(new LineSegment(p, max));
                }
            }
        }
    }    // finds all line segments containing 4 or more points

    private void checkNull(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The input argument is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("The input array contains null point");
            }
        }
    }

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Input points array contains duplicated points");
        }
    }

    public int numberOfSegments() {
        return result.size();
    }       // the number of line segments

    public LineSegment[] segments() {
        return result.toArray(new LineSegment[0]);
    }               // the line segments

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("input6.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
