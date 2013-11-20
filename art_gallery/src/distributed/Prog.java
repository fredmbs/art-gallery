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

package distributed;

import daj.Node;
import daj.Program;

// ------------------------------------------------------------------------
//
// a program class
//
// ------------------------------------------------------------------------
public class Prog extends Program {
    
    private Process process;     // Local process.
    private int eventNumber;     // Local event index
    private ArtGallery gallery;  // Art Gallery control
    private Msg lastMsg;         // only to simulation display purpose
    private Node node = null;
    
    //static Process test = null;
    
    // ----------------------------------------------------------------------
    // called for initialization of program
    // ----------------------------------------------------------------------
    public Prog (ArtGalleryFactory factory, int x, int y) {
        this.process = new Process(x, y);
        // Create a Art Gallery controller  
        gallery = factory.construct(this.process);
        /*
        if (test == null) {
            test = this.process;
            System.out.println("Test @"+test);
        }
        */
    }
    
    @Override
    public void setNode(Node node) {
        if (node != null) {
            super.setNode(node);
            this.node = node;
        }
    }
    
    private void setLabel(String l) {
        if (node != null)
            node.getVisual().setLabel(l);
    }
    
    /*
    private void debug(Object o) {
        if (this.process.equals(test))
            System.out.println(o + "@" + this);
    }
    */
    
    // ----------------------------------------------------------------------
    // called for execution of program
    // ----------------------------------------------------------------------
    public void main() {
        int channel;
        // start event index
        eventNumber = 0;
        // Execute the algorithm
        // Broadcast the presence of this process. 
        send(process);
        setLabel("?");
        while (true) {
            // receive message
            if ((channel = in().select(1)) >= 0) {
                Msg in = getMessage(channel);
                Process p = in.getFrom();
                // is known process?
                if (gallery.hasProcess(p)) {
                    // is this process?
                    if (p.equals(this.process)) {
                        // terminate the algorithm!
                        gallery.guard();
                        // mark the color in DAJ
                        String label = gallery.getColor(process).toString();
                        if (gallery.isGuard(process)) 
                            this.setLabel("!" + label);
                        else
                            this.setLabel(label);
                        return;
                    }
                } else {
                 // forward unknown remote process
                    if (gallery.addRemoteProcessInOrder(p)) {
                        flood(in);
                    }
                }
            }
        }
    }

    /*
     * Remove a message from channel buffer and adjust local event index.
     */ 
    private Msg getMessage(int index) {
        Msg in = (Msg)(in(index).receive(1));
        if (in != null) {
            ++eventNumber;
            in = new Msg(in);
            lastMsg = in;
        }
        return in;
    }

    /*
     * Create message with current process information and send to all channels.
     */ 
    private Msg send(Process process) {
        Msg out = new Msg(process, ++eventNumber);
        out().send(out);
        return out;
    }

    // use this there are double channel (bidirectional simulation in DAJ)
    /*
    private void flood(Msg msg, int inChannel) {
        // send message to all channels, except one (originating)
        for(int i = 0; i < inChannel; i++) 
            out(i).send(msg);
        for(int i = (out().getSize() - 1); i > inChannel; i--) 
            out(i).send(msg);
    }
    */

    private void flood(Msg msg) {
        // send message to all channels
        out().send(msg);
    }

    /*
    private boolean forward(Msg msg, int outChannel) {
        if (outChannel >= 0 && outChannel < out().getSize()) { 
            out(outChannel).send(msg);
            return true;
        }
        return false;   
    }
    */

    // ----------------------------------------------------------------------
    // called for display of program state
    // ----------------------------------------------------------------------
    public String getText() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        return "Prog [process=" + process + ", msg=" + lastMsg + 
                ", #event=" + eventNumber + "]";
    }

}
