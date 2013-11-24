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

import java.util.Collections;
import java.util.HashMap;

import local.Polygon;
import local.Vertex;

public class ArtGallery {

    private Process process;
    private HashMap<Process, Integer> processes;
    private int count = 0;
    private Polygon polygon;
    private ArtGalleryAlgorithm algorithm;
    
    public ArtGallery(ArtGalleryAlgorithm a, Process p) {
        algorithm = a;
        process = p;
        polygon = new Polygon();
        polygon.add(new Vertex(p.getX(), p.getY()));
        algorithm.setPolygon(polygon);
        processes = new HashMap<Process, Integer>();
        processes.put(p, count++);
    }
    
    public Process getProcess() { 
        return process; 
    };
    
    public Polygon getPolygon() {
        return polygon;
    }
    
    public ArtGalleryAlgorithm getAlgorithm() {
        return algorithm;
    }
    
    public boolean isComplete() {
        return polygon.isClosed();
    }

    public boolean addRemoteProcessInOrder(Process p) {
        if (processes.containsKey(p)) {
            if (process == p)
                return polygon.close();
            return false;
        }
        processes.put(p, count++);
        return polygon.add(new Vertex(p.getX(), p.getY()));
    }
    
    public boolean guard() {
        Process minProcess = Collections.min(processes.keySet());
        int min = processes.get(minProcess);
        return algorithm.solve(min);
    }

    public boolean isGuard(Process p) {
        return algorithm.isGuard(0);
    }
    
    protected int getGuardStatus() {
        return algorithm.getGuardStatus();
    }
    
    public boolean hasProcess(Process p) {
        return processes.containsKey(p);
    }
    
    public int getNumProcesses() {
        return count;
    }
    
    public Integer getStatus(Process p) {
        Integer i = processes.get(p);
        if (i == null)
            return 0;
        return algorithm.getStatus(i);
    }
    
    public String getSummary() {
        return algorithm.getSummary();
    }

    @Override
    public String toString() {
        return process + "[" + algorithm.getStatus(0) + "]";
    }

}
