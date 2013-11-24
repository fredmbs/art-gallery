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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ArtGalleryApp extends java.applet.Applet 
implements Runnable, ActionListener, ListSelectionListener, 
           MouseListener, MouseMotionListener, ItemListener
{
    static private ArtGalleryApp app = null;
    
    static public void start(boolean mainApp) {
        if (app != null) {
            app.disposable();
        }
        app = new ArtGalleryApp();
        app.configure(mainApp);
        app.run();
    }
    
    static public ArtGalleryApp getApp() {
        return app;
    }
    
    static public void clear() {
        app.restart();
    }
    
    private ArtGalleryViewer viewer;
    private String windowTitle = "Distributed Art Gallery Visualizer"; 

    private JFrame dWindow;
    private DefaultListModel<ArtGalleryVisual> listModel;
    private JList<ArtGalleryVisual> list;
    private JScrollPane scrollPane = new JScrollPane(list);

    public void disposable() {
        dWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Main program (used when run as application instead of applet).
     */
    public void configure(boolean mainWindow) {
        this.init();                    // Applet initialization
        dWindow = new JFrame();         // Create window
        dWindow.setSize(750, 620);      // Set window size
        dWindow.setTitle(windowTitle);  // Set window title
        dWindow.setLayout(new BorderLayout());   // Specify layout manager
        // Specify closing behavior
        if (mainWindow) 
            dWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else
            dWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dWindow.add(this, "Center");           // Place applet into window
        dWindow.setFocusable(true);
        dWindow.setVisible(true);              // Show the window
    }
    
    /**
     * Initialize the applet.
     * As recommended, the actual use of Swing components takes place in the
     * event-dispatching thread.
     */
    //public void init () {
    //    try {SwingUtilities.invokeAndWait(this);}
    //    catch (Exception e) {System.err.println("Initialization failure");}
    //}

    private void restart() {
        listModel.clear();
        viewer.setVisual(null);
        refresh(scrollPane);
    }
    
    private void refresh(JComponent c) {
        c.revalidate();
        c.repaint();
        JComponent pane = c.getRootPane();
        if (pane != null) {
            pane.revalidate();
            pane.repaint();
        }
    }
    
    public void initProcessSelector() {
        //
        listModel = new DefaultListModel<ArtGalleryVisual>();
        list = new JList<ArtGalleryVisual>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        scrollPane = new JScrollPane(list);
        this.add(scrollPane, "West");
    }
    
    public void addVisual(ArtGalleryVisual gallery) {
        listModel.addElement(gallery);
        list.setSelectedValue(gallery, true);
        viewer.setVisual(gallery);
        refresh(scrollPane);
    }
    
    public void delVisual(ArtGalleryVisual gallery) {
        listModel.removeElement(gallery);
        refresh(scrollPane);
    }
    
    public void refresh() {
        scrollPane.repaint();
    }

    public void selectAt(int x, int y) {
        int px, py;
        int r = 6;
        for (int i = 0; i < listModel.size(); i++) {
            ArtGalleryVisual agv = listModel.get(i); 
            px = agv.getProcess().getX();
            py = agv.getProcess().getY();
            if (x >= px-r && x <= px+r && y >= py-r && y <= py+r) {
                list.setSelectedValue(agv, true);
            }
        }
    }

    
    /**
     * Set up the applet's GUI.
     * As recommended, the init method executes this in the event-dispatching
     * thread.
     */
    public void run()
    {
        setLayout(new BorderLayout());
        viewer = new ArtGalleryViewer(this);
        viewer.addMouseMotionListener(this);
        viewer.addMouseListener(this);
        viewer.setBackground(Color.WHITE);
        add("Center",viewer);
        initViewerControls();
        initProcessSelector();
    }
    
    /**
     * A button has been pressed; redraw the picture.
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println(((AbstractButton)e.getSource()).getText());
        viewer.repaint();
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            ArtGalleryVisual diagram = list.getSelectedValue();
            viewer.setVisual(diagram);
        }
    }
   
    protected JPanel controlPanel = new JPanel();
    protected JCheckBox triangulation = new JCheckBox("Triangulation");
    protected JLabel mousePos = new JLabel("(x,y)");
    protected JLabel text = new JLabel("");
    
    public void setMessage(String msg) {
        text.setText(msg);
    }
    
    private void initViewerControls() {
        controlPanel.setLayout(new BorderLayout());
        triangulation.doClick();
        triangulation.addItemListener(this);
        controlPanel.add("West", triangulation);
        controlPanel.add("Center", text);
        controlPanel.add("East", mousePos);
        add("South",controlPanel);
    }
    
    /**
     * Some not used, but needed for MouseListener.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == viewer) {
            selectAt(e.getX(), e.getY());
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseDragged(MouseEvent arg0) {}
    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getSource() == viewer) {
            mousePos.setText("(" + e.getX() + "," + e.getY() + ")");
            mousePos.repaint();
        }
    }

    /**
     * ItemListener
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() == triangulation) {
            viewer.repaint();        
        }
    }
    
}