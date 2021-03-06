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
public class Vec2 {

    public float x;
    public float y;


    public Vec2() {}


    public Vec2( float x, float y ) {
        set( x, y );
    }


    public Vec2( Vec2 copy ) {
        set( copy );
    }



    public float el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, float v ) {
        switch( d ) {
        case 0: x = v; break;
        case 1: y = v; break;
        default:
            throw new NoSuchElementException();
        }
    }

    
    public void set( float x, float y ) {
        this.x = x;
        this.y = y;
    }


    public void set( double x, double y ) {
        this.x = (float)x;
        this.y = (float)y;
    }


    public void set( Vec2 copy ) {
        x = copy.x;
        y = copy.y;
    }

    
    public void set( Vec2d copy ) {
        x = (float)copy.x;
        y = (float)copy.y;
    }
    
    
    public void set( ByteBuffer bb ) {
        x = bb.getFloat();
        y = bb.getFloat();
    }
    
    

    @Override
    public boolean equals( Object obj ) {
        if( !( obj instanceof Vec2 ) ) {
            return false;
        }
        Vec2 v = (Vec2)obj;
        // "v == this" is needed to protect against NaNs.
        return v == this || x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits( x + y );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT2, x, y );
    }

}
