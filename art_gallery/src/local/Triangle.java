/**
 * Distributed Art-Gallery
 *
 *  @author Frederico Martins Biber Sampaio
 *
 * The MIT License (MIT)
 * 
 * Copyright (C) 2013  Frederico Martins Biber Sampaio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
*/

package local;

import java.util.Collection;

public class Triangle {
    
    Vertex v0, v1, v2;
    Edge edges[];

    public Triangle() {
        set(null, null, null);
    }

    public Triangle(Triangle t) {
        set(t.v0, t.v1, t.v2);
    }
    
    public Triangle(Vertex v0, Vertex v1, Vertex v2) {
        set(v0, v1, v2);
    }

    public void set(Triangle t) {
        this.v0 = t.v0;
        this.v1 = t.v1;
        this.v2 = t.v2;
        this.edges = null;
    }
    
    public void set(Vertex v0, Vertex v1, Vertex v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.edges = null;
    }
    
    public Edge[] getEdges() {
        if (edges == null) {
            edges = new Edge[3];
            edges[0] = new Edge(v0, v1);
            edges[1] = new Edge(v1, v2);
            edges[2] = new Edge(v2, v0);
        }
        return edges;
    }

    /* cross product of 3D with z = 0
     "Note that the signed area will be positive if the vertices V0V1V2 
     are oriented counterclockwise around the triangle, 
     and will be negative if the triangle is oriented clockwise; and so, 
     this area computation can be used to test for a triangle's orientation. 
     This signed area can also be used to test whether the point V2 
     is to the left (positive) or the right (negative) of the directed 
     line segment V0V1 . So this value is a very useful primitive, 
     and it's great to have such an efficient formula for it."
     http://geomalgorithms.com/a01-_area.html
     */
    static private double cp(Vertex v0, Vertex v1, Vertex v2) {
        return (((v1.x-v0.x)*(v2.y-v0.y))-((v2.x-v0.x)*(v1.y-v0.y)));
    }

    public double area() {
        return Math.abs(cp(v0, v1, v2) / 2.0d);
    }    
    
    public double signed_area() {
        return cp(v0, v1, v2) / 2.0d;
    }    
    
    public boolean inClockwise() {
        return cp(v0, v1, v2) < 0;
    }
    
    static public boolean inClockwise(Edge e, Vertex v) {
        return cp(e.v0, e.v1, v) < 0;
    }
    
    public boolean hasVertexInside(Collection<Vertex> vertexes) {
        for(Vertex v: vertexes)
            if (!hasVertex(v) && inCollision(v)) 
                return true;
        return false;
    }
    
    public boolean inCollision(Vertex p) {
        boolean d0 = cp(p, v0, v1) <= 0;//Polygon.EPSILON;
        boolean d1 = cp(p, v1, v2) <= 0;//Polygon.EPSILON;
        boolean d2 = cp(p, v2, v0) <= 0;//Polygon.EPSILON;
        return ((d0 == d1) && (d1 == d2));
    }
    
    public boolean hasVertex(Vertex p) {
        return (p.equals(v0) || p.equals(v1) || p.equals(v2));
    }

    public int nextColor(boolean colors[]) {
        int color = 0;
        if (colors[1]) {
            if (colors[2]) {
                color = 3;
            } else {
                color = 2;
            }
        } else {
            color = 1;
        }
        colors[color] = true;
        return color;
    }
    
    static final int free_color_by_sum[] = { 0, 0, 0, 3, 2, 1, 0 }; 
    public int getFreeColor() {
        // here, the colorization also can be checked, but it is not.
        return free_color_by_sum[v0.color + v1.color + v2.color];
    }
    
    public void coloringFirstColorlessVertex() {
        if (v0.color == 0) {
            v0.color = getFreeColor();
        } else if (v1.color == 0) {
            v1.color = getFreeColor();
        } else if (v2.color == 0) {
            v2.color = getFreeColor();
        }
    }

    public boolean isColored() {
        return (v0.color + v1.color + v2.color) == 6;
    }

    public void coloring() {
        // precondition: all booleans in array must be initialized with false
        boolean colors[] = new boolean[4];
        colors[v0.color] = true;
        colors[v1.color] = true;
        colors[v2.color] = true;
        if (v0.color == 0) v0.color = nextColor(colors);
        if (v1.color == 0) v1.color = nextColor(colors);
        if (v2.color == 0) v2.color = nextColor(colors);
    }

    @Override
    public String toString() {
        return "Triangle => [" + v0 + "-" + v1 + "-" + v2 + "]";
    
    }
    
    public static void main(String[] args) {
        Vertex v0 = new Vertex(1, 1);
        Vertex v1 = new Vertex(3, 1);
        Vertex v2 = new Vertex(1, 3);
        System.out.println("t1 cross product = " + cp(v0, v1, v2));
        System.out.println("t2 cross product = " + cp(v1, v0, v2));
        Triangle t1 = new Triangle(v0, v1, v2);
        Triangle t2 = new Triangle(v1, v0, v2);
        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        if (!t1.inClockwise()) System.out.println("t1 counter-clockwise direction ok");
        else System.out.println("t1 counter-clockwise direction fail");
        if (t2.inClockwise()) System.out.println("t2 clockwise direction ok");
        else System.out.println("t2 clockwise direction fail");
        System.out.println("t1 area = " + t1.area());
        System.out.println("t2 area = " + t2.area());
    }
}
