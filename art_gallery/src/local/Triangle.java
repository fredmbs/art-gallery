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
    
    final static double EPSILON = 0.000000001;
    static int n = 0;
    Vertex v0, v1, v2;

    public void set(Vertex v0, Vertex v1, Vertex v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    static private double direction(Vertex v0, Vertex v1, Vertex v2) {
        return (((v1.x-v0.x)*(v2.y-v0.y))-((v1.y-v0.y)*(v2.x-v0.x)));
    }

    public boolean inCCW() {
        return direction(v0, v1, v2) < EPSILON;
    }
    
    public boolean hasVertexInside(Collection<Vertex> vertexes) {
        for(Vertex v: vertexes)
            if (!hasVertex(v) && inCollision(v)) 
                return true;
        return false;
    }
    
    public boolean inCollision(Vertex p) {
        boolean d0 = direction(p, v0, v1) < 0.0;
        boolean d1 = direction(p, v1, v2) < 0.0;
        boolean d2 = direction(p, v2, v0) < 0.0;
        return ((d0 == d1) && (d1 == d2));
    }
    
    public boolean hasVertex(Vertex p) {
        return (p == v0) || (p == v1) || (p == v2);
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
    
    public void coloring() {
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
        return "Triangle => (" + v0.x + "," + v0.y + ") (" +
                v1.x + "," + v1.y + ") (" + v2.x + "," + v2.y + ")";
    
    }
    
}
