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
import java.util.HashMap;
import java.util.Set;

import distributed.ArtGalleryAlgorithm;

public class BasicTriangulation implements ArtGalleryAlgorithm {
    
    // In case of float point operation:  
    final static double EPSILON = 0.000000001;
    
    private Polygon polygon;
    private boolean triangulated;
    private HashMap<Edge, Diagonal> diagonals;

    // we can keep all triangles, if need in preview
    // but, we need to known at least on triangle to start colorization
    private Triangle hook;
    
    private int minColor = 0;
    int colorCount[] = new int[4];

    @Override
    public void setPolygon(Polygon p) {
        polygon = p;
        triangulated = false;
        diagonals = new HashMap<Edge, Diagonal>();
    }
    
    protected void newDiagonal(Triangle tt) {
        // must to make a copy of informed triangle (see triangulate method)
        Triangle t = new Triangle(tt);
        if (hook == null) hook = t;
        Diagonal d;
        // presupposition: the diagonal always is v2-v0
        // otherwise, the edge must be informed as parameter
        Edge e = new Edge(t.v2, t.v0);
        if ((d = diagonals.get(e)) == null) {
            d = new Diagonal(t);
            diagonals.put(e, d);
            checkSide(t, new Edge(t.v0, t.v1));
            checkSide(t, new Edge(t.v1, t.v2));
        }
        /*
        else {
            // we can check if the triangulation is correct here
        }
        */
    }
    
    private void checkSide(Triangle t, Edge e) {
        Diagonal d;
        if ((d = diagonals.get(e)) != null) { 
            // we can check if the triangulation is correct here
            d.setRelated(t);
        }
    }
    
    protected void newTriangle(Vertex v0, Vertex v1, Vertex v2) {
        Triangle t = new Triangle(v0, v1, v2);
        if (hook == null) hook = t;
        for (Edge e: t.getEdges())
            checkSide(t, e);
    }

    @Override
    public boolean solve(int init) {
        //-------------------------------------------------------------------
        // Preconditions
        //-------------------------------------------------------------------
        // is a valid polygon?
        if (polygon.size() < 3) 
            return false;
        // already closed and triangulated?
        if (polygon.isClosed() && triangulated)
            return true;
        // verify initial vertex,
        if ((init < 0) || (init >= polygon.size()))
            return false;
        //-------------------------------------------------------------------
        // Preparation (initialization)
        //-------------------------------------------------------------------
        // clear last triangulation
        triangulated = false;
        diagonals.clear();
        hook = null;
        // new polygon that will be reduced until remain only a triangle.
        @SuppressWarnings("unchecked")
        ArrayList<Vertex> remaining = (ArrayList<Vertex>)polygon.clone();
        // ensure the Clockwise order
        if (polygon.area() > 0) {
            Collections.reverse(remaining);
        }
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
            iv1 = (iv0 + 1) % size;
            iv2 = (iv1 + 1) % size;
            // set the triangle of these vertexes...
            v0 = remaining.get(iv0);
            v1 = remaining.get(iv1);
            v2 = remaining.get(iv2);
            t.set(v0, v1, v2);
            // verify if the triangle makes a valid diagonal
            if (t.inClockwise() && !t.hasVertexInside(remaining)) {
                loop = 0;  // new cycle
                newDiagonal(t); 
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
        newTriangle(v0, v1, v2);
        coloring();
        if (countColors())
            triangulated = true;
        return triangulated;
    }
    
    
    private void coloring() {
        // reset current control
        minColor = 0;
        // clear vertexes colors:
        for (Vertex v: polygon) 
            v.color = 0;
        // coloring the first triangle
        hook.v0.color = 1;
        hook.v1.color = 2;
        hook.v2.color = 3;
        // coloring others triangles
        for (Edge e: hook.getEdges()) 
            recursive_coloring(hook, e);
    }
    
    private void recursive_coloring(Triangle from, Edge e) {
        Diagonal d = diagonals.get(e);
        if (d == null) return;
        Triangle to = d.getOpposite(from); 
        if (to == null) return;
        if (to.isColored()) return;
        to.coloringFirstColorlessVertex();
        for (Edge next: to.getEdges()) {
            if (!next.equals(e))
                recursive_coloring(to, next);
        }
    }
    
    private boolean countColors() {
        // summing the colors:
        colorCount[0] = 0;
        colorCount[1] = 0;
        colorCount[2] = 0;
        colorCount[3] = 0;
        for (Vertex v: polygon) 
            colorCount[v.color]++;
        // Coloring problem?
        if (colorCount[0] > 0)
            return false;
        // find min(colorCount):
        int minCount = Integer.MAX_VALUE;
        for (int i = 1; i < 4; i++)
            if (minCount > colorCount[i]) {
                minColor = i;
                minCount = colorCount[i];
            } else if ((minCount == colorCount[i]) && (minColor > i)) {
                minColor = i;
            }
        return true;
    }
    
    public int getMinColor() {
        return minColor;
    }
    
    public int getColor(int i) {
        if ((i < 0) || (i >= polygon.size()))
            return 0;
        return polygon.get(i).color;
    }
    
    public int getColorCount(int i) {
        if (i >= 0 && i < 4)
            return colorCount[i];
        return 0;
    }
    
    public Set<Edge> getDiagonals() {
        return diagonals.keySet();
    }

    @Override
    public boolean isSolved() {
        return triangulated;
    }

    @Override
    public int getGuardStatus() {
        return minColor;
    }

    @Override
    public boolean isGuard(int i) {
        return polygon.get(i).color == minColor;
    }

    @Override
    public int getStatus(int i) {
        return polygon.get(i).color;
    }

    @Override
    public String getSummary() {
        String colors = "  Totals:";
        for (int i = 0; i < 4; i++) 
            colors += "  color[" + i + "] = " + colorCount[i];
        return colors;
    }

    static public Vertex[] triangle() {
        int i = 0;
        Vertex[] v = new Vertex[3];
        v[i++] = new Vertex(10,10);
        v[i++] = new Vertex(30,10);
        v[i++] = new Vertex(10,30);
        return v;
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
        Vertex v[] = ex2();
        int n = v.length;
        int i;

        System.out.println("Size = " + n);
        Polygon p = new Polygon();
        BasicTriangulation triagulation = new BasicTriangulation();
        triagulation.setPolygon(p);
        for (i = 0; i < n; i++) {
            p.add(v[i]);
            System.out.println("-- " + v[i]);
        }
        System.out.println("Area = " + p.area());
        p.add(v[0]);
        if (triagulation.solve(0)) {
            for (i = 0; i < n; i++) {
                System.out.println(v[i]);
            }
            for (Edge e: triagulation.getDiagonals()) {
                System.out.println(e);
            }
            System.out.println("Min(colors) = " + triagulation.getMinColor());
        } else {
            System.out.println("Erro!");
        }
    }

}
