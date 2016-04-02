/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import org.junit.Test;

import java.util.Random;


/**
 * @author Philip DeCamp
 */
public class TestTrans3 {

    @Test
    public void testMatEquivalence() {
        Random rand = new Random( 10 );

        for( int i = 0; i < 5; i++ ) {
            Mat3 rot = Tests.randRotation( rand );
            Vec3 pos = Tests.randPos( rand );

            Trans3 trans = new Trans3();
            Mat.put( rot, trans.mRot );
            Vec.put( pos, trans.mPos );

            Mat4 mat = new Mat4();
            Mat.put( rot, mat );
            Mat.preTranslate( pos.x, pos.y, pos.z, mat, mat );

            Mat4 comp = new Mat4();
            Trans.transToMat( trans, comp );
            Tests.assertNear( comp, mat );

            Trans3 otherTrans = new Trans3();
            Trans.matToTrans( mat, otherTrans );
            Tests.assertNear( otherTrans.mRot, trans.mRot );
            Tests.assertNear( otherTrans.mPos, trans.mPos );

            Vec3 vt = Tests.randPos( rand );
            Vec3 va = new Vec3();
            Vec3 vb = new Vec3();

            Mat.mult( mat, vt, va );
            Trans.mult( trans, vt, vb );

            Tests.assertNear( va, vb );
        }
    }

    @Test
    public void testMultVec3() {
        Random rand = new Random( 12 );

        for( int i = 0; i < 5; i++ ) {
            Mat3 rot = Tests.randRotation( rand );
            Vec3 pos = Tests.randPos( rand );

            Trans3 trans = new Trans3();
            Mat.put( rot, trans.mRot );
            Vec.put( pos, trans.mPos );

            Mat4 mat = new Mat4();
            Trans.transToMat( trans, mat );

            Vec3 test = Tests.randPos( rand );
            Vec3 va   = new Vec3();
            Vec3 vb   = new Vec3();
            Trans.mult( trans, test, va );
            Mat.mult( mat, test, vb );

            Tests.assertNear( va, vb );
        }
    }

    @Test
    public void testMultVec4() {
        Random rand = new Random( 12 );

        for( int i = 0; i < 5; i++ ) {
            Mat3 rot = Tests.randRotation( rand );
            Vec3 pos = Tests.randPos( rand );

            Trans3 trans = new Trans3();
            Mat.put( rot, trans.mRot );
            Vec.put( pos, trans.mPos );

            Mat4 mat = new Mat4();
            Trans.transToMat( trans, mat );

            Vec4 test = new Vec4( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat() );
            Vec4 va   = new Vec4();
            Vec4 vb   = new Vec4();
            Trans.mult( trans, test, va );
            Mat.mult( mat, test, vb );

            Tests.assertNear( va, vb );

            Trans3 inv = new Trans3();
            Trans.invert( trans, inv );
            Vec4 vc = new Vec4();

            Trans3 thingy = new Trans3();
            Trans.mult( inv, trans, thingy );

            System.out.println( thingy );

            Tests.assertNear( test, vc );
        }
    }

    @Test
    public void testInvert() {
        Random rand   = new Random( 8 );
        Trans3 trans  = new Trans3();
        Trans3 inv    = new Trans3();
        Trans3 eyeTrans = new Trans3();
        Mat4   mat    = new Mat4();
        Mat4   invMat = new Mat4();
        Mat4   temp   = new Mat4();

        Mat.put( Tests.randRotation( rand ), trans.mRot );
        Vec.put( Tests.randPos( rand ), trans.mPos );

        Trans.invert( trans, inv );
        Trans.invert( inv, inv );
        Tests.assertNear( trans.mPos, inv.mPos );
        Tests.assertNear( trans.mRot, inv.mRot );

        Trans.invert( trans, inv );
        Trans.transToMat( trans, mat );
        Mat.invert( mat, invMat );
        Trans.transToMat( inv, temp );
        Tests.assertNear( invMat, temp );

        Trans.mult( trans, inv, eyeTrans );
        Trans3 eye = new Trans3();
        Trans.identity( eye );

        Tests.assertNear( eyeTrans.mPos, eye.mPos );
        Tests.assertNear( eyeTrans.mRot, eye.mRot );
    }

    @Test
    public void testOrthoInvert() {
        Random rand   = new Random( 8 );
        Trans3 trans  = new Trans3();
        Trans3 inv    = new Trans3();
        Trans3 eyeTrans = new Trans3();
        Mat4   mat    = new Mat4();
        Mat4   invMat = new Mat4();
        Mat4   temp   = new Mat4();

        Mat.put( Tests.randRotation( rand ), trans.mRot );
        Vec.put( Tests.randPos( rand ), trans.mPos );

        Trans.orthoInvert( trans, inv );
        Trans.orthoInvert( inv, inv );

        System.out.println( trans.mRot );
        System.out.println( inv.mRot );
        Tests.assertNear( trans.mPos, inv.mPos );
        Tests.assertNear( trans.mRot, inv.mRot );

        Trans.orthoInvert( trans, inv );
        Trans.transToMat( trans, mat );
        Mat.invert( mat, invMat );
        Trans.transToMat( inv, temp );
        Tests.assertNear( invMat, temp );

        Trans.mult( trans, inv, eyeTrans );
        Trans3 eye = new Trans3();
        Trans.identity( eye );

        Tests.assertNear( eyeTrans.mPos, eye.mPos );
        Tests.assertNear( eyeTrans.mRot, eye.mRot );
    }

}
