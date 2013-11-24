package local;

import java.util.HashSet;
import java.util.Set;

public class FieldOfView extends BasicTriangulation {

    private Polygon polygon; 
    private HashSet<Edge> sides;
    private HashSet<Edge> diagonals;
    
    @Override
    public void setPolygon(Polygon p) {
        super.setPolygon(p);
        polygon = p;
        sides = new HashSet<Edge>();
        diagonals = new HashSet<Edge>();
    }
    
    @Override
    public boolean solve(int init) {
        boolean result = super.solve(init);
        test();
        return result;
    }

    public Set<Edge> getDiagonals() {
        return diagonals;
    }
    
    private Edge makeDiagonal(Vertex from, Vertex to) {
        Edge diag = new Edge(from, to);
        for (Edge side: sides) {
            if (!side.contains(diag) && diag.intersect(side))
                return null;
        }
        //if (Triangle.inCCW(diag, pointsInOrder.get(i+1)))
            return diag;
        //return null;
    }
    
    public void test()
    {
        int n = polygon.size();
        sides.clear();
        for(int p = n - 1, q = 0; q < n; p = q++) {
            sides.add(new Edge(polygon.get(p), polygon.get(q)));
        }
        Edge diag;
        Vertex v = polygon.get(0);
        diagonals.clear();
        for (int i = 2; i < (n - 1); i++) {
            if ((diag = makeDiagonal(v, polygon.get(i))) != null)  
                diagonals.add(diag);
        }
    }
        
}
