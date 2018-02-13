/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;


import java.util.NoSuchElementException;


/**
 * 3x3 Matrix
 *
 * @author Philip DeCamp
 */
public class Mat3 {

    public float m00, m01, m02;
    public float m10, m11, m12;
    public float m20, m21, m22;


    public Mat3() {}


    public Mat3( 
        float m00, float m10, float m20,
        float m01, float m11, float m21,
        float m02, float m12, float m22 
    ) {
        set( 
            m00,
            m10,
            m20,
            m01,
            m11,
            m21,
            m02,
            m12,
            m22
        );
    }


    public Mat3( Mat3 copy ) {
        set( copy );
    }

    

    public float el( int row, int col ) {
        switch( row ) {
        case 0:
            switch( col ) {
            case 0: return m00;
            case 1: return m01;
            case 2: return m02;
            }
            break;
        case 1:
            switch( col ) {
            case 0: return m10;
            case 1: return m11;
            case 2: return m12;
            }
            break;
        case 2:
            switch( col ) {
            case 0: return m20;
            case 1: return m21;
            case 2: return m22;
            }
            break;
        }
        throw new NoSuchElementException();
    }


    public void el( int row, int col, float v ) {
        switch( row ) {
        case 0:
            switch( col ) {
            case 0: m00 = v; return;
            case 1: m01 = v; return;
            case 2: m02 = v; return;
            }
            break;
        case 1:
            switch( col ) {
            case 0: m10 = v; return;
            case 1: m11 = v; return;
            case 2: m12 = v; return;
            }
            break;
        case 2:
            switch( col ) {
            case 0: m20 = v; return;
            case 1: m21 = v; return;
            case 2: m22 = v; return;
            }
            break;
        }
        throw new NoSuchElementException();
    }



    public void set( float v ) {
        m00 = v;
        m10 = v;
        m20 = v;
        m01 = v;
        m11 = v;
        m21 = v;
        m02 = v;
        m12 = v;
        m22 = v;
    }


    public void set(
        float m00, float m10, float m20,
        float m01, float m11, float m21,
        float m02, float m12, float m22
    ) {
        this.m00 = m00;
        this.m10 = m10;
        this.m20 = m20;
        this.m01 = m01;
        this.m11 = m11;
        this.m21 = m21;
        this.m02 = m02;
        this.m12 = m12;
        this.m22 = m22;
    }


    public void set( Mat3d copy ) {
        this.m00 = (float)copy.m00;
        this.m10 = (float)copy.m10;
        this.m20 = (float)copy.m20;
        this.m01 = (float)copy.m01;
        this.m11 = (float)copy.m11;
        this.m21 = (float)copy.m21;
        this.m02 = (float)copy.m02;
        this.m12 = (float)copy.m12;
        this.m22 = (float)copy.m22;
    }


    public void set( Mat3 copy ) {
        this.m00 = copy.m00;
        this.m10 = copy.m10;
        this.m20 = copy.m20;
        this.m01 = copy.m01;
        this.m11 = copy.m11;
        this.m21 = copy.m21;
        this.m02 = copy.m02;
        this.m12 = copy.m12;
        this.m22 = copy.m22;
    }

    
    

    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Mat3 ) ) {
            return false;
        }

        Mat3 v = (Mat3)obj;
        // v == this is necessary to catch NaNs.
        return v == this ||
               m00 == v.m00 &&
               m01 == v.m01 &&
               m02 == v.m02 &&
               m10 == v.m10 &&
               m11 == v.m11 &&
               m12 == v.m12 &&
               m20 == v.m20 &&
               m21 == v.m21 &&
               m22 == v.m22;
    }

    @Override
    public int hashCode() {
        int hash = Float.floatToIntBits( m00 + m11 + m22 );
        hash ^= Float.floatToIntBits(    m01 + m12 + m20 );
        hash ^= Float.floatToIntBits(    m02 + m10 + m21 );
        return hash;
    }

    @Override
    public String toString() {
        return Mat.format( this );
    }

}
