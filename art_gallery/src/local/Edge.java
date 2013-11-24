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
public class Edge {
    protected Vertex v0, v1;

    public Edge(Vertex v1, Vertex v2) {
        if (v1.compareTo(v2) <= 0) {
            this.v0 = v1;
            this.v1 = v2;
        } else {
            this.v0 = v2;
            this.v1 = v1;
        }
    }
    
    public Vertex getV0() { return v0; };

    public Vertex getV1() { return v1; }
    
    public boolean contains(Vertex v) {
        return v0.equals(v) || v1.equals(v);
    }

    public boolean contains(Edge e) {
        return v0.equals(e.v0) || v0.equals(e.v1) || 
               v1.equals(e.v0) || v1.equals(e.v1);
    }

    // cross product of 3D with z = 0
    private double cp(Vertex v2) {
        return (((v1.x-v0.x)*(v2.y-v0.y))-((v1.y-v0.y)*(v2.x-v0.x)));
    }
    
    private boolean within(int p, int q, int r) {
        return ((p <= q) && (q <= r)) || ((r <= q) && (q <= p));
    }
    
    public boolean intersect(Edge other) {
        double cp00 = this.cp(other.v0);
        double cp01 = this.cp(other.v1);
        double cp10 = other.cp(this.v0);
        double cp11 = other.cp(this.v1);
        return (((cp00 * cp01) < Polygon.EPSILON) && 
                ((cp10 * cp11) < Polygon.EPSILON));
    }
    
    public boolean intersect(Vertex v) {
        double cp = this.cp(v);
        // if collinear and within the segment, them intersect. 
        if (Math.abs(cp) < Polygon.EPSILON) {
            if (v0.x == v1.x) 
                return within(v0.y, v.y, v1.y);
            else
                return within(v0.x, v.x, v1.x);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "[" + v0 + ", " + v1 + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((v0 == null) ? 0 : v0.hashCode());
        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        if (v0 == null) {
            if (other.v0 != null)
                return false;
        } else if (!v0.equals(other.v0))
            return false;
        if (v1 == null) {
            if (other.v1 != null)
                return false;
        } else if (!v1.equals(other.v1))
            return false;
        return true;
    };

}
