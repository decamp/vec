/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Philip DeCamp
 */
public class Tests {

    public static boolean isNear( float a, float b ) {
        if( Float.isNaN( a ) && Float.isNaN( b ) ) {
            return true;
        }

        if( Tol.approxEqual( a, b, 0.001f, 1E-5f ) ) {
            return true;
        }

        System.err.println( a + "  --  " + b );
        return false;
    }


    public static boolean isNear( double a, double b ) {
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
        assertTrue( isNear( a, b ) );
    }


    public static void assertNear( double a, double b ) {
        assertTrue( isNear( a, b ) );
    }
    
    
    public static void assertNear( double[] a, double[] b ) {
        assertEquals( a.length, b.length );
        for( int i = 0; i < a.length; i++ ) {
            assertTrue( isNear( a[i], b[i] ) );   
        }
    }


    public static void assertNear( Vec3 a, Vec3 b ) {
        assertTrue( isNear( a.x, b.x ) &&
                    isNear( a.y, b.y ) &&
                    isNear( a.z, b.z ) );
    }


    public static void assertNear( Vec4 a, Vec4 b ) {
        assertTrue( isNear( a.x, b.x ) &&
                    isNear( a.y, b.y ) &&
                    isNear( a.z, b.z ) &&
                    isNear( a.w, b.w ) );
    }


    public static void assertNear( Mat3 a, Mat3 b ) {
        assertTrue( isNear( a.m00, b.m00 ) &&
                    isNear( a.m01, b.m01 ) &&
                    isNear( a.m02, b.m02 ) &&
                    isNear( a.m10, b.m10 ) &&
                    isNear( a.m11, b.m11 ) &&
                    isNear( a.m12, b.m12 ) &&
                    isNear( a.m20, b.m20 ) &&
                    isNear( a.m21, b.m21 ) &&
                    isNear( a.m22, b.m22 ) );
    }


    public static void assertNear( Mat4 a, Mat4 b ) {
        assertTrue( isNear( a.m00, b.m00 ) &&
                    isNear( a.m01, b.m01 ) &&
                    isNear( a.m02, b.m02 ) &&
                    isNear( a.m03, b.m03 ) &&
                    isNear( a.m10, b.m10 ) &&
                    isNear( a.m11, b.m11 ) &&
                    isNear( a.m12, b.m12 ) &&
                    isNear( a.m13, b.m13 ) &&
                    isNear( a.m20, b.m20 ) &&
                    isNear( a.m21, b.m21 ) &&
                    isNear( a.m22, b.m22 ) &&
                    isNear( a.m23, b.m23 ) &&
                    isNear( a.m30, b.m30 ) &&
                    isNear( a.m31, b.m31 ) &&
                    isNear( a.m32, b.m32 ) &&
                    isNear( a.m33, b.m33 ) );
    }


    public static Mat3 randRot3( Random rand ) {
        Mat3 mat = new Mat3();
        Vec4 quat = new Vec4();
        Quat.sampleUniform( rand, quat );
        Quat.quatToMat( quat, mat );
        return mat;
    }

    
    public static Mat4 randRot4( Random rand ) {
        Mat4 mat = new Mat4();
        Vec4 quat = new Vec4();
        Quat.sampleUniform( rand, quat );
        Quat.quatToMat( quat, mat );
        return mat;
    }

    
    public static Mat3 randMat3( Random rand, float scale ) {
        Mat3 mat = new Mat3( 
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale,
            rand.nextFloat() * scale - 0.5f * scale 
        );
        return mat;
    }


    public static Vec3 randPos( Random rand ) {
        return new Vec3( 
            rand.nextFloat() * 20 - 10,
            rand.nextFloat() * 20 - 10,
            rand.nextFloat() * 20 - 10
        );
    }

}
