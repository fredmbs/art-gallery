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
