/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;


public class TestMat {
    @Test
    public void testInverse() {
        double[] a = new double[16];
        double[] b = new double[16];
        double[] c = new double[16];
        double[] eye = new double[16];
        Mat.identity4( eye );
        Random rand = new Random( 100 );
        
        for( int i = 0; i < 100; i++ ) {
            for( int j = 0; j < 16; j++ ) {
                a[j] = rand.nextDouble() * 10.0 - 5.0;
            }
            
            if( !Mat.invert4( a, b ) ) {
                continue;
            }

            Mat.mult4( a, b, c );
            for( int j = 0; j < 16; j++ ) {
                boolean eq = Tol.equal( c[j], eye[j], 0.000001, 0.000001 );
                assertTrue( eq ); 
            }
        }
        
        Arrays.fill( a, 1.0 );
        assertFalse( Mat.invert4( a, b ) );
    }

    @Test
    public void testInverseSpeed() {
        double[] a = new double[16];
        double[] c = new double[16];
        Random rand = new Random( 100 );
        
        long t0 = 0;
        long t1 = 0;
        
        for( int i = 0; i < 100000; i++ ) {
            for( int j = 0; j < 16; j++ ) {
                a[j] = rand.nextDouble() * 1.0 - 5.0;
            }
            
            long start = System.nanoTime();
            
            for( int j = 0; j < 10; j++ ) {
                Mat.invert4( a, c );
            }
            
            t0 += System.nanoTime() - start;
            start = System.nanoTime();
            
            for( int j = 0; j < 10; j++) {
                //Matrices.invert2( a, c );
            }
            
            t1 += System.nanoTime() - start;
        }
        
        
        System.out.println( "Time0: " + ( t0 / 1000000000.0 ) );
        System.out.println( "Time1: " + ( t1 / 1000000000.0 ) );
    }

    @Test
    public void testTranspose3() {
        Random rand = new Random( 3 );
        Mat3 mat = randRotation( rand );
        Mat3 trans = new Mat3();
        Mat3 inv   = new Mat3();

        Mat.transpose( mat, trans );
        Mat.invert( mat, inv );

        Tests.assertNear( trans, inv );

        Mat.transpose( trans, trans );
        Tests.assertNear( trans, mat );

        Mat.invert( inv, inv );
        Tests.assertNear( inv, mat );
    }


    static void rotXyz( double rx, double ry, double rz, double[] out ) {
        double[] a = new double[16];
        double[] b = new double[16];
        
        Mat.getRotate4( rx, 1, 0, 0, a );
        Mat.getRotate4( ry, 0, 1, 0, out );
        Mat.mult4( out, a, b );
        Mat.getRotate4( rz, 0, 0, 1, a );
        Mat.mult4( a, b, out );
    }


    static Mat3 randRotation( Random rand ) {
        Mat3 mat = new Mat3();
        Vec4 quat = new Vec4();
        Quat.sampleUniform( rand, quat );
        Quat.quatToMat( quat, mat );
        return mat;
    }
}
