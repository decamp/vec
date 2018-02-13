/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.nio.ByteBuffer;
import java.util.NoSuchElementException;


/**
 * 3-dimensional vector.
 *
 * @author Philip DeCamp
 */
public class Vec3d {

    public double x;
    public double y;
    public double z;


    public Vec3d() {}


    public Vec3d( double x, double y, double z ) {
        set( x, y, z );
    }


    public Vec3d( Vec3d copy ) {
        set( copy );
    }



    public double el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, double v ) {
        switch( d ) {
        case 0: x = v; return;
        case 1: y = v; return;
        case 2: z = v; return;
        default:
            throw new NoSuchElementException();
        }
    }

    
    public void set( float x, float y, float z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void set( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void set( Vec3 copy ) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }


    public void set( Vec3d copy ) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }


    public void set( ByteBuffer bb ) {
        x = bb.getDouble();
        y = bb.getDouble();
        z = bb.getDouble();
    }



    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Vec3d) ) {
            return false;
        }

        Vec3d v = (Vec3d)obj;
        // v == this is necessary to catch NaNs.
        return v == this || x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits( x + y );
        return (int)( bits ^ ( bits >>> 32 ) );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT3, x, y, z );
    }

}
