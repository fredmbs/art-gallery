package local;

public class Diagonal {
    
    private Triangle t0;
    private Triangle t1;

    public Diagonal(Triangle triangle) {
        this.t0 = triangle;
        this.t1 = null;
    }
    
    public Triangle getOpposite(Triangle of) {
        if (this.t0 == of) {
            return this.t1;
        }
        return this.t0;
    }
    
    public boolean setRelated(Triangle triangle) {
        if (this.t1 == null) {
            this.t1 = triangle;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "[" + t0 + ", " + t1 + "]";
    }

}
