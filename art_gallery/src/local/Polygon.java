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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Polygon implements Iterable<Vertex> {
    
    private boolean closed, triangulated;
    // Counter-Clock Wise order!
    private ArrayList<Vertex> pointsInOrder;
    private ArrayList<Edge> diagonals;
    private ArrayList<Edge> sides;
    
    private int minColor = 0;

    public Polygon(Vertex first) {
        closed = false;
        triangulated = false;
        pointsInOrder = new ArrayList<Vertex>();
        pointsInOrder.add(first);
        diagonals = new ArrayList<Edge>();
        sides = new ArrayList<Edge>();
    }
    
    public int size() {
        return pointsInOrder.size();
    }
    
    public Vertex get(int i) {
        return pointsInOrder.get(i);
    }
    
    public boolean close() {
            if (pointsInOrder.size() < 3)
                return false;
            Vertex last = pointsInOrder.get(pointsInOrder.size() - 1);
            last.setNext(pointsInOrder.get(0));
            closed = true;
            return true;
    }
    
    public boolean addVertexInOrder(Vertex next) {
        if (closed) return false;
        if (next.equals(pointsInOrder.get(0))) {
            return close();
        }
        int last = pointsInOrder.size() - 1;
        if (last >= 0) {
            pointsInOrder.get(last).setNext(next);
        }
        pointsInOrder.add(next);
        return true;
    }

    public boolean isClosed() {
        return closed;
    }
    
    public boolean isTriangulated() {
        return triangulated;
    }
    
    public double area()
    {
        int n = pointsInOrder.size();
        double area = 0.0d;
        for(int p = n - 1, q = 0; q < n; p = q++) {
            area += pointsInOrder.get(p).getX() * pointsInOrder.get(q).getY() - 
                    pointsInOrder.get(q).getX() * pointsInOrder.get(p).getY();
        }
        return area * 0.5d;
    }    
    
    private boolean testIntersection(Vertex v00, Vertex v01, Vertex v10, Vertex v11) {
    	Edge e0 = new Edge(v00, v01);
    	Edge e1 = new Edge(v10, v11);
    	return e0.intersect(e1);    	
    }
    
    private Edge makeDiagonal(Vertex v, int i) {
    	Edge diag = new Edge(v, pointsInOrder.get(i));
    	for (Edge side: sides)
    		if (diag.intersect(side)) 
    			return null;
        //if (Triangle.inCCW(diag, pointsInOrder.get(i+1)))
        	return diag;
        //return null;
    }
    
    public void test()
    {
        int n = pointsInOrder.size();
        sides.clear();
        for(int p = n - 1, q = 0; q < n; p = q++) {
            sides.add(new Edge(pointsInOrder.get(p), pointsInOrder.get(q)));
        }
        n--;
        diagonals.clear();
        Edge diag;
        Vertex v = pointsInOrder.get(0);
        for (int i = 2; i < n; i++) {
        	if ((diag = makeDiagonal(v, i)) != null)  
        		diagonals.add(diag);
        }
    }    
    
    // Ref: www.flipcode.com/archives/Efficient_Polygon_Triangulation.shtml
    public boolean triangulate(int init) {
        //-------------------------------------------------------------------
        // Preconditions
        //-------------------------------------------------------------------
        // is a valid polygon?
        if (pointsInOrder.size() < 3) 
            return false;
        // already closed and triangulated?
        if (closed && triangulated)
            return true;
        // verify initial vertex,
        if ((init < 0) || (init >= pointsInOrder.size()))
            return false;
        //-------------------------------------------------------------------
        // Preparation (initialization)
        //-------------------------------------------------------------------
        clearColors();
        diagonals.clear();
        // new polygon that will be reduced until remain only a triangle.
        @SuppressWarnings("unchecked")
        ArrayList<Vertex> remaining = (ArrayList<Vertex>)pointsInOrder.clone();
        // ensure the Counter-ClockWise order
        if (this.area() > 0) 
            Collections.reverse(remaining);
        // sequential vertices (candidates triangles to be removed) 
        int iv0, iv1, iv2;
        Vertex v0, v1, v2;
        // size control of remaining polygon 
        int size = remaining.size();
        // control if removing triangle (ear) loop become infinite
        int loop = 0;
        Triangle t = new Triangle();
        iv0 = init;
        //-------------------------------------------------------------------
        // Main loop (cut ears...)
        //-------------------------------------------------------------------
        while (size > 3) {
            // was made a full cycle without to find a triangle?
            if (loop++ >= size) return false;
            // take three consecutive vertexes (v0, v1, v2)
            if (iv0 >= size) iv0 = 0;
            iv1 = (iv0 + 1) % size;
            iv2 = (iv1 + 1) % size;
            // set the triangle of these vertexes...
            v0 = remaining.get(iv0);
            v1 = remaining.get(iv1);
            v2 = remaining.get(iv2);
            t.set(v0, v1, v2);
            // verify if the triangle makes a valid diagonal
            if (t.inCCW() && !t.hasVertexInside(remaining)) {
                loop = 0;  // new cycle
                v2.setDiagonal(v0);
                diagonals.add(new Edge(v0, v2));
                t.coloring();
                remaining.remove(v1);
                size--;
                if (iv0 >= size) iv0 = (size - 1);
            } else {
                iv0 = (iv0 + 1) % size;
            }
        }
        //-------------------------------------------------------------------
        // Termination (finalization)
        //-------------------------------------------------------------------
        v0 = remaining.get(0);
        v1 = remaining.get(1);
        v2 = remaining.get(2);
        t.set(v0, v1, v2);
        t.coloring();
        countColors();
        triangulated = true;
        test();
        return triangulated;
    }
    
    private void clearColors() {
        // reset current control
        minColor = 0;
        // clear vertexes colors:
        for (Vertex v: pointsInOrder) 
            v.color = 0;
    }
    
    private void countColors() {
        // summing the colors:
        int colorCount[] = new int[4];
        colorCount[0] = 0;
        colorCount[1] = 0;
        colorCount[2] = 0;
        colorCount[3] = 0;
        for (Vertex v: pointsInOrder) 
            colorCount[v.color]++;
        // Coloring problem?
        if (colorCount[0] > 0)
            return;
        // find min(colorCount):
        int minCount = Integer.MAX_VALUE;
        for (int i = 1; i < 4; i++)
            if (minCount > colorCount[i]) {
                minColor = i;
                minCount = colorCount[i];
            } else if ((minCount == colorCount[i]) && (minColor > i)) {
                minColor = i;
            }
    }
    
    public int getMinColor() {
        return minColor;
    }
    
    public int getColor(int i) {
        if ((i < 0) || (i >= pointsInOrder.size()))
            return 0;
        return pointsInOrder.get(i).color;
    }
    
    public ArrayList<Edge> getDiagonals() {
        return diagonals;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return pointsInOrder.iterator();
    }

    static public Vertex[] ex1() {
        int i = 0;
        Vertex[] v = new Vertex[6];
        v[i++] = new Vertex(2,2);
        v[i++] = new Vertex(2,4);
        v[i++] = new Vertex(3,1);
        v[i++] = new Vertex(4,4);
        v[i++] = new Vertex(4,2);
        v[i++] = new Vertex(3,6);
        return v;
    }
    
    static public Vertex[] ex2() {
        int i = 0;
        Vertex[] v = new Vertex[8];
        v[i++] = new Vertex(1,1);
        v[i++] = new Vertex(1,4);
        v[i++] = new Vertex(4,4);
        v[i++] = new Vertex(4,3);
        v[i++] = new Vertex(2,3);
        v[i++] = new Vertex(2,2);
        v[i++] = new Vertex(4,2);
        v[i++] = new Vertex(4,1);
        return v;
    }
    
    /*
    Triangle 1 => (2,6) (0,6) (0,0)
    Triangle 2 => (0,0) (3,0) (4,1)
    Triangle 3 => (6,1) (8,0) (12,0)
    Triangle 4 => (12,0) (13,2) (8,2)
    Triangle 5 => (8,4) (11,4) (11,6)
    Triangle 6 => (11,6) (6,6) (4,3)
    Triangle 7 => (4,3) (2,6) (0,0)
    Triangle 8 => (6,1) (12,0) (8,2)
    Triangle 9 => (8,4) (11,6) (4,3)
    Triangle 10 => (4,3) (0,0) (4,1)
    Triangle 11 => (4,1) (6,1) (8,2)
    Triangle 12 => (8,2) (8,4) (4,3)
    Triangle 13 => (4,3) (4,1) (8,2)
    */
    static public Vertex[] ex3() {
        int i = 0;
        Vertex[] v = new Vertex[15];
        v[i++] = new Vertex(0,6);
        v[i++] = new Vertex(0,0);
        v[i++] = new Vertex(3,0);
        v[i++] = new Vertex(4,1);
        v[i++] = new Vertex(6,1);
        v[i++] = new Vertex(8,0);
        v[i++] = new Vertex(12,0);
        v[i++] = new Vertex(13,2);
        v[i++] = new Vertex(8,2);
        v[i++] = new Vertex(8,4);
        v[i++] = new Vertex(11,4);
        v[i++] = new Vertex(11,6);
        v[i++] = new Vertex(6,6);
        v[i++] = new Vertex(4,3);
        v[i++] = new Vertex(2,6);
        return v;
    }
    
    public static void main(String[] args) {
        Vertex v[] = ex3();
        int n = v.length;
        int i;
        
        Polygon p = new Polygon(v[0]);
        for (i = 1; i < n; i++) {
            p.addVertexInOrder(v[i]);
        }
        p.addVertexInOrder(v[0]);
        if (p.triangulate(0)) {
            for (i = 0; i < n; i++) {
                System.out.println(v[i]);
            }
            for (Edge e: p.getDiagonals()) {
                System.out.println(e);
            }
            System.out.println("Min(colors) = " + p.getMinColor());
        } else {
            System.out.println("Erro!");
        }
    }
}
