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
public class Vec3 {

    public float x;
    public float y;
    public float z;


    public Vec3() {}


    public Vec3( float x, float y, float z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vec3( Vec3 copy ) {
        x = copy.x;
        y = copy.y;
        z = copy.z;
    }



    public float el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, float v ) {
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
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }


    public void set( Vec3 copy ) {
        x = copy.x;
        y = copy.y;
        z = copy.z;
    }


    public void set( Vec3d copy ) {
        x = (float)copy.x;
        y = (float)copy.y;
        z = (float)copy.z;
    }


    public void set( ByteBuffer bb ) {
        x = bb.getFloat();
        y = bb.getFloat();
        z = bb.getFloat();
    }



    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Vec3 ) ) {
            return false;
        }

        Vec3 v = (Vec3)obj;
        // v == this is necessary to catch NaNs.
        return v == this || x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits( x + y + z );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT3, x, y, z );
    }

}
