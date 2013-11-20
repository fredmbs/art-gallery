package distributed;

import java.util.Collections;
import java.util.HashMap;

import local.Polygon;
import local.Vertex;

public class ArtGallery {

    private Polygon polygon = null;
    private Process process;
    private HashMap<Process, Integer> processes;
    private int count = 0;
    
    public ArtGallery(Process p) {
        process = p;
        polygon = new Polygon(new Vertex(p.getX(), p.getY()));
        processes = new HashMap<Process, Integer>();
        processes.put(p, count++);
    }
    
    public Process getProcess() { return process; };
    
    public boolean addRemoteProcessInOrder(Process p) {
        if (processes.containsKey(p)) {
            if (process == p)
                return polygon.close();
            return false;
        }
        processes.put(p, count++);
        return polygon.addVertexInOrder(new Vertex(p.getX(), p.getY()));
    }
    
    public boolean isComplete() {
        return polygon.isClosed();
    }

    public boolean guard() {
        Process minProcess = Collections.min(processes.keySet());
        int min = processes.get(minProcess);
        return polygon.triangulate(min);
    }

    public boolean isGuard(Process p) {
        return polygon.getColor(0) == polygon.getMinColor();
    }
    
    protected int getGuardColor() {
        return polygon.getMinColor();
    }
    
    public boolean hasProcess(Process p) {
        return processes.containsKey(p);
    }
    
    public Polygon getPolygon() {
        return polygon;
    }
    
    public int getNumProcesses() {
        return count;
    }
    
    public Integer getColor(Process p) {
        Integer i = processes.get(p);
        if (i == null)
            return 0;
        return polygon.getColor(i);
    }

    public Integer getColor(int i) {
        return polygon.getColor(i);
    }

    @Override
    public String toString() {
        return process + "[" + polygon.getColor(0) + "]";
    }

}
