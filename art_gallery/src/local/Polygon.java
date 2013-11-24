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

public class Polygon extends ArrayList<Vertex> {
    
    /**
     * 
     */
    private static final long serialVersionUID = -49433842819633387L;

    // In case of float point operation:  
    final static double EPSILON = 0.000000001;
    
    private boolean closed;
    // Any order! The triangulation must to adjust the orientation.
    
    public Polygon() {
        closed = false;
    }
    
    public boolean close() {
            if (size() < 3)
                return false;
            closed = true;
            return true;
    }
    
    @Override
    public boolean add(Vertex v) {
        if (closed) return false;
        if (size() > 0 && v.equals(get(0))) {
            return close();
        }
        super.add(v);
        return true;
    }

    public boolean isClosed() {
        return closed;
    }
    
    public double area()
    {
        int n = size();
        double area = 0.0d;
        for(int p = n - 1, q = 0; q < n; p = q++) {
            area += get(p).getX() * get(q).getY() - 
                    get(q).getX() * get(p).getY();
        }
        return area / 2.0d;
    }    
    
}
