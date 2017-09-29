/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * @author Philip DeCamp
 */
public class TestMat3 {

    @Test
    public void testNormalizeRotation() {
        Random rand = new Random( 6 );

        for( int i = 0; i < 10; i++ ) {
            Mat3 mat = Tests.randMat3( rand, 3f );
            Mat.normalizeRotationMatrix( mat );
            Mat3 inv = new Mat3();
            Mat.transpose( mat, inv );
            Mat3 eye = new Mat3();
            Mat.mult( mat, inv, eye );
            Mat3 comp = new Mat3();
            Mat.identity( comp );

            Tests.assertNear( eye, comp );
        }
    }


    @Test
    public void testMultDifferentSizedMats() {
        Mat3 left3  = new Mat3();
        Mat4 left4  = new Mat4();
        Mat3 right3 = new Mat3();
        Mat4 right4 = new Mat4();

        Mat3 tmp  = new Mat3();
        Mat4 ans0 = new Mat4();
        Mat4 ans1 = new Mat4();
        Mat4 ans2 = new Mat4();

        Random rand = new Random();

        for( int i = 0; i < 100; i++ ) {
            fillRandom( rand, left3 );
            fillRandom( rand, right3 );
            Mat.put( left3, left4 );
            Mat.put( right3, right4 );

            Mat.mult( left3, right3, tmp );
            Mat.put( tmp, ans0 );
            Mat.mult( left3, right4, ans1 );
            Mat.mult( left4, right3, ans2 );

            assertNear( ans0, ans1 );
            assertNear( ans0, ans2 );
        }
    }


    static void fillRandom( Random rand, Mat3 out ) {
        out.m00 = rand.nextFloat() * 10f - 5f;
        out.m10 = rand.nextFloat() * 10f - 5f;
        out.m20 = rand.nextFloat() * 10f - 5f;
        out.m01 = rand.nextFloat() * 10f - 5f;
        out.m11 = rand.nextFloat() * 10f - 5f;
        out.m21 = rand.nextFloat() * 10f - 5f;
        out.m02 = rand.nextFloat() * 10f - 5f;
        out.m12 = rand.nextFloat() * 10f - 5f;
        out.m22 = rand.nextFloat() * 10f - 5f;
    }


    static void assertNear( Mat4 a, Mat4 b ) {
        assertNear( a.m00, b.m00 );
        assertNear( a.m01, b.m01 );
        assertNear( a.m02, b.m02 );
        assertNear( a.m03, b.m03 );
        assertNear( a.m10, b.m10 );
        assertNear( a.m11, b.m11 );
        assertNear( a.m12, b.m12 );
        assertNear( a.m13, b.m13 );
        assertNear( a.m20, b.m20 );
        assertNear( a.m21, b.m21 );
        assertNear( a.m22, b.m22 );
        assertNear( a.m23, b.m23 );
        assertNear( a.m30, b.m30 );
        assertNear( a.m31, b.m31 );
        assertNear( a.m32, b.m32 );
        assertNear( a.m33, b.m33 );
    }


    static void assertNear( float a, float b ) {
        assertTrue( Tol.equal( a, b ) );
    }

}

