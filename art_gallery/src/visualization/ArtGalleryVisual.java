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

package visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.concurrent.locks.ReentrantLock;

import local.BasicTriangulation;
import local.Edge;
import local.Vertex;

import distributed.ArtGallery;
import distributed.ArtGalleryAlgorithm;
import distributed.Process;



public class ArtGalleryVisual extends ArtGallery {

    final static Color processColor[] = { Color.BLACK, 
        Color.RED, Color.GREEN, Color.BLUE };
    final static Color diagonalColor = Color.YELLOW; //new Color(1.0f, 1.0f, 0.0f, 0.5f);
    final static Color galleryColor = Color.BLACK;
    final static int radius = 4;
    
    private java.awt.Polygon visualPolygon;

    public ArtGalleryVisual(ArtGalleryAlgorithm a, Process p) {
        super(a, p);
        this.visualPolygon = new java.awt.Polygon();
        visualPolygon.addPoint(p.getX(), p.getY());
        // must to be the last call, because visualPolygon construct.
        ArtGalleryApp app = ArtGalleryApp.getApp();
        if (app != null) app.addVisual(this);
    }

    // Avoid race condition caused by 
    // simultaneous method invocation originated by other applications.
    private final ReentrantLock lock = new ReentrantLock();
    
    public void lock() {
        lock.lock();
    }
    
    public void unlock() {
        lock.unlock();
    }
    
    // visual parent
    ArtGalleryViewer viewer;
    protected void setViewer(ArtGalleryViewer parent) {
        lock.lock(); 
        try {
            this.viewer = parent;
        } finally {
            lock.unlock();
        }
    };

    @Override
    public boolean addRemoteProcessInOrder(Process p) {
        lock.lock();  // block until condition holds
        try {
            if (super.addRemoteProcessInOrder(p)) {
                visualPolygon.addPoint(p.getX(), p.getY());
                if (viewer != null) {
                    viewer.repaint();
                }
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean guard() {
        lock.lock();  // block until condition holds
        try {
            if (super.guard()) {
                if (viewer != null) {
                    viewer.setMessage(getSummary());
                    viewer.refresh();
                }
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Handles painting entire contents of viewer.
     * @param g the Graphics context
     */
    public void draw (Graphics g, boolean showDiagonals) {
        Color oc = g.getColor();
        g.setColor(galleryColor);
        //g.fillPolygon(p);
        g.drawPolygon(visualPolygon);
        drawInitialVertex(g);
        for (Vertex v: getPolygon())
            drawVertex(g, v);
        if (showDiagonals && (getAlgorithm() instanceof BasicTriangulation))
            for (Edge e: ((BasicTriangulation) getAlgorithm()).getDiagonals())
                drawEdge(g, e);
        g.setColor(oc);
    }

    public void drawVertex (Graphics g, Vertex v) {
        int x = v.getX();
        int y = v.getY();
        int c = v.getColor();
        g.setColor(processColor[c]);
        if (c == getGuardStatus())
            g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
        else
            g.drawOval(x-radius, y-radius, 2*radius, 2*radius);
    }
    
    public void drawInitialVertex(Graphics g) {
        Vertex v = getPolygon().get(0);
        int x = v.getX();
        int y = v.getY();
        int r = radius + 3;
        int c = v.getColor();
        g.setColor(processColor[c]);
        Graphics2D g2 = (Graphics2D)g;
        Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(2.5f));
        g.drawOval(x-r, y-r, r+r, r+r);
        g2.setStroke(os);
    }
    
    public void drawEdge(Graphics g, Edge e) {
        g.setColor(diagonalColor);
        g.drawLine(e.getV0().getX(), e.getV0().getY(), 
                e.getV1().getX(), e.getV1().getY());
    }
    
}
