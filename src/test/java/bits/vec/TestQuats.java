/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.*;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static bits.vec.Tests.*;

public class TestQuats {

    @Test
    public void testIdentityToMat3() {
        Vec4 q = new Vec4( 0, 0, 0, 1 );
        Mat3 mat = new Mat3();
        Quat.quatToMat( q, mat );
        
        Mat3 eye = new Mat3();
        Mat.identity( eye );
        assertNear( mat, eye );
        
        Quat.matToQuat( eye, q );
        assertNear( q, new Vec4( 0, 0, 0, 1 ) );
    }

    @Test
    public void testIdentityToMat4() {
        Vec4 q = new Vec4( 0, 0, 0, 1 );
        Mat4 mat = new Mat4();
        Quat.quatToMat( q, mat );

        Mat4 eye = new Mat4();
        Mat.identity( eye );
        assertNear( mat, eye );

        Quat.matToQuat( eye, q );
        assertNear( q, new Vec4( 0, 0, 0, 1 ) );
    }
    
    @Test
    public void testRandMatrixConversionsMat3() {
        Random rand = new Random( 6 );
        Vec4 q = new Vec4();
        Mat3 rotOut = new Mat3();

        for( int i = 0; i < 100; i++ ) {
            Mat3 rotIn = Tests.randRot3( rand );
            Quat.matToQuat( rotIn, q );
            Quat.quatToMat( q, rotOut );
            assertNear( rotIn, rotOut );
        }
    }

    @Test
    public void testRandMatrixConversionsMat4() {
        Random rand = new Random( 6 );
        Vec4 q = new Vec4();
        Mat4 rotOut = new Mat4();

        for( int i = 0; i < 100; i++ ) {
            Mat4 rotIn = Tests.randRot4( rand );
            Quat.matToQuat( rotIn, q );
            Quat.quatToMat( q, rotOut );
            assertNear( rotIn, rotOut );
        }
    }

    @Test
    public void testRandMatrixConversionsArr() {
        Random rand = new Random( 6 );
        double[] rotIn = new double[16];
        double[] q = new double[4];
        double[] rotOut = new double[16];
        
        for( int i = 0; i < 100; i++ ) {
            rotXyz( 
                rand.nextDouble() * Math.PI,
                rand.nextDouble() * Math.PI,
                rand.nextDouble() * Math.PI,
                rotIn 
            );
            
            Quat.mat4ToQuat( rotIn, q );
            Quat.quatToMat4( q, rotOut );
            assertNear( rotIn, rotOut );
        }
    }

    @Test
    public void testOrthoMatrixConversions() {
        double[] rotIn  = new double[16];
        double[] q      = new double[4];
        double[] rotOut = new double[16];
        
        for( int i = 0; i < 4*4*4; i++ ) {
            double rx = ( i      % 4 ) * Math.PI * 0.5;
            double ry = ( i /  4 % 4 ) * Math.PI * 0.5;
            double rz = ( i / 16 % 4 ) * Math.PI * 0.5;
                        
            rotXyz( rx, ry, rz, rotIn );
            Quat.mat4ToQuat( rotIn, q );
            Quat.quatToMat4( q, rotOut );
            assertNear( rotIn, rotOut );
        }
    }

    @Ignore
    @Test
    public void testMultSpeed() {
        double[] a  = new double[4];
        double[] b  = new double[4];
        double[] c  = new double[4];
        Random rand = new Random( 100 );
        
        long t0 = 0;
        long t1 = 0;
        
        for( int i = 0; i < 100000; i++ ) {
            uniformRandQuat( rand, a );
            uniformRandQuat( rand, b );
            
            long start = System.nanoTime();
            
            for( int j = 0; j < 200; j++ ) {
                Quat.mult( a, b, c );
            }
            
            t0 += System.nanoTime() - start;
            start = System.nanoTime();
            
            for( int j = 0; j < 200; j++) {
                Quat.mult( a, b, c );
            }
            
            t1 += System.nanoTime() - start;
        }
        
        System.out.println( "Time0: " + ( t0 / 1000000000.0 ) );
        System.out.println( "Time1: " + ( t1 / 1000000000.0 ) );
    }

    /**
     * Make sure quaternion sampling is actually uniform.
     */
    @Test
    public void testSamplingUniformity() {
        double[] startVec = { 1, 0, 0 };
        double[] outVec   = new double[3];
        double[] quat     = new double[4];
        double[][] testVecs = { 
            {  1,  0,  0 },
            { -1,  0,  0 },
            {  0,  1,  0 },
            {  0, -1,  0 },
            {  0,  0,  1 },
            {  0,  0, -1 } 
        };
        
        float[] testAngs = new float[testVecs.length];

        int trials = 100000;
        Random rand = new Random( 20 );

        for( int i = 0; i < trials; i++ ) {
            Quat.sampleUniform( rand, quat );
            Quat.multVec3( quat, startVec, outVec );

            for( int j = 0; j < testVecs.length; j++ ) {
                testAngs[j] += Vec.ang3( outVec, testVecs[j] );
            }
        }

        for( int j = 0; j < testVecs.length; j++ ) {
            double err = Math.abs( testAngs[j] / trials - (float)( 0.5 * Math.PI ) );
            assertTrue( "Quaternion sampling not uniform.",  err < 0.01 * Math.PI );

            System.out.println( Vec.format3( testVecs[j] ) + " : " + err );
        }
    }

    /**
     * I understand you can uniformly sample a sphere with gaussians samples!
     * Yes. It is true.
     */
    @Ignore @Test
    public void testSphericalSamplingWithGaussians() {
        double[] outVec   = new double[3];
        double[][] testVecs = { 
            {  1,  0,  0 },
            { -1,  0,  0 },
            {  0,  1,  0 },
            {  0, -1,  0 },
            {  0,  0,  1 },
            {  0,  0, -1 } 
        };
        float[] testAngs = new float[testVecs.length];

        int trials = 100000;
        Random rand = new Random( 20 );

        for( int i = 0; i < trials; i++ ) {
            double r;
            r = rand.nextDouble();
            double x = Phi.ncdfInv( r );
            r = rand.nextDouble();
            double y = Phi.ncdfInv( r );
            r = rand.nextDouble();
            double z = Phi.ncdfInv( r );
            r = 1.0 / Math.sqrt( x * x + y * y + z * z );
            outVec[0] = (float)( r * x );
            outVec[1] = (float)( r * y );
            outVec[2] = (float)( r * z );

            for( int j = 0; j < testVecs.length; j++ ) {
                testAngs[j] += Vec.ang3( outVec, testVecs[j] );
            }
        }

        for( int j = 0; j < testVecs.length; j++ ) {
            double err = Math.abs( testAngs[j] / trials ) - 0.5 * Math.PI;
            assertTrue( "Gaussian sampling not uniform.",  err < 0.01 * Math.PI );
            //System.out.println( Vec3.format( testVecs[j] ) + " : " + err );
        }
    }
    

    private static void uniformRandQuat( Random rand, double[] out ) {
        // Draw three uniform samples.
        double u0 = rand.nextDouble();
        double u1 = rand.nextDouble();
        double u2 = rand.nextDouble();
        Quat.randToQuat( u0, u1, u2, out );
    }
    
    
    private static void rotXyz( double rx, double ry, double rz, double[] out ) {
        double[] a = new double[16];
        double[] b = new double[16];
        Mat.getRotate4( rx, 1, 0, 0, a );
        Mat.getRotate4( ry, 0, 1, 0, out );
        Mat.mult4( out, a, b );
        Mat.getRotate4( rz, 0, 0, 1, a );
        Mat.mult4( a, b, out );
    }
    
    
    private static boolean matEquals( double[] a, double[] b ) {
        for( int i = 0; i < 16; i++ ) {
            if( !Tol.approxEqual( a[i], b[i], 1E-8, 1E-8 ) ) {
                return false;
            }
        }
        
        return true;
    }

}
