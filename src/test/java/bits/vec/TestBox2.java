/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.Random;

import org.junit.*;
import static org.junit.Assert.*;

public class TestBox2 {

    @Test
    public void testFit() {
        Random rand = new Random( 0 );
        double[][] a = new double[128][4];
        double[][] b = new double[128][4];
        Box2[] ra;
        Box2[] rb;
        double[] c = new double[4];
        
        fill( rand, a );
        fill( rand, b );
        ra = toBox2( a );
        rb = toBox2( b );
        
        for( int i = 0; i < a.length; i++ ) {
            Box2 r = new Box2();
            Box.fit( ra[i], rb[i], r );
            Box.fit2( a[i], b[i], c );
            assertTrue( equiv( r, c ) );
        }
    }

    @Test
    public void testClamp() {
        Random rand = new Random( 0 );
        double[][] a = new double[128][4];
        double[][] b = new double[128][4];
        Box2[] ra;
        Box2[] rb;
        double[] c = new double[4];
        
        fill( rand, a );
        fill( rand, b );
        ra = toBox2( a );
        rb = toBox2( b );
        
        for( int i = 0; i < a.length; i++ ) {
            Box2 r = new Box2();
            Box.clamp( ra[i], rb[i], r );
            Box.clamp2( a[i], b[i], c );
            assertTrue( equiv( r, c ) );
        }
    }

    @Test
    @Ignore
    public void testSpeed() {
        Random rand = new Random( 0 );
        double[][] a = new double[128][4];
        double[][] b = new double[128][4];
        Box2[] ra;
        Box2[] rb;
        double[] c = new double[4];

        fill( rand, a );
        fill( rand, b );
        ra = toBox2( a );
        rb = toBox2( b );
        
        long ta = 0;
        long tb = 0;
        Box2 temp = new Box2();

        for( int trial = 0; trial < 10000; trial++ ) {
            long t0 = System.nanoTime();
            for( int i = 0; i < 1024 * 4; i++ ) {
                Box.clamp( ra[i % a.length], rb[i % b.length], temp );
            }
            
            ta += System.nanoTime() - t0;
            t0 = System.nanoTime();
            
            for( int i = 0; i < 1024 * 4; i++ ) {
                Box.clamp2( a[i % a.length], b[i % b.length], c );
            }
            
            tb += System.nanoTime() - t0;
        }
        
        System.out.println( "A : " + ( ta / 1000000000.0 ) );
        System.out.println( "B : " + ( tb / 1000000000.0 ) );
    }

    
    private static void fill( Random rand, double[][] out ) {
        for( double[] a: out ) {
            fill( rand, a );
        }
    }
    
    
    private static void fill( Random rand, double[] out ) {
        for( int i = 0; i < 4; i++ ) {
            out[i] = rand.nextDouble();
        }
        Box.fix2( out );
    }


    private static boolean equiv( Box2 r, double[] b ) {
        return Tests.isNear( r.x0, b[0] ) &&
               Tests.isNear( r.y0, b[1] ) &&
               Tests.isNear( r.x1, b[2] ) &&
               Tests.isNear( r.y1, b[3] );
    }
    
    
    private static Box2[] toBox2( double[][] a ) {
        Box2[] ret = new Box2[a.length];
        for( int i = 0; i < a.length; i++ ) {
            ret[i] = new Box2(
                    (float)a[i][0],
                    (float)a[i][1],
                    (float)a[i][2],
                    (float)a[i][3]
            );
        }
        return ret;
    }
    
}
