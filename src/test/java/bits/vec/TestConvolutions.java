/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;


@SuppressWarnings( "unused" )
public class TestConvolutions {
   
    public static void main( String[] args ) throws Exception {
        testConvolve();
    }

    
    static void testConvolve() throws Exception {
        BufferedImage im = testImage();
        final int w = im.getWidth();
        final int h = im.getHeight();
        float[][] src = Images.imageToRgbPlanes( im, null, null );
        src[1] = src[2] = src[0];
        float[][] dst = new float[3][];
        dst[0] = dst[1] = dst[2] = new float[w*h];
        
        float[] kern  = new float[25];
        Convolutions.binomialKernel( kern );
        Arrays.fill( kern, 1f / 25f );
        
        Convolutions.convolveFillZero1( src[0], 0, w, h, 1, w, kern, dst[0], 0 );
        ImagePanel.showImage( im );
        ImagePanel.showImage( Images.rgbPlanesToImage( dst, w, h, null, null ) );
        
        Convolutions.convolveFillZero1( dst[0], 0, h, w, w, 1, kern, src[0], 0 );
        ImagePanel.showImage( Images.rgbPlanesToImage( src, w, h, null, null ) );
        
    }
    
    
    private static BufferedImage testImage() throws Exception {
        return ImageIO.read( new File( "resources/ansel0.jpg" ) );
    }
    
    
    private static BufferedImage testImageTiny() throws Exception {
        return ImageIO.read( new File( "resources/ansel0_tiny.png" ) );
    }
    
}
