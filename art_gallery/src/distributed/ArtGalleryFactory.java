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

import local.BasicTriangulation;
import local.FieldOfView;

public class ArtGalleryFactory {
    
    static public String[] algorithms = {
        "Basic triangulation", 
        "Field Of View (incomplete)"
    };
    
    static public Class<?>[] classes  = {
        BasicTriangulation.class,
        FieldOfView.class
    };
    
    private int algorithm = 0;
    
    public boolean setAlgorithm(int i) {
        if (i >= 0 && i < algorithms.length) {
            algorithm = i;
            return true;                    
        }
        return false;
    }
    
    protected ArtGalleryAlgorithm construct() {
        try {
            return (ArtGalleryAlgorithm)
                    (ArtGalleryFactory.classes[algorithm].newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ArtGallery construct(Process p) {
            return new ArtGallery(construct(), p);
    }
}
