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
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class Polygon implements Iterable<Vertex> {
    
    // In case of float point operation:  
    final static double EPSILON = 0.000000001;

    // Avoid race condition caused by 
    // simultaneous method invocation originated by other applications.
    private final ReentrantLock lock;
    
    private boolean closed;
    // Any order! The triangulation must to adjust the orientation.
    ArrayList<Vertex> vertexes;
    
    public Polygon() {
        closed = false;
        lock = new ReentrantLock();
        vertexes = new ArrayList<Vertex>(); 
    }
    
    public boolean close() {
        lock.lock(); 
        try {
            if (vertexes.size() < 3)
                return false;
            closed = true;
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean add(Vertex v) {
        if (closed) return false;
        lock.lock(); 
        try {
            if (vertexes.size() > 0 && v.equals(vertexes.get(0))) {
                return close();
            }
            vertexes.add(v);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Vertex get(int i) {
        lock.lock(); 
        try {
            return vertexes.get(i);
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock(); 
        try {
            return vertexes.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean inClockwise() {
        return (area() < 0);
    }
    
    public boolean inCounterclockwise() {
        return (area() > 0);
    }
    
    public boolean isCollinear() {
        return (area() == 0);
    }
    
    public ArrayList<Vertex> reverseClone() {
        lock.lock(); 
        try {
            ArrayList<Vertex> clone = new ArrayList<Vertex>();
            clone.add(vertexes.get(0));
            for (int i = vertexes.size() - 1; i > 0; i--)
                clone.add(vertexes.get(i));
            return clone;
        } finally {
            lock.unlock();
        }
    }
    
    public ArrayList<Vertex> clone() {
        @SuppressWarnings("unchecked")
        ArrayList<Vertex> clone = (ArrayList<Vertex>)vertexes.clone();
        return clone;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    public double area()
    {
        lock.lock(); 
        try {
            int n = vertexes.size();
            double area = 0.0d;
            for(int p = n - 1, q = 0; q < n; p = q++) {
                area += vertexes.get(p).getX() * vertexes.get(q).getY() - 
                        vertexes.get(q).getX() * vertexes.get(p).getY();
            }
            return area / 2.0d;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator<Vertex> iterator() {
        return vertexes.iterator();
    }    
    
    
}
