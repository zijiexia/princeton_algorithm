/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class KdTree {
    private TreeNode root;


    private class TreeNode {
        private double key;
        private Point2D point;
        private TreeNode left, right;
        private int count;
        private final int level;

        public TreeNode(Point2D point, int level) {
            this.point = point;
            this.level = level;
            this.count = 1;
            if (level % 2 == 0) this.key = point.x();
            else this.key = point.y();
        }
    }

    public KdTree() {
        root = null;
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return this.root == null;
    }                     // is the set empty?

    public int size() {
        return size(root);
    }                        // number of points in the set

    private int size(TreeNode p) {
        if (p == null) return 0;
        return p.count;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.root = insert(this.root, p, 0);
    }              // add the point to the set (if it is not already in the set)

    private TreeNode insert(TreeNode node, Point2D point, int level) {
        if (node == null) {
            return new TreeNode(point, level);
        }
        if (node.level % 2 == 0) {
            if (point.x() < node.key) node.left = insert(node.left, point, level + 1);
            else if (point.equals(node.point)) {
                node.point = point;
            }
            else node.right = insert(node.right, point, level + 1);
        }
        else {
            if (point.y() < node.key) node.left = insert(node.left, point, level + 1);
            else if (point.equals(node.point)) node.point = point;
            else node.right = insert(node.right, point, level + 1);
        }
        node.count = 1 + size(node.left) + size(node.right);
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return search(root, p);
    }            // does the set contain point p?

    private boolean search(TreeNode node, Point2D point) {
        if (node == null) return false;
        if (point.equals(node.point)) {
            return true;
        }
        if (node.level % 2 == 0) {
            if (point.x() < node.key) return search(node.left, point);
            else return search(node.right, point);
        }
        else {
            if (point.y() < node.key) return search(node.left, point);
            else return search(node.right, point);
        }
    }

    public void draw() {
        draw(root, 0.0, 1.0, 0.0, 1.0);
    }                         // draw all points to standard draw

    private void draw(TreeNode node, double horMin, double horMax, double verMin, double verMax) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            node.point.draw();
            if (node.level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                node.point.drawTo(new Point2D(node.point.x(), verMax));
                node.point.drawTo(new Point2D(node.point.x(), verMin));
                draw(node.left, horMin, node.point.x(), verMin, verMax);
                draw(node.right, node.point.x(), horMax, verMin, verMax);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                node.point.drawTo(new Point2D(horMin, node.point.y()));
                node.point.drawTo(new Point2D(horMax, node.point.y()));
                draw(node.left, horMin, horMax, verMin, node.point.y());
                draw(node.right, horMin, horMax, node.point.y(), verMax);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) return null;
        LinkedList<Point2D> result = findPoints(rect, 0.0, 1.0, 0.0, 1.0, root, new LinkedList<>());
        return result;
    }            // all points that are inside the rectangle (or on the boundary)

    private LinkedList<Point2D> findPoints(RectHV rect, double horMin, double horMax, double verMin,
                                           double verMax, TreeNode node,
                                           LinkedList<Point2D> result) {
        if (node == null) return new LinkedList<Point2D>();
        if (rect.contains(node.point)) result.add(node.point);
        if (node.level % 2 == 0) {
            RectHV leftRec = new RectHV(horMin, verMin, node.point.x(), verMax);
            RectHV rightRec = new RectHV(node.point.x(), verMin, horMax, verMax);
            if (rect.intersects(leftRec)) {
                result.addAll(findPoints(rect, horMin, node.point.x(), verMin, verMax, node.left,
                                         new LinkedList<Point2D>()));
            }
            if (rect.intersects(rightRec)) {
                result.addAll(findPoints(rect, node.point.x(), horMax, verMin, verMax, node.right,
                                         new LinkedList<Point2D>()));
            }
        }
        else {
            RectHV topRec = new RectHV(horMin, node.point.y(), horMax, verMax);
            RectHV botRec = new RectHV(horMin, verMin, horMax, node.point.y());
            if (rect.intersects(topRec)) {
                result.addAll(findPoints(rect, horMin, horMax, node.point.y(), verMax, node.right,
                                         new LinkedList<Point2D>()));
            }
            if (rect.intersects(botRec)) {
                result.addAll(findPoints(rect, horMin, horMax, verMin, node.point.y(), node.left,
                                         new LinkedList<Point2D>()));
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        TreeNode nearest = findNearest(root, 0.0, 1.0, 0.0, 1.0, root, p);
        return isEmpty() ? null : nearest.point;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    private TreeNode findNearest(TreeNode node, double horMin, double horMax, double verMin,
                                 double verMax, TreeNode nearest, Point2D query) {
        if (node == null) return nearest;
        if (query.distanceSquaredTo(node.point) < query.distanceSquaredTo(nearest.point)) {
            nearest = node;
        }
        if (node.level % 2 == 0) {
            if (query.x() < node.point.x()) {
                nearest = findNearest(node.left, horMin, node.key, verMin, verMax, nearest, query);
                RectHV rightRec = new RectHV(node.point.x(), verMin, horMax, verMax);
                if (query.distanceSquaredTo(nearest.point) > rightRec.distanceSquaredTo(query)) {
                    nearest = findNearest(node.right, node.key, horMax, verMin, verMax, nearest,
                                          query);
                }
            }
            else {
                nearest = findNearest(node.right, node.key, horMax, verMin, verMax, nearest, query);
                RectHV leftRec = new RectHV(horMin, verMin, node.point.x(), verMax);
                if (query.distanceSquaredTo(nearest.point) > leftRec.distanceSquaredTo(query)) {
                    nearest = findNearest(node.left, horMin, node.key, verMin, verMax, nearest,
                                          query);
                }
            }
        }
        else {
            if (query.y() < node.point.y()) {
                nearest = findNearest(node.left, horMin, horMax, verMin, node.key, nearest, query);
                RectHV topRec = new RectHV(horMin, node.point.y(), horMax, verMax);
                if (query.distanceSquaredTo(nearest.point) > topRec.distanceSquaredTo(query)) {
                    nearest = findNearest(node.right, horMin, horMax, node.key, verMax, nearest,
                                          query);
                }
            }
            else {
                nearest = findNearest(node.right, horMin, horMax, node.key, verMax, nearest, query);
                RectHV botRec = new RectHV(horMin, verMin, horMax, node.point.y());
                if (query.distanceSquaredTo(nearest.point) > botRec.distanceSquaredTo(query)) {
                    nearest = findNearest(node.left, horMin, horMax, verMin, node.key, nearest,
                                          query);
                }
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        StdOut.println(kdtree.size());
        StdOut.println(kdtree.isEmpty());
        RectHV rightRec = new RectHV(0.0, 0.0, 0.1, 0.1);
        Point2D root = new Point2D(0.0, 0.75);
        StdOut.println(kdtree.range(rightRec));
        StdOut.println(kdtree.nearest(root));
        kdtree.insert(root);
        StdOut.println(kdtree.contains(root));
        StdOut.println(kdtree.size());
        kdtree.draw();
        Point2D root1 = new Point2D(0.875, 0.25);
        kdtree.insert(root1);
        StdOut.println(kdtree.contains(root1));
        StdOut.println(kdtree.size());
        kdtree.draw();
        Point2D root2 = new Point2D(0.25, 0.25);
        kdtree.insert(root2);
        StdOut.println(kdtree.contains(root2));
        StdOut.println(kdtree.contains(root1));
        StdOut.println(kdtree.size());
        kdtree.draw();
    }
}
