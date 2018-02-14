/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;


import java.nio.*;
import java.util.NoSuchElementException;


/**
 * 3x3 Matrix
 *
 * @author Philip DeCamp
 */
public class Mat3d {

    public double m00, m01, m02;
    public double m10, m11, m12;
    public double m20, m21, m22;


    public Mat3d() {}


    
    public double el( int row, int col ) {
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


    public void el( int row, int col, double v ) {
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

    
    
    public void get( ByteBuffer db ) {
        db.putDouble( m00 );
        db.putDouble( m10 );
        db.putDouble( m20 );
        db.putDouble( m01 );
        db.putDouble( m11 );
        db.putDouble( m21 );
        db.putDouble( m02 );
        db.putDouble( m12 );
        db.putDouble( m22 );
    }
    
    
    public void get( double[] arr, int off ) {
        arr[off + 0] = m00;
        arr[off + 1] = m10;
        arr[off + 2] = m20;
        arr[off + 3] = m01;
        arr[off + 4] = m11;
        arr[off + 5] = m21;
        arr[off + 6] = m02;
        arr[off + 7] = m12;
        arr[off + 8] = m22;   
    }
    
    
    
    public void set( double v ) {
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
        double m00, double m10, double m20,
        double m01, double m11, double m21,
        double m02, double m12, double m22
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


    public void set( Mat3d copy ) {
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


    public void set( Mat4 copy ) {
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


    public void set( Mat4d copy ) {
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


    public void set( ByteBuffer bb ) {
        this.m00 = bb.getDouble();
        this.m10 = bb.getDouble();
        this.m20 = bb.getDouble();
        this.m01 = bb.getDouble();
        this.m11 = bb.getDouble();
        this.m21 = bb.getDouble();
        this.m02 = bb.getDouble();
        this.m12 = bb.getDouble();
        this.m22 = bb.getDouble();
    }
    
    
    public void set( double[] arr, int off ) {
        m00 = arr[off + 0];
        m10 = arr[off + 1];
        m20 = arr[off + 2];
        m01 = arr[off + 3];
        m11 = arr[off + 4];
        m21 = arr[off + 5];
        m02 = arr[off + 6];
        m12 = arr[off + 7];
        m22 = arr[off + 8];   
    }
    
    


    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Mat3d) ) {
            return false;
        }

        Mat3d v = (Mat3d)obj;
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
        long hash = Double.doubleToLongBits( m00 + m11 + m22 );
        hash ^= Double.doubleToLongBits(    m01 + m12 + m20 );
        hash ^= Double.doubleToLongBits(    m02 + m10 + m21 );
        return (int)( hash ^ ( hash >>> 32 ) );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "[" );
        sb.append( String.format( Vec.FORMAT3, m00, m01, m02 ) ).append( '\n' );
        sb.append( String.format( Vec.FORMAT3, m10, m11, m12 ) ).append( '\n' );
        sb.append( String.format( Vec.FORMAT3, m20, m21, m22 ) );
        sb.append( "]");
        return sb.toString();
    }

}
