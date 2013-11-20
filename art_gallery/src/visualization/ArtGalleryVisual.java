package visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.concurrent.locks.ReentrantLock;

import local.Edge;
import local.Vertex;

import distributed.ArtGallery;
import distributed.Process;



public class ArtGalleryVisual extends ArtGallery {

    final static Color processColor[] = { Color.BLACK, 
        Color.RED, Color.GREEN, Color.BLUE };
    final static Color diagonalColor = new Color(1.0f, 1.0f, 0.0f, 0.5f);
    final static Color galleryColor = Color.BLACK;
    
    private java.awt.Polygon visualPolygon;

    public ArtGalleryVisual(Process p) {
        super(p);
        this.visualPolygon = new java.awt.Polygon();
        visualPolygon.addPoint(p.getX(), p.getY());
        // must to be the last call, because visualPolygon construct.
        ArtGalleryApp.getApp().addVisual(this);
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
                if (viewer != null) 
                    viewer.refresh();
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
        if (showDiagonals)
            for (Edge e: getPolygon().getDiagonals())
                drawEdge(g, e);
        g.setColor(oc);
    }

    public void drawVertex (Graphics g, Vertex v) {
        int x = v.getX();
        int y = v.getY();
        int r = 4;
        int c = v.getColor();
        g.setColor(processColor[c]);
        if (c == getGuardColor())
            g.fillOval(x-r, y-r, r+r, r+r);
        else
            g.drawOval(x-r, y-r, r+r, r+r);
    }
    
    public void drawInitialVertex(Graphics g) {
        Vertex v = getPolygon().get(0);
        int x = v.getX();
        int y = v.getY();
        int r = 7;
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
