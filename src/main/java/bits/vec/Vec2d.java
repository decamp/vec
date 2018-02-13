/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.nio.ByteBuffer;
import java.util.NoSuchElementException;


/**
 * 2-dimensional Vector
 *
 * @author Philip DeCamp  
 */
public class Vec2d {

    public double x;
    public double y;


    public Vec2d() {}


    public Vec2d( double x, double y ) {
        this.x = x;
        this.y = y;
    }


    public Vec2d( Vec2d copy ) {
        x = copy.x;
        y = copy.y;
    }



    public double el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, double v ) {
        switch( d ) {
        case 0: x = v; break;
        case 1: y = v; break;
        default:
            throw new NoSuchElementException();
        }
    }


    public void set( double x, double y ) {
        this.x = x;
        this.y = y;
    }


    public void set( Vec2 copy ) {
        x = copy.x;
        y = copy.y;
    }


    public void set( Vec2d copy ) {
        x = copy.x;
        y = copy.y;
    }


    public void set( ByteBuffer bb ) {
        x = bb.getDouble();
        y = bb.getDouble();
    }




    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Vec2d) ) {
            return false;
        }
        Vec2d v = (Vec2d)obj;
        // "v == this" is needed to protect against NaNs.
        return v == this || x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits( x + y );
        return (int)( bits ^ ( bits >>> 32 ) );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT2, x, y );
    }

}
