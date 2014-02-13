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
import java.util.HashSet;
import java.util.Set;

/*
 * Experimental
 */
public class FieldOfView extends BasicTriangulation {

    private Polygon polygon; 
    private HashSet<Edge> sides;
    private HashSet<Edge> diagonals;
    private ArrayList<Vertex> testp;
    
    @Override
    public void setPolygon(Polygon p) {
        super.setPolygon(p);
        polygon = p;
        sides = new HashSet<Edge>();
        diagonals = new HashSet<Edge>();
    }
    
    @Override
    public boolean solve(int init) {
        boolean result = super.solve(init);
        test();
        return result;
    }

    public Set<Edge> getDiagonals() {
        return diagonals;
    }
    
    private boolean validIncidence(int vi1, Edge diag) {
        int size = testp.size();
        int vi0 = (vi1 - 1 + size)%(size);
        int vi2 = (vi1 + 1)%(size);
        Vertex v0 = testp.get(vi0);
        Vertex v1 = testp.get(vi1);
        Vertex v2 = testp.get(vi2);
        Vertex opposite = diag.getOpposite(v1);
        if (opposite == null) return false;
        Triangle t = new Triangle(v0, v1, v2);
        Edge side = new Edge(v0, v2);
        boolean isEar = t.inClockwise();
        boolean inside = t.inCollision(opposite);
        boolean intersect = diag.intersect(side);
        if (isEar) return inside || intersect;
        return !(inside || intersect);
    }
    
    private Edge makeDiagonal(int i, int vi1) {
        Vertex from = testp.get(i);
        Vertex to = testp.get(vi1);
        Edge diag = new Edge(from, to);
        for (Edge side: sides) {
            if (diag.intersect(side) || 
                    !(validIncidence(vi1, diag) && validIncidence(i, diag)))
                return null;
        } 
        return diag;
    }
    
    private void makeSides() {
        int n = testp.size();
        sides.clear();
        for(int p = n - 1, q = 0; q < n; p = q++) {
            sides.add(new Edge(testp.get(p), testp.get(q)));
        }
    }
    
    public void test()
    {
        if (polygon.inClockwise()) {
            testp = polygon.clone();
        } else {
            testp = polygon.reverseClone();
        }
        makeSides();
        int n = testp.size();
        Edge diag;
        diagonals.clear();
        for (int i = 1; i < n; i++) {
            if ((diag = makeDiagonal(0, i)) != null)  
                diagonals.add(diag);
        }
    }
        
}
