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
// ----------------------------------------------------------------------
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import local.Vertex;
import visualization.ArtGalleryApp;
import visualization.ArtGalleryVisualFactory;
import daj.Application;
import daj.Node;
import distributed.ArtGalleryFactory;
import distributed.Prog;
// 
// 
// ----------------------------------------------------------------------

// ------------------------------------------------------------------------
//
//
// ------------------------------------------------------------------------
@SuppressWarnings("serial")
public class Main extends Application {
    private int id;
    private Configuration cfg;
    private ArtGalleryFactory factory;
    private static int width = 600;
    private static int height = 600; 
    // ----------------------------------------------------------------------
    // main function of application
    // ----------------------------------------------------------------------
    public static void main(String[] args) {
        Application app = new Main();
        app.nodeRadius = 7;
        app.channelRadius = 4;
        app.channelWidth = 2;
        app.nodeNormalFont = app.nodeSmallFont;
        ArtGalleryApp.start(false);
        app.run();
    }
    
    // ----------------------------------------------------------------------
    // constructor for application
    // ----------------------------------------------------------------------
    public Main () {
        super("Distributed Art Gallery", Main.width, Main.height);
        factory = new ArtGalleryVisualFactory();
        cfg = new Configuration();
        cfg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        id = 0;
    }
    
    private Node newNode(int x, int y)
    {
        int newId = id++;
        Prog newProg = new Prog(factory, x, y);
        // convert from space (x, y) to canvas (x, y), if necessary.
        Node node = node(newProg, Integer.toString(newId), x, y);
        newProg.setNode(node);
        return node;
    }
    
    private void edge(Node a, Node b) {
        // here we can change the orientation...
        //link(a, b);
        link(b, a);
    }
    
    // ----------------------------------------------------------------------
    // construction of network
    // ----------------------------------------------------------------------
    public void construct() {

        ArtGalleryApp.clear();
        
        this.requestFocus();
        cfg.ok = false;
        while (!cfg.ok) {
            cfg.setVisible(true);
            if (!cfg.ok) System.exit(0);
            
            factory.setAlgorithm(cfg.algorithm);
            
            switch(cfg.testCase) {
            case 0:
                gallery1();
                break;
            case 1:
                simpleGallery();
                break;
            case 2:
                gallery2();
                break;
            case 99:
                if (!read(cfg.fileName)) {
                    cfg.ok = false;
                }
                break;
            default:
                System.exit(1);
            }
        }
        this.repaint();
    }
    
    private void simpleGallery() {
        int i = 0, n = 8;
        Node nodes[] = new Node[n];
        nodes[i++] = newNode(100,100);
        nodes[i++] = newNode(100,400);
        nodes[i++] = newNode(400,400);
        nodes[i++] = newNode(400,300);
        nodes[i++] = newNode(200,300);
        nodes[i++] = newNode(200,200);
        nodes[i++] = newNode(400,200);
        nodes[i++] = newNode(400,100);
        for (i = 0; i < n - 1; i++)
            edge(nodes[i], nodes[i+1]);
        edge(nodes[n-1], nodes[0]);
    }
    
    private void gallery1() {
        Node nodes[] = new Node[42];
        nodes[ 0] = newNode( 20,  20); 
        nodes[ 1] = newNode( 20, 230); 
        nodes[ 2] = newNode(130, 230); 
        nodes[ 3] = newNode(130, 260); 
        nodes[ 4] = newNode( 20, 260); 
        nodes[ 5] = newNode( 20, 440); 
        nodes[ 6] = newNode( 90, 440); 
        nodes[ 7] = newNode( 90, 510); 
        nodes[ 8] = newNode(200, 510); 
        nodes[ 9] = newNode(200, 330); 
        nodes[10] = newNode(160, 330); 
        nodes[11] = newNode(160, 300); 
        nodes[12] = newNode(200, 300); 
        nodes[13] = newNode(200, 230); 
        nodes[14] = newNode(230, 230); 
        nodes[15] = newNode(230, 300); 
        nodes[16] = newNode(270, 300); 
        nodes[17] = newNode(270, 330); 
        nodes[18] = newNode(230, 330); 
        nodes[19] = newNode(230, 510); 
        nodes[20] = newNode(380, 510); 
        nodes[21] = newNode(380, 470); 
        nodes[22] = newNode(410, 470); 
        nodes[23] = newNode(410, 510); 
        nodes[24] = newNode(510, 510); 
        nodes[25] = newNode(510, 330); 
        nodes[26] = newNode(410, 330); 
        nodes[27] = newNode(410, 370); 
        nodes[28] = newNode(380, 370); 
        nodes[29] = newNode(380, 300); 
        nodes[30] = newNode(510, 300); 
        nodes[31] = newNode(510, 120); 
        nodes[32] = newNode(380, 120); 
        nodes[33] = newNode(380,  90); 
        nodes[34] = newNode(510,  90); 
        nodes[35] = newNode(510,  20); 
        nodes[36] = newNode(160,  20); 
        nodes[37] = newNode(160,  90); 
        nodes[38] = newNode(300,  90); 
        nodes[39] = newNode(300, 120); 
        nodes[40] = newNode(130, 120); 
        nodes[41] = newNode(130,  20);
        for (int i=0; i < 41; i++)
            edge(nodes[i], nodes[i+1]);
        edge(nodes[41], nodes[0]);
    }

    private void gallery2() {
        int i = 0;
        Vertex[] v = new Vertex[15];
        v[i++] = new Vertex(0,6);
        v[i++] = new Vertex(0,0);
        v[i++] = new Vertex(3,0);
        v[i++] = new Vertex(4,1);
        v[i++] = new Vertex(6,1);
        v[i++] = new Vertex(8,0);
        v[i++] = new Vertex(12,0);
        v[i++] = new Vertex(13,2);
        v[i++] = new Vertex(8,2);
        v[i++] = new Vertex(8,4);
        v[i++] = new Vertex(11,4);
        v[i++] = new Vertex(11,6);
        v[i++] = new Vertex(6,6);
        v[i++] = new Vertex(4,3);
        v[i++] = new Vertex(2,6);
        Node nodes[] = new Node[15];
        int x, y, f = 20;
        for (i=0; i < 15; i++) {
            x = ((v[i].getX() + 1) * f);
            y = ((v[i].getY() + 1) * f);
            nodes[i] = newNode(x, y);
        }
        for (i=0; i < 14; i++) {
            edge(nodes[i], nodes[i+1]);
        }
        edge(nodes[14], nodes[0]);
    }
    
    public boolean read(String from) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(from));
            try {
                int x, y, size = scanner.nextInt();
                Node nodes[] = new Node[size];
                for (int i = 0; i < size; i++) {
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    nodes[i] = newNode(x, y);
                }
                for (int i=0; i < (size - 1); i++) 
                    edge(nodes[i], nodes[i+1]);
                edge(nodes[size-1], nodes[0]);
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                        "Reading error.\n" + from + "\n Check file format.",
                        "Art Gallery File", JOptionPane.ERROR_MESSAGE);
                return false;
            } finally {
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                    "File not found!\n" + from,
                    "Art Gallery File", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    

    // ----------------------------------------------------------------------
    // informative message
    // ----------------------------------------------------------------------
    public String getText() {
        return  "Distributed Art Gallery \n" + 
                "\n------------------------------------------------------\n" +
                "\n Test case: " + 
                (cfg.testCase < cfg.testCases.length ?
                        cfg.testCases[cfg.testCase] : cfg.fileName);
    }
    
    @Override
    public void resetStatistics() {
        // TODO Auto-generated method stub
        
    }
}

