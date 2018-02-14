/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;


import java.nio.ByteBuffer;
import java.util.NoSuchElementException;


/**
 * 4x4 Matrix
 *
 * @author Philip DeCamp
 */
public class Mat4d {

    public double m00, m01, m02, m03;
    public double m10, m11, m12, m13;
    public double m20, m21, m22, m23;
    public double m30, m31, m32, m33;

    public Mat4d() {}


    public Mat4d( 
        double m00, double m10, double m20, double m30,
        double m01, double m11, double m21, double m31,
        double m02, double m12, double m22, double m32,
        double m03, double m13, double m23, double m33 
    ) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }


    public Mat4d( Mat4d copy ) {
        set( copy );
    }




    public double el( int row, int col ) {
        switch( row ) {
        case 0:
            switch( col ) {
            case 0: return m00;
            case 1: return m01;
            case 2: return m02;
            case 3: return m03;
            }
            break;
        case 1:
            switch( col ) {
            case 0: return m10;
            case 1: return m11;
            case 2: return m12;
            case 3: return m13;
            }
            break;
        case 2:
            switch( col ) {
            case 0: return m20;
            case 1: return m21;
            case 2: return m22;
            case 3: return m23;
            }
            break;
        case 3:
            switch( col ) {
            case 0: return m30;
            case 1: return m31;
            case 2: return m32;
            case 3: return m33;
            }
            break;
        }
        throw new NoSuchElementException();
    }


    public void el( int row, int col, double v ) {
        switch( row ) {
        case 0:
            switch( col ) {
            case 0: m00 = v; return;
            case 1: m01 = v; return;
            case 2: m02 = v; return;
            case 3: m03 = v; return;
            }
            break;
        case 1:
            switch( col ) {
            case 0: m10 = v; return;
            case 1: m11 = v; return;
            case 2: m12 = v; return;
            case 3: m13 = v; return;
            }
            break;
        case 2:
            switch( col ) {
            case 0: m20 = v; return;
            case 1: m21 = v; return;
            case 2: m22 = v; return;
            case 3: m23 = v; return;
            }
            break;
        case 3:
            switch( col ) {
            case 0: m30 = v; return;
            case 1: m31 = v; return;
            case 2: m32 = v; return;
            case 3: m33 = v; return;
            }
            break;
        }
        throw new NoSuchElementException();
    }



    public void set( double v ) {
        this.m00 = v;
        this.m01 = v;
        this.m02 = v;
        this.m03 = v;
        this.m10 = v;
        this.m11 = v;
        this.m12 = v;
        this.m13 = v;
        this.m20 = v;
        this.m21 = v;
        this.m22 = v;
        this.m23 = v;
        this.m30 = v;
        this.m31 = v;
        this.m32 = v;
        this.m33 = v;
    }


    public void set(
        double m00, double m10, double m20, double m30,
        double m01, double m11, double m21, double m31,
        double m02, double m12, double m22, double m32,
        double m03, double m13, double m23, double m33
    ) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    
    public void set( Mat3 copy ) {
        m00 = copy.m00;
        m01 = copy.m01;
        m02 = copy.m02;
        m03 = 0;
        m10 = copy.m10;
        m11 = copy.m11;
        m12 = copy.m12;
        m13 = 0;
        m20 = copy.m20;
        m21 = copy.m21;
        m22 = copy.m22;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;
    }


    public void set( Mat3d copy ) {
        m00 = copy.m00;
        m01 = copy.m01;
        m02 = copy.m02;
        m03 = 0;
        m10 = copy.m10;
        m11 = copy.m11;
        m12 = copy.m12;
        m13 = 0;
        m20 = copy.m20;
        m21 = copy.m21;
        m22 = copy.m22;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;
    }


    public void set( Mat4 copy ) {
        m00 = copy.m00;
        m01 = copy.m01;
        m02 = copy.m02;
        m03 = copy.m03;
        m10 = copy.m10;
        m11 = copy.m11;
        m12 = copy.m12;
        m13 = copy.m13;
        m20 = copy.m20;
        m21 = copy.m21;
        m22 = copy.m22;
        m23 = copy.m23;
        m30 = copy.m30;
        m31 = copy.m31;
        m32 = copy.m32;
        m33 = copy.m33;
    }


    public void set( Mat4d copy ) {
        m00 = copy.m00;
        m01 = copy.m01;
        m02 = copy.m02;
        m03 = copy.m03;
        m10 = copy.m10;
        m11 = copy.m11;
        m12 = copy.m12;
        m13 = copy.m13;
        m20 = copy.m20;
        m21 = copy.m21;
        m22 = copy.m22;
        m23 = copy.m23;
        m30 = copy.m30;
        m31 = copy.m31;
        m32 = copy.m32;
        m33 = copy.m33;
    }


    public void set( ByteBuffer bb ) {
        m00 = bb.getDouble();
        m10 = bb.getDouble();
        m20 = bb.getDouble();
        m30 = bb.getDouble();
        m01 = bb.getDouble();
        m11 = bb.getDouble();
        m21 = bb.getDouble();
        m31 = bb.getDouble();
        m02 = bb.getDouble();
        m12 = bb.getDouble();
        m22 = bb.getDouble();
        m32 = bb.getDouble();
        m03 = bb.getDouble();
        m13 = bb.getDouble();
        m23 = bb.getDouble();
        m33 = bb.getDouble();
    }
    


    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Mat4d) ) {
            return false;
        }

        Mat4d v = (Mat4d)obj;
        // v == this is necessary to catch NaNs.
        return v == this ||
               m00 == v.m00 &&
               m01 == v.m01 &&
               m02 == v.m02 &&
               m03 == v.m03 &&
               m10 == v.m10 &&
               m11 == v.m11 &&
               m12 == v.m12 &&
               m13 == v.m13 &&
               m20 == v.m20 &&
               m21 == v.m21 &&
               m22 == v.m22 &&
               m23 == v.m23 &&
               m30 == v.m30 &&
               m31 == v.m31 &&
               m32 == v.m32 &&
               m33 == v.m33;
    }

    @Override
    public int hashCode() {
        long hash = Double.doubleToLongBits( m00 + m11 + m22 + m33 );
        hash ^= Double.doubleToLongBits( m01 + m12 + m23 + m30 );
        hash ^= Double.doubleToLongBits( m02 + m13 + m20 + m31 );
        hash ^= Double.doubleToLongBits( m03 + m10 + m21 + m32 );
        return (int)( hash ^ ( hash >>> 32 ) );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "[" );
        sb.append( String.format( Vec.FORMAT4, m00, m01, m02, m03 ) ).append( '\n' );
        sb.append( String.format( Vec.FORMAT4, m10, m11, m12, m13 ) ).append( '\n' );
        sb.append( String.format( Vec.FORMAT4, m20, m21, m22, m23 ) ).append( '\n' );
        sb.append( String.format( Vec.FORMAT4, m30, m31, m32, m33 ) );
        sb.append( "]");
        return sb.toString();
    }

}
