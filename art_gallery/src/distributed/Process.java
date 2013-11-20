package distributed;

public class Process implements Comparable<Process>{
    private int x, y;
    
    public Process(int x, int y) {
        setPos(x, y);
    }
    
    public Process(Process process) {
        setPos(process.x, process.y);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setPos(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Process other) {
        if (this.x == other.x)
            return (this.y - other.y); 
        return (this.x - other.x);
    }

    /*
    @Override
    public int compareTo(Process other) {
        // this comparator was meant to compatibility with double type 
        if (this.x == other.x) {
            if (this.y == other.y)
                return 0;
            return (this.y < other.y) ? 1: -1;
        }
        return (this.x < other.x) ? 1: -1;
    }
    */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Process other = (Process) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

}
