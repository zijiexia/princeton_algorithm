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
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        checkNull(points);
        Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            aux[i] = points[i];
        }
        Arrays.sort(aux);
        checkDuplicate(aux);
        for (int i = 0; i < aux.length - 3; i++) {
            for (int j = i + 1; j < aux.length - 2; j++) {
                for (int k = j + 1; k < aux.length - 1; k++) {
                    for (int x = k + 1; x < aux.length; x++) {
                        double slope1 = aux[i].slopeTo(aux[j]);
                        double slope2 = aux[i].slopeTo(aux[k]);
                        double slope3 = aux[i].slopeTo(aux[x]);
                        if (Double.compare(slope1, slope2) == 0
                                && Double.compare(slope2, slope3) == 0) {
                            segments.add(new LineSegment(aux[i], aux[x]));
                        }
                    }
                }
            }
        }
    }

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
        return segments.size();
    }      // the number of line segments

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("input48.txt");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
