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

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ArtGalleryViewer extends JPanel {

    private ArtGalleryVisual visual;
    private ArtGalleryApp app;
    private boolean changed = false;
    
    public ArtGalleryViewer(ArtGalleryApp app) {
        this.app = app;
        visual = null;
    }
    
    @Override
    public void repaint() {
        changed = true; 
        super.repaint();
    }
    
    public void refresh() {
        repaint();
        app.refresh();
    }
    
    public void setVisual(ArtGalleryVisual gallery) {
        ArtGalleryVisual oldVisual = this.visual;
        if (oldVisual != null) 
            oldVisual.setViewer(null);
        gallery.setViewer(this);
        this.visual = gallery;
        refresh();
    }
    
    public void clear(Graphics g) {
        Color oc = g.getColor();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(oc);
    }

    /**
     * Handles painting entire contents of DelaunayPanel.
     * Called automatically; requested via call to repaint().
     * @param g the Graphics context
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if (visual == null || !changed) return;
        visual.draw(g, app.triangulation.isSelected());
    }

}
