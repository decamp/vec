/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.util.NoSuchElementException;


/**
 * Methods for quaternions.
 *
 * @author decamp
 */
public final class Quatd {

    public double x;
    public double y;
    public double z;
    public double w;

    
    public Quatd() {}
        

    public void set( double x, double y, double z, double w ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    public void set( Quat copy ) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
        this.w = copy.w;
    }
    

    public void set( Quatd copy ) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
        this.w = copy.w;
    }
    


    public double el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        case 3: return w;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, double v ) {
        switch( d ) {
        case 0: x = v; return;
        case 1: y = v; return;
        case 2: z = v; return;
        case 3: w = v; return;
        default:
            throw new NoSuchElementException();
        }
    }


    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Quatd) ) {
            return false;
        }

        Quatd v = (Quatd)obj;
        // v == this is necessary to catch NaNs.
        return v == this || x == v.x && y == v.y && z == v.z && w == v.w;
    }

    @Override
    public int hashCode() {
        long hash = Double.doubleToLongBits( x + y + z + w );
        return (int)( hash ^ ( hash >>> 32 ) );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT4, x, y, z, w );
    }    

}
