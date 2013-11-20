package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ArtGalleryApp extends java.applet.Applet 
implements Runnable, ActionListener, ListSelectionListener, 
           MouseListener, MouseMotionListener, ItemListener
{
    protected JLabel mousePos = new JLabel("(x,y)");
    
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
    
    private ArtGalleryViewer viewer;
    private JFrame dWindow;
    
    private String windowTitle = "The Art Gallery Problem"; 
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
        //this.init();                    // Applet initialization
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
    public void init () {
        try {SwingUtilities.invokeAndWait(this);}
        catch (Exception e) {System.err.println("Initialization failure");}
    }

    static int test = 0;
    private void refresh(JComponent c) {
        //c.revalidate();
        c.repaint();
        JComponent pane = c.getRootPane();
        if (pane != null) {
            //pane.revalidate();
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
        refresh(scrollPane);
    }
    
    protected JCheckBox triangulation = new JCheckBox("Triangulation");

    private void initViewerControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(triangulation);
        triangulation.doClick();
        triangulation.addItemListener(this);
        panel.add(mousePos);
        add("South",panel);
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
   
    /**
     * Some not used, but needed for MouseListener.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
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
    static int teste = 0;
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() == triangulation) {
            viewer.repaint();        
        }
    }
    
}