package visualization;

import distributed.ArtGallery;
import distributed.ArtGalleryFactory;
import distributed.Process;

public class ArtGalleryVisualFactory extends ArtGalleryFactory {
    @Override
    public ArtGallery construct(Process p) {
                return new ArtGalleryVisual(p);
    }
}
