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
        Quat q = new Quat( 0, 0, 0, 1 );
        Mat3 mat = new Mat3();
        Quat.quatToMat( q, mat );
        
        Mat3 eye = new Mat3();
        Mat.identity( eye );
        assertNear( mat, eye );
        
        Quat.matToQuat( eye, q );
        assertNear( q, new Quat( 0, 0, 0, 1 ) );
    }

    @Test
    public void testIdentityToMat4() {
        Quat q = new Quat( 0, 0, 0, 1 );
        Mat4 mat = new Mat4();
        Quat.quatToMat( q, mat );

        Mat4 eye = new Mat4();
        Mat.identity( eye );
        assertNear( mat, eye );

        Quat.matToQuat( eye, q );
        assertNear( q, new Quat( 0, 0, 0, 1 ) );
    }

    @Test
    public void testMult() {
        Random rand = new Random( 8 );
        Quat qa = new Quat();
        Quat qb = new Quat();
        Mat3 ma = new Mat3();
        Mat3 mb = new Mat3();
        
        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), qa );
            Quat.randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), qb );
            Quat.quatToMat( qa, ma );
            Quat.quatToMat( qb, mb );
            
            Quat.mult( qa, qb, qb );
            Mat.mult( ma, mb, mb );
            Quat.quatToMat( qb, ma );
            assertNear( ma, mb );
        }
    }
    
    @Test
    public void testMultArr() {
        Random rand = new Random( 8 );
        double[] qa = new double[4];
        double[] qb = new double[4];
        double[] ma = new double[16];
        double[] mb = new double[16];
        
        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), qa );
            Quat.randToQuat( rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), qb );
            Quat.quatToMat4( qa, ma );
            Quat.quatToMat4( qb, mb );
            Quat.mult( qa, qb, qb );
            Mat.mult4( ma, mb, mb );
            Quat.quatToMat4( qb, ma );
            assertNear( ma, mb );
        }
    }

    @Test
    public void testMultVec() {
        Random rand = new Random( 8 );
        Quat q = new Quat();
        Mat3 m = new Mat3();
        Vec3 vq = new Vec3();
        Vec3 vm = new Vec3();
        
        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), q );
            Quat.quatToMat( q, m );
            Vec3 v = new Vec3( 
                10f * ( rand.nextFloat() - 0.5f ),
                10f * ( rand.nextFloat() - 0.5f ),
                10f * ( rand.nextFloat() - 0.5f )
            );
            
            Quat.multVec( q, v, vq );
            Mat.mult( m, v, vm );
            
            assertNear( vq, vm );
        }
    }

    @Test
    public void testMultVecArr() {
        Random rand = new Random( 8 );
        double[] q = new double[4];
        double[] m = new double[16];
        double[] vq = new double[3];
        double[] vm = new double[3];

        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), q );
            Quat.quatToMat4( q, m );
            double[] v = new double[] {
                10.0 * ( rand.nextDouble() - 0.5 ),
                10.0 * ( rand.nextDouble() - 0.5 ),
                10.0 * ( rand.nextDouble() - 0.5 )
            };

            Quat.multVec3( q, v, vq );
            Mat.mult4Vec3( m, v, vm );
            
            assertNear( vq, vm );
        }
    }
    
    @Test
    public void testPreRotate() {
        Random rand = new Random( 8 );
        Quat q = new Quat();
        Mat3 m = new Mat3();
        Mat3 result = new Mat3();

        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), q );
            Quat.quatToMat( q, m );
            
            float rads = (float)(2 * Math.PI) * rand.nextFloat();
            Vec3 axis = new Vec3( rand.nextFloat(), rand.nextFloat(), rand.nextFloat() );
            Vec.normalize( axis );
            
            Quat.preRotate( rads, axis.x, axis.y, axis.z, q, q );
            Mat.preRotate( rads, axis.x, axis.y, axis.z, m, m );
            
            Quat.quatToMat( q, result );
            assertNear( result, m );
        }
    }

    @Test
    public void testRotate() {
        Random rand = new Random( 8 );
        Quat q = new Quat();
        Mat3 m = new Mat3();
        Mat3 result = new Mat3();

        for( int i = 0; i < 6; i++ ) {
            Quat.randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), q );
            Quat.quatToMat( q, m );

            float rads = (float)(2 * Math.PI) * rand.nextFloat();
            Vec3 axis = new Vec3( rand.nextFloat(), rand.nextFloat(), rand.nextFloat() );
            Vec.normalize( axis );

            Quat.rotate( q, rads, axis.x, axis.y, axis.z, q );
            Mat.rotate( m, rads, axis.x, axis.y, axis.z, m );

            Quat.quatToMat( q, result );
            assertNear( result, m );
        }
    }
    
    @Test
    public void testGetRotationBetweenVecs() {
        Random rand = new Random( 9 );
        Quat q = new Quat();
        Vec3 result = new Vec3();
        
        for( int i = 0; i < 50; i++ ) {
            Vec3 a = Tests.randPos( rand );
            Vec3 b = Tests.randPos( rand );
            
            Vec.normalize( a );
            Vec.normalize( b );

            Quat.rotationBetweenUnitVecs( a, b, q );
            Quat.multVec( q, a, result );
                        
            assertNear( Vec.len( a ), Vec.len( result ) );
            Vec.normalize( b );
            Vec.normalize( result );
            assertNear( b, result );
        }

        for( int i = 0; i < 10; i++ ) {
            Vec3 a = Tests.randPos( rand );
            Vec3 b = new Vec3();
            Vec.mult( -1, a, b );
            Vec.normalize( a );
            Vec.normalize( b );
            
            Quat.rotationBetweenUnitVecs( a, b, q );
            Quat.multVec( q, a, result );

            assertNear( Vec.len( a ), Vec.len( result ) );
            Vec.normalize( b );
            Vec.normalize( result );
            assertNear( b, result );
        }
    }
    
    @Test
    public void testRandMatrixConversionsMat3() {
        Random rand = new Random( 6 );
        Quat q = new Quat();
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
        Quat q = new Quat();
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
    @Ignore
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
            if( !Tol.equal( a[i], b[i], 1E-8, 1E-8 ) ) {
                return false;
            }
        }
        
        return true;
    }

    
    
}
