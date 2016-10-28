/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * @author Philip DeCamp
 */
public class Tests {

    public static boolean eq( float a, float b ) {
        if( Float.isNaN( a ) && Float.isNaN( b ) ) {
            return true;
        }

        if( Tol.approxEqual( a, b, 0.001f, 1E-6f ) ) {
            return true;
        }

        System.err.println( a + "  --  " + b );
        return false;
    }


    public static boolean eq( double a, double b ) {
        if( Double.isNaN( a ) && Double.isNaN( b ) ) {
            return true;
        }

        if( Tol.approxEqual( a, b, 0.001f, 1E-6f ) ) {
            return true;
        }

        System.err.println( a + "  --  " + b );
        return false;
    }


    public static void assertNear( float a, float b ) {
        assertTrue( eq( a, b ) );
    }


    public static void assertNear( double a, double b ) {
        assertTrue( eq( a, b ) );
    }


    public static void assertNear( Vec3 a, Vec3 b ) {
        assertTrue( eq( a.x, b.x ) &&
                    eq( a.y, b.y ) &&
                    eq( a.z, b.z ) );
    }


    public static void assertNear( Vec4 a, Vec4 b ) {
        assertTrue( eq( a.x, b.x ) &&
                    eq( a.y, b.y ) &&
                    eq( a.z, b.z ) &&
                    eq( a.w, b.w ) );
    }


    public static void assertNear( Mat3 a, Mat3 b ) {
        assertTrue( eq( a.m00, b.m00 ) &&
                    eq( a.m01, b.m01 ) &&
                    eq( a.m02, b.m02 ) &&
                    eq( a.m10, b.m10 ) &&
                    eq( a.m11, b.m11 ) &&
                    eq( a.m12, b.m12 ) &&
                    eq( a.m20, b.m20 ) &&
                    eq( a.m21, b.m21 ) &&
                    eq( a.m22, b.m22 ) );
    }


    public static void assertNear( Mat4 a, Mat4 b ) {
        assertTrue( eq( a.m00, b.m00 ) &&
                    eq( a.m01, b.m01 ) &&
                    eq( a.m02, b.m02 ) &&
                    eq( a.m03, b.m03 ) &&
                    eq( a.m10, b.m10 ) &&
                    eq( a.m11, b.m11 ) &&
                    eq( a.m12, b.m12 ) &&
                    eq( a.m13, b.m13 ) &&
                    eq( a.m20, b.m20 ) &&
                    eq( a.m21, b.m21 ) &&
                    eq( a.m22, b.m22 ) &&
                    eq( a.m23, b.m23 ) &&
                    eq( a.m30, b.m30 ) &&
                    eq( a.m31, b.m31 ) &&
                    eq( a.m32, b.m32 ) &&
                    eq( a.m33, b.m33 ) );
    }


    public static Mat3 randRotation( Random rand ) {
        Mat3 mat = new Mat3();
        Vec4 quat = new Vec4();
        Quat.sampleUniform( rand, quat );
        Quat.quatToMat( quat, mat );
        return mat;
    }


    public static Mat3 randMat3( Random rand, float scale ) {
        Mat3 mat = new Mat3( rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale,
                             rand.nextFloat() * scale - 0.5f * scale );
        return mat;
    }



    public static Vec3 randPos( Random rand ) {
        return new Vec3( rand.nextFloat() * 20 - 10,
                         rand.nextFloat() * 20 - 10,
                         rand.nextFloat() * 20 - 10 );
    }

}
