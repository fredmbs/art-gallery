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

public class Vertex {
    protected int x, y;
    protected Vertex next = null;
    protected Vertex diagonal = null;
    protected int color = 0;

    public Vertex(int x, int y) {
        setLocation(x, y);
    }
    
    public Vertex(Vertex pos) {
        setLocation(pos.x, pos.y);
    }
    
    public int getX() { return x; };
    public int getY() { return y; };

    public int getColor() { return color; };

    public Vertex getNext() { return next; };
    public void setNext(Vertex v) { next = v; };

    public Vertex getDiagonal() { return diagonal; };
    // here, we can check the triangle orientation... 
    // but, this is the responsibility of the class that make the triangulation
    public void setDiagonal(Vertex v) { diagonal = v; };

    private void setLocation(int x, int y) { 
        this.x = x;
        this.y = y;
        this.color = 0;
    }

    @Override
    public String toString() {
        return "Vertex [(" + x + ", " + y + "), color=" + color + "]";
    }
    
}
