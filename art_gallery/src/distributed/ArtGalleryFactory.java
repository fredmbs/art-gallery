package distributed;



public class ArtGalleryFactory {
    public ArtGallery construct(Process p) {
            return new ArtGallery(p);
    }
}
