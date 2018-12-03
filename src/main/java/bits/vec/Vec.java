/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static bits.vec.Tol.*;


/**
 * Functions for length-3 vectors.
 *
 * @author Philip DeCamp
 */
public final class Vec {

    public static final String FORMAT_NUM = "% 7.4f";
    public static final String FORMAT2 = "[ % 7.4f, % 7.4f ]";
    public static final String FORMAT3 = "[ % 7.4f, % 7.4f, % 7.4f ]";
    public static final String FORMAT4 = "[ % 7.4f, % 7.4f, % 7.4f, % 7.4f ]";

    
    //== VEC2 Functions ===========================

    
    public static void put( Vec2 v, Vec2 out ) {
        out.x = v.x;
        out.y = v.y;
    }


    public static void put( Vec2 v, ByteBuffer out ) {
        out.putFloat( v.x );
        out.putFloat( v.y );
    }


    public static void put( Vec2 v, FloatBuffer out ) {
        out.put( v.x );
        out.put( v.y );
    }


    public static void put( ByteBuffer b, Vec2 v ) {
        v.x = b.getFloat();
        v.y = b.getFloat();
    }


    public static void put( FloatBuffer b, Vec2 v ) {
        v.x = b.get();
        v.y = b.get();
    }


    public static void add( Vec2 a, Vec2 b, Vec2 out ) {
        out.x = a.x + b.x;
        out.y = a.y + b.y;
    }


    public static void add( float dx, float dy, Vec2 a, Vec2 out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
    }


    public static void addTo( Vec2 a, Vec2 out ) {
        out.x += a.x;
        out.y += a.y;
    }


    public static void subtract( Vec2 a, Vec2 b, Vec2 out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
    }


    public static void subtractFrom( Vec2 a, Vec2 out ) {
        out.x -= a.x;
        out.y -= a.y;
    }


    public static void mult( float sa, Vec2 a ) {
        a.x *= sa;
        a.y *= sa;
    }


    public static void mult( float sa, Vec2 a, Vec2 out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
    }


    public static void mult( float sx, float sy, Vec2 a, Vec2 out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
    }


    public static void multAdd( float sa, Vec2 a, float sb, Vec2 b, Vec2 out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
    }


    public static void multAddTo( float sa, Vec2 a, float sOut, Vec2 out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
    }


    public static void min( Vec2 a, Vec2 b, Vec2 out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
    }


    public static void max( Vec2 a, Vec2 b, Vec2 out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
    }


    public static float len( Vec2 a ) {
        return (float)Math.sqrt( lenSquared( a ) );
    }


    public static float lenSquared( Vec2 a ) {
        return a.x * a.x + a.y * a.y;
    }


    public static float dist( Vec2 a, Vec2 b ) {
        return (float)Math.sqrt( distSquared( a, b ) );
    }


    public static float distSquared( Vec2 a, Vec2 b ) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return dx * dx + dy * dy;
    }


    public static boolean near( Vec2 a, Vec2 b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y );
    }


    public static boolean near( Vec2 a, Vec2 b, float relErr, float absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr );
    }


    public static void normalize( Vec2 a ) {
        float s = 1f / len( a );
        a.x *= s;
        a.y *= s;
    }


    public static void normalize( Vec2 a, float normLength, Vec2 out ) {
        float d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
    }


    public static float dot( Vec2 a, Vec2 b ) {
        return a.x * b.x + a.y * b.y;
    }


    public static float dot( Vec2 origin, Vec2 a, Vec2 b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y);
    }


    public static float cosAng( Vec2 a, Vec2 b ) {
        return dot( a, b ) / (float)Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static float cosAng( Vec2 origin, Vec2 a, Vec2 b ) {
        float ax = a.x - origin.x;
        float ay = a.y - origin.y;
        float bx = b.x - origin.x;
        float by = b.y - origin.y;

        float dd = ( ax*ax + ay*ay ) * ( bx*bx + by*by );
        return ( ax*bx + ay*by ) / (float)Math.sqrt( dd );
    }


    public static float ang( Vec2 a, Vec2 b ) {
        return (float)Math.acos( cosAng( a, b ) );
    }


    public static float ang( Vec2 origin, Vec2 a, Vec2 b ) {
        return (float)Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec2 a, Vec2 b, float p, Vec2 out ) {
        float q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void reject( Vec2 a, Vec2 ref ) {
        float lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void project( Vec2 a, Vec2 ref ) {
        float lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    public static float linePointDistance( Vec2 n1, Vec2 n2, Vec2 p ) {
        float c = (p.x - n1.x) * (p.y - n2.y) - (p.x - n2.x) * (p.y - n1.y);
        return c / dist( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param s1 Start of line segment
     * @param s2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    public static float segmentPointDistance( Vec2 s1, Vec2 s2, Vec2 p ) {
        float u0 =  p.x - s1.x;
        float u1 =  p.y - s1.y;
        float v0 = s2.x - s1.x;
        float v1 = s2.y - s1.y;

        float num = u0 * v0 + u1 * v1;
        float den = v0 * v0 + v1 * v1;
        float t   = num / den;

        if( den < FSQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in <u0,u1>).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p.x - s2.x;
            u1 = p.y - s2.y;
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
        }

        return (float)Math.sqrt( u0 * u0 + u1 * u1 );
    }

    /**
     * Finds intersection between two lines.
     *
     * @param a0  First point on line a
     * @param a1  Second point on line a
     * @param b0  First point on line b
     * @param b1  Second point on line b
     * @param out On return, holds point of intersection if lines are not parallel.
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    public static boolean lineIntersection( Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1, Vec2 out ) {
        float bx = a1.x - a0.x;
        float by = a1.y - a0.y;
        float dx = b1.x - b0.x;
        float dy = b1.y - b0.y;
        float dotPerp = bx * dy - by * dx;
        if( dotPerp < FSQRT_ABS_TOL && -dotPerp < FSQRT_ABS_TOL ) {
            return false;
        }
        float cx = b0.x - a0.x;
        float cy = b0.y - a0.y;
        float t = (cx * dy - cy * dx) / dotPerp;

        out.x = a0.x + t * bx;
        out.y = a0.y + t * by;
        return true;
    }

    /**
     * Finds intersection between two line segments.
     *
     * @param a0  First point on segment a
     * @param a1  Second point on segment a
     * @param b0  First point on segment b
     * @param b1  Second point on segment b
     * @param out On return, holds point of intersection if exists.
     * @return true if lines intersect at single place.
     */
    public static boolean segmentIntersection( Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1, Vec2 out ) {
        float bx = a1.x - a0.x;
        float by = a1.y - a0.y;
        float dx = b1.x - b0.x;
        float dy = b1.y - b0.y;
        float dotPerp = bx * dy - by * dx;
        if( dotPerp < FSQRT_ABS_TOL && -dotPerp < FSQRT_ABS_TOL ) {
            return false;
        }

        float cx = b0.x - a0.x;
        float cy = b0.y - a0.y;
        float t = (cx * dy - cy * dx) / dotPerp;
        if( t < 0 || t > 1 ) {
            return false;
        }

        float u = (cx * by - cy * bx) / dotPerp;
        if( u < 0 || u > 1 ) {
            return false;
        }

        out.x = a0.x + t * bx;
        out.y = a0.y + t * by;
        return true;
    }


    public static boolean isNaN( Vec2 vec ) {
        return vec.x != vec.x || vec.y != vec.y;
    }


    public static String format( Vec2 vec ) {
        return String.format( FORMAT2, vec.x, vec.y );
    }



    //== VEC3 Functions =======================================================


    public static void put( float x, float y, float z, Vec3 out ) {
        out.x = x;
        out.y = y;
        out.z = z;
    }


    public static void put( Vec3 v, Vec3 out ) {
        out.x = v.x;
        out.y = v.y;
        out.z = v.z;
    }


    public static void put( Vec3 v, ByteBuffer out ) {
        out.putFloat( v.x );
        out.putFloat( v.y );
        out.putFloat( v.z );
    }


    public static void put( ByteBuffer b, Vec3 v ) {
        v.x = b.getFloat();
        v.y = b.getFloat();
        v.z = b.getFloat();
    }


    public static void put( Vec3 v, FloatBuffer out ) {
        out.put( v.x );
        out.put( v.y );
        out.put( v.z );
    }


    public static void put( FloatBuffer b, Vec3 v ) {
        v.x = b.get();
        v.y = b.get();
        v.z = b.get();
    }


    public static void put( Vec3 v, float[] out ) {
        out[0] = v.x;
        out[1] = v.y;
        out[2] = v.z;
    }


    public static void put( float[] v, Vec3 out ) {
        out.x = v[0];
        out.y = v[1];
        out.z = v[2];
    }


    public static void add( Vec3 a, Vec3 b, Vec3 out ) {
        out.x = a.x + b.x;
        out.y = a.y + b.y;
        out.z = a.z + b.z;
    }


    public static void add( float dx, float dy, float dz, Vec3 a, Vec3 out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
        out.z = a.z + dz;
    }


    public static void addTo( Vec3 a, Vec3 out ) {
        out.x += a.x;
        out.y += a.y;
        out.z += a.z;
    }


    public static void addTo( float dx, float dy, float dz, Vec3 out ) {
        out.x += dx;
        out.y += dy;
        out.z += dz;
    }


    public static void subtract( Vec3 a, Vec3 b, Vec3 out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
        out.z = a.z - b.z;
    }


    public static void subtractFrom( Vec3 a, Vec3 out ) {
        out.x -= a.x;
        out.y -= a.y;
        out.z -= a.z;
    }


    public static void mult( float sa, Vec3 a ) {
        a.x *= sa;
        a.y *= sa;
        a.z *= sa;
    }


    public static void mult( float sa, Vec3 a, Vec3 out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
        out.z = sa * a.z;
    }


    public static void mult( float sx, float sy, float sz, Vec3 a, Vec3 out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
        out.z = sz * a.z;
    }


    public static void multAdd( float sa, Vec3 a, float sb, Vec3 b, Vec3 out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
        out.z = a.z * sa + b.z * sb;
    }


    public static void multAddTo( float sa, Vec3 a, float sOut, Vec3 out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
        out.z = sOut * out.z + sa * a.z;
    }


    public static void min( Vec3 a, Vec3 b, Vec3 out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
        out.z = a.z <= b.z ? a.z : b.z;
    }


    public static void max( Vec3 a, Vec3 b, Vec3 out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
        out.z = a.z >= b.z ? a.z : b.z;
    }


    public static float len( Vec3 a ) {
        return (float)Math.sqrt( lenSquared( a ) );
    }


    public static float lenSquared( Vec3 a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }


    public static float dist( Vec3 a, Vec3 b ) {
        return (float)Math.sqrt( distSquared( a, b ) );
    }


    public static float distSquared( Vec3 a, Vec3 b ) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz;
    }


    public static boolean near( Vec3 a, Vec3 b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y ) &&
            Tol.near( a.z, b.z );
    }


    public static boolean near( Vec3 a, Vec3 b, float relErr, float absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr ) &&
            Tol.equal( a.z, b.z, relErr, absErr );
    }


    public static void normalize( Vec3 a ) {
        float s = 1f / len( a );
        a.x *= s;
        a.y *= s;
        a.z *= s;
    }


    public static void normalize( Vec3 a, float normLength, Vec3 out ) {
        float d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
        out.z = a.z * d;
    }


    public static float dot( Vec3 a, Vec3 b ) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }


    public static float dot( Vec3 origin, Vec3 a, Vec3 b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y) +
               (a.z - origin.z) * (b.z - origin.z);
    }


    public static void cross( Vec3 a, Vec3 b, Vec3 out ) {
        out.x = a.y * b.z - b.y * a.z;
        out.y = a.z * b.x - b.z * a.x;
        out.z = a.x * b.y - b.x * a.y;
    }
    

    public static void cross( Vec3 origin, Vec3 a, Vec3 b, Vec3 out ) {
        out.x = (a.y - origin.y) * (b.z - origin.z) - (b.y - origin.y) * (a.z - origin.z);
        out.y = (a.z - origin.z) * (b.x - origin.x) - (b.z - origin.z) * (a.x - origin.x);
        out.z = (a.x - origin.x) * (b.y - origin.y) - (b.x - origin.x) * (a.y - origin.y);
    }


    public static float cosAng( Vec3 a, Vec3 b ) {
        return dot( a, b ) / (float)Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static float cosAng( Vec3 origin, Vec3 a, Vec3 b ) {
        float ax = a.x - origin.x;
        float ay = a.y - origin.y;
        float az = a.z - origin.z;
        float bx = b.x - origin.x;
        float by = b.y - origin.y;
        float bz = b.z - origin.z;

        float dd = ( ax*ax + ay*ay + az*az ) * ( bx*bx + by*by + bz*bz );
        return ( ax*bx + ay*by + az*bz ) / (float)Math.sqrt( dd );
    }


    public static float ang( Vec3 a, Vec3 b ) {
        return (float)Math.acos( cosAng( a, b ) );
    }


    public static float ang( Vec3 origin, Vec3 a, Vec3 b ) {
        return (float)Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec3 a, Vec3 b, float p, Vec3 out ) {
        float q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
        out.z = q * a.z + p * b.z;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void reject( Vec3 a, Vec3 ref ) {
        float lenRef = lenSquared( ref );
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
        a.z -= ref.z * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void project( Vec3 a, Vec3 ref ) {
        float lenRef = lenSquared( ref );
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
        a.z = ref.z * parScale;
    }

    /**
     * Finds signed axis-aligned unit-vector nearest to input vector.
     * For example, the nearest axis to [ 0.8, -1.3, 0.1 ] is [ 0.0, -1.0, 0.0 ].
     *
     * @param x X-coord of axis.
     * @param y Y-coord of axis.
     * @param z Z-coord of axis.
     * @param out Length-3 array to hold axis on return.
     */
    public static void nearestAxis( float x, float y, float z, Vec3 out ) {
        float ax = x >= 0 ? x : -x;
        float ay = y >= 0 ? y : -y;
        float az = z >= 0 ? z : -z;

        if( ax > ay && ax > az ) {
            out.x = x >= 0 ? 1 : -1;
            out.y = 0;
            out.z = 0;
        } else if( ay > az ) {
            out.x = 0;
            out.y = y >= 0 ? 1 : -1;
            out.z = 0;
        } else {
            out.x = 0;
            out.y = 0;
            out.z = z >= 0 ? 1 : -1;
        }
    }

    /**
     * Picks a unit-length vector that is orthogonal to the input vector.
     */
    public static void chooseOrtho( float x, float y, float z, Vec3 out3x1 ) {
        chooseOrtho( x, y, z, 2, out3x1 );
    }

    /**
     * Picks a unitl-length vector that is orthogonal to the input vector.
     * <p>
     * This method allows the user to define a "zero-dimension", where the
     * vector that is returned by this method is guaranteed to have a zero coordinate
     * for that dimension. Additionally, the coordinate after the zero-dimension will
     * hold a non-negative mVal.
     * <p>
     * For example, <br>
     * {@code chooseOrtho3( 1.0f, 1.0f, 1.0f, 0, out )}<br>
     * will set <br>
     * {@code out = [  0.0000,  0.7071, -0.7071 ] }<br>
     * where {@code out.x} is zero and {@code out.y} is non-negative. <br>
     * {@code chooseOrtho3( 1.0f, 1.0f, 1.0f, 2, out )}<br>
     * will set <br>
     * {@code out = [  0.7071, -0.7071,  0.0000 ] }<br>
     *
     * @param x       X-coord of vector.
     * @param y       Y-coord of vector.
     * @param z       Z-coord of vector.
     * @param zeroDim Number {0,1,2} indicating if the output x, y, or z axis should be zero.
     * @param out     Length-3 array to hold output axis.
     */
    public static void chooseOrtho( float x, float y, float z, int zeroDim, Vec3 out ) {
        switch( zeroDim ) {
        case 2:
            if( y > FSQRT_ABS_TOL || -y > FSQRT_ABS_TOL ) {
                out.x = 1;
                out.y = -x/y;
                out.z = 0;
            } else if ( x > FSQRT_ABS_TOL || -x > FSQRT_ABS_TOL ) {
                out.x = -y/x;
                out.y = 1;
                out.z = 0;
            } else {
                out.x = 1;
                out.y = 0;
                out.z = 0;
                // No need to normalize3.
                return;
            }
            break;

        case 1:
            if( x > FSQRT_ABS_TOL || -x > FSQRT_ABS_TOL ) {
                out.x = -z / x;
                out.y = 0;
                out.z = 1;
            } else if( z > FSQRT_ABS_TOL || -z > FSQRT_ABS_TOL ) {
                out.x = 1;
                out.y = 0;
                out.z = -x / z;
            } else {
                out.x = 0;
                out.y = 0;
                out.z = 1;
                // No need to normalize3.
                return;
            }
            break;

        default:
            if( z > FSQRT_ABS_TOL || -z > FSQRT_ABS_TOL ) {
                out.x = 0;
                out.y = 1;
                out.z = -y / z;
            } else if( y > FSQRT_ABS_TOL || -y > FSQRT_ABS_TOL ) {
                out.x = 0;
                out.y = -z / y;
                out.z = 1;
            } else {
                out.x = 0;
                out.y = 1;
                out.z = 0;
                // No need to normalize3.
                return;
            }
            break;
        }

        normalize( out );
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    public static float lineToPointDistance( Vec3 n1, Vec3 n2, Vec3 p ) {
        float cx = (p.y - n1.y) * (p.z - n2.z) - (p.y - n2.y) * (p.z - n1.z);
        float cy = (p.z - n1.z) * (p.x - n2.x) - (p.z - n2.z) * (p.x - n1.x);
        float cz = (p.x - n1.x) * (p.y - n2.y) - (p.x - n2.x) * (p.y - n1.y);
        return (float)Math.sqrt( cx * cx + cy * cy + cz * cz ) / dist( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param n1 Start of line segment
     * @param n2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    public static float lineSegmentToPointDistance( Vec3 n1, Vec3 n2, Vec3 p ) {
        float u0 = p.x - n1.x;
        float u1 = p.y - n1.y;
        float u2 = p.z - n1.z;

        float v0 = n2.x - n1.x;
        float v1 = n2.y - n1.y;
        float v2 = n2.z - n1.z;

        float num = u0 * v0 + u1 * v1 + u2 * v2;
        float den = v0 * v0 + v1 * v1 + v2 * v2;
        float t   = num / den;

        if( den < FSQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in u0,u1,u2).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p.x - n2.x;
            u1 = p.y - n2.y;
            u2 = p.z - n2.z;
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
            u2 -= v2 * t;
        }

        return (float)Math.sqrt( u0 * u0 + u1 * u1 + u2 * u2 );
    }

    /**
     * Finds intersection of line and plane. The intersection point is
     * only defined if this method returns 1, indicating that the line and
     * plane intersect at one point.
     *
     * @param line0      First point on line.
     * @param line1      Second point on line.
     * @param planePoint A Point on the plane.
     * @param planeNorm  Normal vector of plane.
     * @param optOut     length-3 array to hold point of intersection. May be {@code null}
     * @return 0 if no intersection,
     *         1 if point intersection ( line crosses plane )
     *         2 if line intersection ( line lies on plane )
     */
    public static int intersectLinePlane( 
        Vec3 line0,
        Vec3 line1,
        Vec3 planePoint,
        Vec3 planeNorm,
        Vec3 optOut 
    ) {
        float dx = line1.x - line0.x;
        float dy = line1.y - line0.y;
        float dz = line1.z - line0.z;

        float num = (planePoint.x - line0.x) * planeNorm.x +
                    (planePoint.y - line0.y) * planeNorm.y +
                    (planePoint.z - line0.z) * planeNorm.z;

        float den = dx * planeNorm.x + dy * planeNorm.y + dz * planeNorm.z;

        if( den < FSQRT_ABS_TOL && den > -FSQRT_ABS_TOL ) {
            return num < FSQRT_ABS_TOL && num > -FSQRT_ABS_TOL ? 2 : 0;
        }

        if( optOut != null ) {
            float d = num / den;
            optOut.x = d * dx + line0.x;
            optOut.y = d * dy + line0.y;
            optOut.z = d * dz + line0.z;
        }

        return 1;
    }


    /**
     * Finds "intersection", or smallest connecting line, between two 3d lines.
     * (Lines, not line segments).
     *
     * @param a0       First point on line a
     * @param a1       Second point on line a
     * @param b0       First point on line b
     * @param b1       Second point on line b
     * @param optOutA  On return, holds point on line a nearest to line b (optional)
     * @param optOutB  On return, holds point on line b nearest to line a (optional)
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    public static boolean intersectLineLine( 
        Vec3 a0,
        Vec3 a1,
        Vec3 b0,
        Vec3 b1,
        Vec3 optOutA,
        Vec3 optOutB 
    ) {
        float da0b0b1b0 = (a0.x - b0.x) * (b1.x - b0.x) + (a0.y - b0.y) * (b1.y - b0.y) + (a0.z - b0.z) * (b1.z - b0.z);
        float db1b0a1a0 = (b1.x - b0.x) * (a1.x - a0.x) + (b1.y - b0.y) * (a1.y - a0.y) + (b1.z - b0.z) * (a1.z - a0.z);
        float da0b0a1a0 = (a0.x - b0.x) * (a1.x - a0.x) + (a0.y - b0.y) * (a1.y - a0.y) + (a0.z - b0.z) * (a1.z - a0.z);
        float db1b0b1b0 = (b1.x - b0.x) * (b1.x - b0.x) + (b1.y - b0.y) * (b1.y - b0.y) + (b1.z - b0.z) * (b1.z - b0.z);
        float da1a0a1a0 = (a1.x - a0.x) * (a1.x - a0.x) + (a1.y - a0.y) * (a1.y - a0.y) + (a1.z - a0.z) * (a1.z - a0.z);

        float num = da0b0b1b0 * db1b0a1a0 - da0b0a1a0 * db1b0b1b0;
        float den = da1a0a1a0 * db1b0b1b0 - db1b0a1a0 * db1b0a1a0;

        if( den < FSQRT_ABS_TOL && -den < FSQRT_ABS_TOL ) {
            return false;
        }

        float mua = num / den;
        if( optOutA != null ) {
            multAdd( 1.0f - mua, a0, mua, a1, optOutA );
        }
        if( optOutB != null ) {
            float mub = ( da0b0b1b0 + mua * db1b0a1a0 ) / db1b0b1b0;
            multAdd( 1.0f - mub, b0, mub, b1, optOutB );
        }

        return true;
    }


    public static boolean isNaN( Vec3 vec ) {
        return vec.x != vec.x ||
               vec.y != vec.y ||
               vec.z != vec.z;
    }

    
    public static String format( Vec3 vec ) {
        return String.format( FORMAT3, vec.x, vec.y, vec.z );
    }



    //== VEC4 Functions =======================================================


    public static void put( float x, float y, float z, float w, Vec4 out ) {
        out.x = x;
        out.y = y;
        out.z = z;
        out.w = w;
    }


    public static void put( Vec4 v, Vec4 out ) {
        out.x = v.x;
        out.y = v.y;
        out.z = v.z;
        out.w = v.w;
    }


    public static void put( Vec4 v, ByteBuffer out ) {
        out.putFloat( v.x );
        out.putFloat( v.y );
        out.putFloat( v.z );
        out.putFloat( v.w );
    }


    public static void put( ByteBuffer b, Vec4 v ) {
        v.x = b.getFloat();
        v.y = b.getFloat();
        v.z = b.getFloat();
        v.w = b.getFloat();
    }


    public static void put( Vec4 v, FloatBuffer out ) {
        out.put( v.x );
        out.put( v.y );
        out.put( v.z );
        out.put( v.w );
    }


    public static void put( FloatBuffer b, Vec4 v ) {
        v.x = b.get();
        v.y = b.get();
        v.z = b.get();
        v.w = b.get();
    }


    public static void put( Vec4 v, float[] out ) {
        out[0] = v.x;
        out[1] = v.y;
        out[2] = v.z;
        out[3] = v.w;
    }


    public static void put( float[] arr, Vec4 v ) {
        v.x = arr[0];
        v.y = arr[1];
        v.z = arr[2];
        v.w = arr[3];
    }


    public static void add( Vec4 a, Vec4 b, Vec4 out ) {
        out.x = a.x + b.x;
        out.y = a.y + b.y;
        out.z = a.z + b.z;
        out.w = a.w + b.w;
    }


    public static void add( float dx, float dy, float dz, float dw, Vec4 a, Vec4 out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
        out.z = a.z + dz;
        out.w = a.w + dw;
    }


    public static void addTo( Vec4 a, Vec4 out ) {
        out.x += a.x;
        out.y += a.y;
        out.z += a.z;
        out.w += a.w;
    }


    public static void subtract( Vec4 a, Vec4 b, Vec4 out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
        out.z = a.z - b.z;
        out.w = a.w - b.w;
    }


    public static void subtractFrom( Vec4 a, Vec4 out ) {
        out.x -= a.x;
        out.y -= a.y;
        out.z -= a.z;
        out.w -= a.w;
    }


    public static void mult( float sa, Vec4 a ) {
        a.x *= sa;
        a.y *= sa;
        a.z *= sa;
        a.w *= sa;
    }


    public static void mult( float sa, Vec4 a, Vec4 out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
        out.z = sa * a.z;
        out.w = sa * a.w;
    }


    public static void mult( float sx, float sy, float sz, float sw, Vec4 a, Vec4 out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
        out.z = sz * a.z;
        out.w = sw * a.w;
    }


    public static void multAdd( float sa, Vec4 a, float sb, Vec4 b, Vec4 out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
        out.z = a.z * sa + b.z * sb;
        out.w = a.w * sa + b.w * sb;
    }


    public static void multAddTo( float sa, Vec4 a, float sOut, Vec4 out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
        out.z = sOut * out.z + sa * a.z;
        out.w = sOut * out.w + sa * a.w;
    }


    public static void min( Vec4 a, Vec4 b, Vec4 out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
        out.z = a.z <= b.z ? a.z : b.z;
        out.w = a.w <= b.w ? a.w : b.w;
    }


    public static void max( Vec4 a, Vec4 b, Vec4 out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
        out.z = a.z >= b.z ? a.z : b.z;
        out.w = a.w >= b.w ? a.w : b.w;
    }


    public static float len( Vec4 a ) {
        return (float)Math.sqrt( lenSquared( a ) );
    }


    public static float lenSquared( Vec4 a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w;
    }


    public static float dist( Vec4 a, Vec4 b ) {
        return (float)Math.sqrt( distSquared( a, b ) );
    }


    public static float distSquared( Vec4 a, Vec4 b ) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float dz = a.z - b.z;
        float dw = a.w - b.w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }


    public static boolean near( Vec4 a, Vec4 b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y ) &&
            Tol.near( a.z, b.z ) &&
            Tol.near( a.w, b.w );
    }


    public static boolean near( Vec4 a, Vec4 b, float relErr, float absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr ) &&
            Tol.equal( a.z, b.z, relErr, absErr ) &&
            Tol.equal( a.w, b.w, relErr, absErr );
    }


    public static void normalize( Vec4 a ) {
        float s = 1f / len( a );
        a.x *= s;
        a.y *= s;
        a.z *= s;
        a.w *= s;
    }


    public static void normalize( Vec4 a, float normLength, Vec4 out ) {
        float d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
        out.z = a.z * d;
        out.w = a.w * d;
    }


    public static float dot( Vec4 a, Vec4 b ) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }


    public static float dot( Vec4 origin, Vec4 a, Vec4 b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y) +
               (a.z - origin.z) * (b.z - origin.z) +
               (a.w - origin.w) * (b.w - origin.w);
    }


    public static float cosAng( Vec4 a, Vec4 b ) {
        return dot( a, b ) / (float)Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static float cosAng( Vec4 origin, Vec4 a, Vec4 b ) {
        float ax = a.x - origin.x;
        float ay = a.y - origin.y;
        float az = a.z - origin.z;
        float aw = a.w - origin.w;
        float bx = b.x - origin.x;
        float by = b.y - origin.y;
        float bz = b.z - origin.z;
        float bw = b.w - origin.w;

        float dd = ( ax*ax + ay*ay + az*az + aw*aw ) * ( bx*bx + by*by + bz*bz + bw*bw );
        return ( ax*bx + ay*by + az*bz + aw*bw ) / (float)Math.sqrt( dd );
    }


    public static float ang( Vec4 a, Vec4 b ) {
        return (float)Math.acos( cosAng( a, b ) );
    }


    public static float ang( Vec4 origin, Vec4 a, Vec4 b ) {
        return (float)Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec4 a, Vec4 b, float p, Vec4 out ) {
        float q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
        out.z = q * a.z + p * b.z;
        out.w = q * a.w + p * b.w;
    }


    public static boolean isNaN( Vec4 vec ) {
        return vec.x != vec.x ||
               vec.y != vec.y ||
               vec.z != vec.z ||
               vec.w != vec.w;
    }


    public static String format( Vec4 vec ) {
        return String.format( FORMAT4, vec.x, vec.y, vec.z, vec.w );
    }





    //== VEC2D Functions ===========================

    
    public static void add( Vec2d a, Vec2d b, Vec2d out ) {
        out.x = a.x + b.x;
        out.y = a.y + b.y;
    }


    public static void add( double dx, double dy, Vec2d a, Vec2d out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
    }


    public static void addTo( Vec2d a, Vec2d out ) {
        out.x += a.x;
        out.y += a.y;
    }


    public static void subtract( Vec2d a, Vec2d b, Vec2d out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
    }


    public static void subtractFrom( Vec2d a, Vec2d out ) {
        out.x -= a.x;
        out.y -= a.y;
    }


    public static void mult( double sa, Vec2d a ) {
        a.x *= sa;
        a.y *= sa;
    }


    public static void mult( double sa, Vec2d a, Vec2d out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
    }


    public static void mult( double sx, double sy, Vec2d a, Vec2d out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
    }


    public static void multAdd( double sa, Vec2d a, double sb, Vec2d b, Vec2d out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
    }


    public static void multAddTo( double sa, Vec2d a, double sOut, Vec2d out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
    }


    public static void min( Vec2d a, Vec2d b, Vec2d out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
    }


    public static void max( Vec2d a, Vec2d b, Vec2d out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
    }


    public static double len( Vec2d a ) {
        return Math.sqrt( lenSquared( a ) );
    }


    public static double lenSquared( Vec2d a ) {
        return a.x * a.x + a.y * a.y;
    }


    public static double dist( Vec2d a, Vec2d b ) {
        return Math.sqrt( distSquared( a, b ) );
    }


    public static double distSquared( Vec2d a, Vec2d b ) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx * dx + dy * dy;
    }


    public static boolean near( Vec2d a, Vec2d b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y );
    }


    public static boolean near( Vec2d a, Vec2d b, double relErr, double absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr );
    }


    public static void normalize( Vec2d a ) {
        double s = 1f / len( a );
        a.x *= s;
        a.y *= s;
    }


    public static void normalize( Vec2d a, double normLength, Vec2d out ) {
        double d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
    }


    public static double dot( Vec2d a, Vec2d b ) {
        return a.x * b.x + a.y * b.y;
    }


    public static double dot( Vec2d origin, Vec2d a, Vec2d b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y);
    }


    public static double cosAng( Vec2d a, Vec2d b ) {
        return dot( a, b ) / Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static double cosAng( Vec2d origin, Vec2d a, Vec2d b ) {
        double ax = a.x - origin.x;
        double ay = a.y - origin.y;
        double bx = b.x - origin.x;
        double by = b.y - origin.y;

        double dd = ( ax*ax + ay*ay ) * ( bx*bx + by*by );
        return ( ax*bx + ay*by ) / Math.sqrt( dd );
    }


    public static double ang( Vec2d a, Vec2d b ) {
        return Math.acos( cosAng( a, b ) );
    }


    public static double ang( Vec2d origin, Vec2d a, Vec2d b ) {
        return Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec2d a, Vec2d b, double p, Vec2d out ) {
        double q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void reject( Vec2d a, Vec2d ref ) {
        double lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void project( Vec2d a, Vec2d ref ) {
        double lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    public static double linePointDistance( Vec2d n1, Vec2d n2, Vec2d p ) {
        double c = (p.x - n1.x) * (p.y - n2.y) - (p.x - n2.x) * (p.y - n1.y);
        return c / dist( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param s1 Start of line segment
     * @param s2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    public static double segmentPointDistance( Vec2d s1, Vec2d s2, Vec2d p ) {
        double u0 =  p.x - s1.x;
        double u1 =  p.y - s1.y;
        double v0 = s2.x - s1.x;
        double v1 = s2.y - s1.y;

        double num = u0 * v0 + u1 * v1;
        double den = v0 * v0 + v1 * v1;
        double t   = num / den;

        if( den < SQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in <u0,u1>).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p.x - s2.x;
            u1 = p.y - s2.y;
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
        }

        return Math.sqrt( u0 * u0 + u1 * u1 );
    }

    /**
     * Finds intersection between two lines.
     *
     * @param a0  First point on line a
     * @param a1  Second point on line a
     * @param b0  First point on line b
     * @param b1  Second point on line b
     * @param out On return, holds point of intersection if lines are not parallel.
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    public static boolean lineIntersection( Vec2d a0, Vec2d a1, Vec2d b0, Vec2d b1, Vec2d out ) {
        double bx = a1.x - a0.x;
        double by = a1.y - a0.y;
        double dx = b1.x - b0.x;
        double dy = b1.y - b0.y;
        double dotPerp = bx * dy - by * dx;
        if( dotPerp < SQRT_ABS_TOL && -dotPerp < SQRT_ABS_TOL ) {
            return false;
        }
        double cx = b0.x - a0.x;
        double cy = b0.y - a0.y;
        double t = (cx * dy - cy * dx) / dotPerp;

        out.x = a0.x + t * bx;
        out.y = a0.y + t * by;
        return true;
    }

    /**
     * Finds intersection between two line segments.
     *
     * @param a0  First point on segment a
     * @param a1  Second point on segment a
     * @param b0  First point on segment b
     * @param b1  Second point on segment b
     * @param out On return, holds point of intersection if exists.
     * @return true if lines intersect at single place.
     */
    public static boolean segmentIntersection( Vec2d a0, Vec2d a1, Vec2d b0, Vec2d b1, Vec2d out ) {
        double bx = a1.x - a0.x;
        double by = a1.y - a0.y;
        double dx = b1.x - b0.x;
        double dy = b1.y - b0.y;
        double dotPerp = bx * dy - by * dx;
        if( dotPerp < SQRT_ABS_TOL && -dotPerp < SQRT_ABS_TOL ) {
            return false;
        }

        double cx = b0.x - a0.x;
        double cy = b0.y - a0.y;
        double t = (cx * dy - cy * dx) / dotPerp;
        if( t < 0 || t > 1 ) {
            return false;
        }

        double u = (cx * by - cy * bx) / dotPerp;
        if( u < 0 || u > 1 ) {
            return false;
        }

        out.x = a0.x + t * bx;
        out.y = a0.y + t * by;
        return true;
    }


    public static boolean isNaN( Vec2d vec ) {
        return vec.x != vec.x || vec.y != vec.y;
    }



    
    //== VEC3D Functions =======================================================

    
    public static void add( double dx, double dy, double dz, Vec3d a, Vec3d out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
        out.z = a.z + dz;
    }


    public static void addTo( Vec3d a, Vec3d out ) {
        out.x += a.x;
        out.y += a.y;
        out.z += a.z;
    }


    public static void addTo( double dx, double dy, double dz, Vec3d out ) {
        out.x += dx;
        out.y += dy;
        out.z += dz;
    }


    public static void subtract( Vec3d a, Vec3d b, Vec3d out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
        out.z = a.z - b.z;
    }


    public static void subtractFrom( Vec3d a, Vec3d out ) {
        out.x -= a.x;
        out.y -= a.y;
        out.z -= a.z;
    }


    public static void mult( double sa, Vec3d a ) {
        a.x *= sa;
        a.y *= sa;
        a.z *= sa;
    }


    public static void mult( double sa, Vec3d a, Vec3d out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
        out.z = sa * a.z;
    }


    public static void mult( double sx, double sy, double sz, Vec3d a, Vec3d out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
        out.z = sz * a.z;
    }


    public static void multAdd( double sa, Vec3d a, double sb, Vec3d b, Vec3d out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
        out.z = a.z * sa + b.z * sb;
    }


    public static void multAddTo( double sa, Vec3d a, double sOut, Vec3d out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
        out.z = sOut * out.z + sa * a.z;
    }


    public static void min( Vec3d a, Vec3d b, Vec3d out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
        out.z = a.z <= b.z ? a.z : b.z;
    }


    public static void max( Vec3d a, Vec3d b, Vec3d out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
        out.z = a.z >= b.z ? a.z : b.z;
    }


    public static double len( Vec3d a ) {
        return Math.sqrt( lenSquared( a ) );
    }


    public static double lenSquared( Vec3d a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }


    public static double dist( Vec3d a, Vec3d b ) {
        return Math.sqrt( distSquared( a, b ) );
    }


    public static double distSquared( Vec3d a, Vec3d b ) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz;
    }


    public static boolean near( Vec3d a, Vec3d b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y ) &&
            Tol.near( a.z, b.z );
    }


    public static boolean near( Vec3d a, Vec3d b, double relErr, double absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr ) &&
            Tol.equal( a.z, b.z, relErr, absErr );
    }


    public static void normalize( Vec3d a ) {
        double s = 1f / len( a );
        a.x *= s;
        a.y *= s;
        a.z *= s;
    }


    public static void normalize( Vec3d a, double normLength, Vec3d out ) {
        double d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
        out.z = a.z * d;
    }


    public static double dot( Vec3d a, Vec3d b ) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }


    public static double dot( Vec3d origin, Vec3d a, Vec3d b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y) +
               (a.z - origin.z) * (b.z - origin.z);
    }


    public static void cross( Vec3d a, Vec3d b, Vec3d out ) {
        out.x = a.y * b.z - b.y * a.z;
        out.y = a.z * b.x - b.z * a.x;
        out.z = a.x * b.y - b.x * a.y;
    }


    public static void cross( Vec3d origin, Vec3d a, Vec3d b, Vec3d out ) {
        out.x = (a.y - origin.y) * (b.z - origin.z) - (b.y - origin.y) * (a.z - origin.z);
        out.y = (a.z - origin.z) * (b.x - origin.x) - (b.z - origin.z) * (a.x - origin.x);
        out.z = (a.x - origin.x) * (b.y - origin.y) - (b.x - origin.x) * (a.y - origin.y);
    }


    public static double cosAng( Vec3d a, Vec3d b ) {
        return dot( a, b ) / Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static double cosAng( Vec3d origin, Vec3d a, Vec3d b ) {
        double ax = a.x - origin.x;
        double ay = a.y - origin.y;
        double az = a.z - origin.z;
        double bx = b.x - origin.x;
        double by = b.y - origin.y;
        double bz = b.z - origin.z;

        double dd = ( ax*ax + ay*ay + az*az ) * ( bx*bx + by*by + bz*bz );
        return ( ax*bx + ay*by + az*bz ) / Math.sqrt( dd );
    }


    public static double ang( Vec3d a, Vec3d b ) {
        return Math.acos( cosAng( a, b ) );
    }


    public static double ang( Vec3d origin, Vec3d a, Vec3d b ) {
        return Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec3d a, Vec3d b, double p, Vec3d out ) {
        double q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
        out.z = q * a.z + p * b.z;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void reject( Vec3d a, Vec3d ref ) {
        double lenRef = lenSquared( ref );
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
        a.z -= ref.z * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    public static void project( Vec3d a, Vec3d ref ) {
        double lenRef = lenSquared( ref );
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
        a.z = ref.z * parScale;
    }

    /**
     * Finds signed axis-aligned unit-vector nearest to input vector.
     * For example, the nearest axis to [ 0.8, -1.3, 0.1 ] is [ 0.0, -1.0, 0.0 ].
     *
     * @param x X-coord of axis.
     * @param y Y-coord of axis.
     * @param z Z-coord of axis.
     * @param out Length-3 array to hold axis on return.
     */
    public static void nearestAxis( double x, double y, double z, Vec3d out ) {
        double ax = x >= 0 ? x : -x;
        double ay = y >= 0 ? y : -y;
        double az = z >= 0 ? z : -z;

        if( ax > ay && ax > az ) {
            out.x = x >= 0 ? 1 : -1;
            out.y = 0;
            out.z = 0;
        } else if( ay > az ) {
            out.x = 0;
            out.y = y >= 0 ? 1 : -1;
            out.z = 0;
        } else {
            out.x = 0;
            out.y = 0;
            out.z = z >= 0 ? 1 : -1;
        }
    }

    /**
     * Picks a unit-length vector that is orthogonal to the input vector.
     */
    public static void chooseOrtho( double x, double y, double z, Vec3d out3x1 ) {
        chooseOrtho( x, y, z, 2, out3x1 );
    }

    /**
     * Picks a unitl-length vector that is orthogonal to the input vector.
     * <p>
     * This method allows the user to define a "zero-dimension", where the
     * vector that is returned by this method is guaranteed to have a zero coordinate
     * for that dimension. Additionally, the coordinate after the zero-dimension will
     * hold a non-negative mVal.
     * <p>
     * For example, <br>
     * {@code chooseOrtho3( 1.0f, 1.0f, 1.0f, 0, out )}<br>
     * will set <br>
     * {@code out = [  0.0000,  0.7071, -0.7071 ] }<br>
     * where {@code out.x} is zero and {@code out.y} is non-negative. <br>
     * {@code chooseOrtho3( 1.0f, 1.0f, 1.0f, 2, out )}<br>
     * will set <br>
     * {@code out = [  0.7071, -0.7071,  0.0000 ] }<br>
     *
     * @param x       X-coord of vector.
     * @param y       Y-coord of vector.
     * @param z       Z-coord of vector.
     * @param zeroDim Number {0,1,2} indicating if the output x, y, or z axis should be zero.
     * @param out     Length-3 array to hold output axis.
     */
    public static void chooseOrtho( double x, double y, double z, int zeroDim, Vec3d out ) {
        switch( zeroDim ) {
        case 2:
            if( y > SQRT_ABS_TOL || -y > SQRT_ABS_TOL ) {
                out.x = 1;
                out.y = -x/y;
                out.z = 0;
            } else if ( x > SQRT_ABS_TOL || -x > SQRT_ABS_TOL ) {
                out.x = -y/x;
                out.y = 1;
                out.z = 0;
            } else {
                out.x = 1;
                out.y = 0;
                out.z = 0;
                // No need to normalize3.
                return;
            }
            break;

        case 1:
            if( x > SQRT_ABS_TOL || -x > SQRT_ABS_TOL ) {
                out.x = -z / x;
                out.y = 0;
                out.z = 1;
            } else if( z > SQRT_ABS_TOL || -z > SQRT_ABS_TOL ) {
                out.x = 1;
                out.y = 0;
                out.z = -x / z;
            } else {
                out.x = 0;
                out.y = 0;
                out.z = 1;
                // No need to normalize3.
                return;
            }
            break;

        default:
            if( z > SQRT_ABS_TOL || -z > SQRT_ABS_TOL ) {
                out.x = 0;
                out.y = 1;
                out.z = -y / z;
            } else if( y > SQRT_ABS_TOL || -y > SQRT_ABS_TOL ) {
                out.x = 0;
                out.y = -z / y;
                out.z = 1;
            } else {
                out.x = 0;
                out.y = 1;
                out.z = 0;
                // No need to normalize3.
                return;
            }
            break;
        }

        normalize( out );
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    public static double lineToPointDistance( Vec3d n1, Vec3d n2, Vec3d p ) {
        double cx = (p.y - n1.y) * (p.z - n2.z) - (p.y - n2.y) * (p.z - n1.z);
        double cy = (p.z - n1.z) * (p.x - n2.x) - (p.z - n2.z) * (p.x - n1.x);
        double cz = (p.x - n1.x) * (p.y - n2.y) - (p.x - n2.x) * (p.y - n1.y);
        return Math.sqrt( cx * cx + cy * cy + cz * cz ) / dist( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param n1 Start of line segment
     * @param n2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    public static double lineSegmentToPointDistance( Vec3d n1, Vec3d n2, Vec3d p ) {
        double u0 = p.x - n1.x;
        double u1 = p.y - n1.y;
        double u2 = p.z - n1.z;

        double v0 = n2.x - n1.x;
        double v1 = n2.y - n1.y;
        double v2 = n2.z - n1.z;

        double num = u0 * v0 + u1 * v1 + u2 * v2;
        double den = v0 * v0 + v1 * v1 + v2 * v2;
        double t   = num / den;

        if( den < SQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in u0,u1,u2).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p.x - n2.x;
            u1 = p.y - n2.y;
            u2 = p.z - n2.z;
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
            u2 -= v2 * t;
        }

        return Math.sqrt( u0 * u0 + u1 * u1 + u2 * u2 );
    }

    /**
     * Finds intersection of line and plane. The intersection point is
     * only defined if this method returns 1, indicating that the line and
     * plane intersect at one point.
     *
     * @param line0      First point on line.
     * @param line1      Second point on line.
     * @param planePoint A Point on the plane.
     * @param planeNorm  Normal vector of plane.
     * @param optOut     length-3 array to hold point of intersection. May be {@code null}
     * @return 0 if no intersection,
     *         1 if point intersection ( line crosses plane )
     *         2 if line intersection ( line lies on plane )
     */
    public static int intersectLinePlane( 
        Vec3d line0,
        Vec3d line1,
        Vec3d planePoint,
        Vec3d planeNorm,
        Vec3d optOut 
    ) {
        double dx = line1.x - line0.x;
        double dy = line1.y - line0.y;
        double dz = line1.z - line0.z;

        double num = (planePoint.x - line0.x) * planeNorm.x +
                    (planePoint.y - line0.y) * planeNorm.y +
                    (planePoint.z - line0.z) * planeNorm.z;

        double den = dx * planeNorm.x + dy * planeNorm.y + dz * planeNorm.z;

        if( den < SQRT_ABS_TOL && den > -SQRT_ABS_TOL ) {
            return num < SQRT_ABS_TOL && num > -SQRT_ABS_TOL ? 2 : 0;
        }

        if( optOut != null ) {
            double d = num / den;
            optOut.x = d * dx + line0.x;
            optOut.y = d * dy + line0.y;
            optOut.z = d * dz + line0.z;
        }

        return 1;
    }


    /**
     * Finds "intersection", or smallest connecting line, between two 3d lines.
     * (Lines, not line segments).
     *
     * @param a0       First point on line a
     * @param a1       Second point on line a
     * @param b0       First point on line b
     * @param b1       Second point on line b
     * @param optOutA  On return, holds point on line a nearest to line b (optional)
     * @param optOutB  On return, holds point on line b nearest to line a (optional)
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    public static boolean intersectLineLine( 
        Vec3d a0,
        Vec3d a1,
        Vec3d b0,
        Vec3d b1,
        Vec3d optOutA,
        Vec3d optOutB 
    ) {
        double da0b0b1b0 = (a0.x - b0.x) * (b1.x - b0.x) + (a0.y - b0.y) * (b1.y - b0.y) + (a0.z - b0.z) * (b1.z - b0.z);
        double db1b0a1a0 = (b1.x - b0.x) * (a1.x - a0.x) + (b1.y - b0.y) * (a1.y - a0.y) + (b1.z - b0.z) * (a1.z - a0.z);
        double da0b0a1a0 = (a0.x - b0.x) * (a1.x - a0.x) + (a0.y - b0.y) * (a1.y - a0.y) + (a0.z - b0.z) * (a1.z - a0.z);
        double db1b0b1b0 = (b1.x - b0.x) * (b1.x - b0.x) + (b1.y - b0.y) * (b1.y - b0.y) + (b1.z - b0.z) * (b1.z - b0.z);
        double da1a0a1a0 = (a1.x - a0.x) * (a1.x - a0.x) + (a1.y - a0.y) * (a1.y - a0.y) + (a1.z - a0.z) * (a1.z - a0.z);

        double num = da0b0b1b0 * db1b0a1a0 - da0b0a1a0 * db1b0b1b0;
        double den = da1a0a1a0 * db1b0b1b0 - db1b0a1a0 * db1b0a1a0;

        if( den < SQRT_ABS_TOL && -den < SQRT_ABS_TOL ) {
            return false;
        }

        double mua = num / den;
        if( optOutA != null ) {
            multAdd( 1.0f - mua, a0, mua, a1, optOutA );
        }
        if( optOutB != null ) {
            double mub = ( da0b0b1b0 + mua * db1b0a1a0 ) / db1b0b1b0;
            multAdd( 1.0f - mub, b0, mub, b1, optOutB );
        }

        return true;
    }


    public static boolean isNaN( Vec3d vec ) {
        return vec.x != vec.x ||
               vec.y != vec.y ||
               vec.z != vec.z;
    }




    //== VEC4D Functions =======================================================


    public static void add( Vec4d a, Vec4d b, Vec4d out ) {
        out.x = a.x + b.x;
        out.y = a.y + b.y;
        out.z = a.z + b.z;
        out.w = a.w + b.w;
    }


    public static void add( double dx, double dy, double dz, double dw, Vec4d a, Vec4d out ) {
        out.x = a.x + dx;
        out.y = a.y + dy;
        out.z = a.z + dz;
        out.w = a.w + dw;
    }


    public static void addTo( Vec4d a, Vec4d out ) {
        out.x += a.x;
        out.y += a.y;
        out.z += a.z;
        out.w += a.w;
    }


    public static void subtract( Vec4d a, Vec4d b, Vec4d out ) {
        out.x = a.x - b.x;
        out.y = a.y - b.y;
        out.z = a.z - b.z;
        out.w = a.w - b.w;
    }


    public static void subtractFrom( Vec4d a, Vec4d out ) {
        out.x -= a.x;
        out.y -= a.y;
        out.z -= a.z;
        out.w -= a.w;
    }


    public static void mult( double sa, Vec4d a ) {
        a.x *= sa;
        a.y *= sa;
        a.z *= sa;
        a.w *= sa;
    }


    public static void mult( double sa, Vec4d a, Vec4d out ) {
        out.x = sa * a.x;
        out.y = sa * a.y;
        out.z = sa * a.z;
        out.w = sa * a.w;
    }


    public static void mult( double sx, double sy, double sz, double sw, Vec4d a, Vec4d out ) {
        out.x = sx * a.x;
        out.y = sy * a.y;
        out.z = sz * a.z;
        out.w = sw * a.w;
    }


    public static void multAdd( double sa, Vec4d a, double sb, Vec4d b, Vec4d out ) {
        out.x = a.x * sa + b.x * sb;
        out.y = a.y * sa + b.y * sb;
        out.z = a.z * sa + b.z * sb;
        out.w = a.w * sa + b.w * sb;
    }


    public static void multAddTo( double sa, Vec4d a, double sOut, Vec4d out ) {
        out.x = sOut * out.x + sa * a.x;
        out.y = sOut * out.y + sa * a.y;
        out.z = sOut * out.z + sa * a.z;
        out.w = sOut * out.w + sa * a.w;
    }


    public static void min( Vec4d a, Vec4d b, Vec4d out ) {
        out.x = a.x <= b.x ? a.x : b.x;
        out.y = a.y <= b.y ? a.y : b.y;
        out.z = a.z <= b.z ? a.z : b.z;
        out.w = a.w <= b.w ? a.w : b.w;
    }


    public static void max( Vec4d a, Vec4d b, Vec4d out ) {
        out.x = a.x >= b.x ? a.x : b.x;
        out.y = a.y >= b.y ? a.y : b.y;
        out.z = a.z >= b.z ? a.z : b.z;
        out.w = a.w >= b.w ? a.w : b.w;
    }


    public static double len( Vec4d a ) {
        return Math.sqrt( lenSquared( a ) );
    }


    public static double lenSquared( Vec4d a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w;
    }


    public static double dist( Vec4d a, Vec4d b ) {
        return Math.sqrt( distSquared( a, b ) );
    }


    public static double distSquared( Vec4d a, Vec4d b ) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double dz = a.z - b.z;
        double dw = a.w - b.w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }


    public static boolean near( Vec4d a, Vec4d b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y ) &&
            Tol.near( a.z, b.z ) &&
            Tol.near( a.w, b.w );
    }


    public static boolean near( Vec4d a, Vec4d b, double relErr, double absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr ) &&
            Tol.equal( a.z, b.z, relErr, absErr ) &&
            Tol.equal( a.w, b.w, relErr, absErr );
    }


    public static void normalize( Vec4d a ) {
        double s = 1f / len( a );
        a.x *= s;
        a.y *= s;
        a.z *= s;
        a.w *= s;
    }


    public static void normalize( Vec4d a, double normLength, Vec4d out ) {
        double d = normLength / len(a);
        out.x = a.x * d;
        out.y = a.y * d;
        out.z = a.z * d;
        out.w = a.w * d;
    }


    public static double dot( Vec4d a, Vec4d b ) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }


    public static double dot( Vec4d origin, Vec4d a, Vec4d b ) {
        return (a.x - origin.x) * (b.x - origin.x) +
               (a.y - origin.y) * (b.y - origin.y) +
               (a.z - origin.z) * (b.z - origin.z) +
               (a.w - origin.w) * (b.w - origin.w);
    }


    public static double cosAng( Vec4d a, Vec4d b ) {
        return dot( a, b ) / Math.sqrt( lenSquared( a ) * lenSquared( b ) );
    }


    public static double cosAng( Vec4d origin, Vec4d a, Vec4d b ) {
        double ax = a.x - origin.x;
        double ay = a.y - origin.y;
        double az = a.z - origin.z;
        double aw = a.w - origin.w;
        double bx = b.x - origin.x;
        double by = b.y - origin.y;
        double bz = b.z - origin.z;
        double bw = b.w - origin.w;

        double dd = ( ax*ax + ay*ay + az*az + aw*aw ) * ( bx*bx + by*by + bz*bz + bw*bw );
        return ( ax*bx + ay*by + az*bz + aw*bw ) / Math.sqrt( dd );
    }


    public static double ang( Vec4d a, Vec4d b ) {
        return Math.acos( cosAng( a, b ) );
    }


    public static double ang( Vec4d origin, Vec4d a, Vec4d b ) {
        return Math.acos( cosAng( origin, a, b ) );
    }


    public static void lerp( Vec4d a, Vec4d b, double p, Vec4d out ) {
        double q = 1.0f - p;
        out.x = q * a.x + p * b.x;
        out.y = q * a.y + p * b.y;
        out.z = q * a.z + p * b.z;
        out.w = q * a.w + p * b.w;
    }


    public static boolean isNaN( Vec4d vec ) {
        return vec.x != vec.x ||
               vec.y != vec.y ||
               vec.z != vec.z ||
               vec.w != vec.w;
    }



    //=== DOUBLE[2] Functions ======================================

    @Deprecated
    public static void put( double x, double y, double[] out ) {
        out[0] = x;
        out[1] = y;
    }

    @Deprecated
    public static void put( Vec2 v, double[] out ) {
        out[0] = v.x;
        out[1] = v.y;
    }

    @Deprecated
    public static void add2( double[] a, double[] b, double[] out ) {
        out[0] = a[0] + b[0];
        out[1] = a[1] + b[1];
    }

    @Deprecated
    public static void addTo2( double[] a, double[] out ) {
        out[0] += a[0];
        out[1] += a[1];
    }

    @Deprecated
    public static void subtract2( double[] a, double[] b, double[] out ) {
        out[0] = a[0] - b[0];
        out[1] = a[1] - b[1];
    }

    @Deprecated
    public static void subtractFrom2( double[] a, double[] out ) {
        out[0] -= a[0];
        out[1] -= a[1];
    }

    @Deprecated
    public static void mult2( double sa, double[] a ) {
        a[0] *= sa;
        a[1] *= sa;
    }

    @Deprecated
    public static void mult2( double sa, double[] a, double[] out ) {
        out[0] = sa * a[0];
        out[1] = sa * a[1];
    }

    @Deprecated
    public static void multAdd2( double sa, double[] a, double sb, double[] b, double[] out ) {
        out[0] = a[0] * sa + b[0] * sb;
        out[1] = a[1] * sa + b[1] * sb;
    }

    @Deprecated
    public static void multAddTo2( double sa, double[] a, double sOut, double[] out ) {
        out[0] = sOut * out[0] + sa * a[0];
        out[1] = sOut * out[1] + sa * a[1];
    }

    @Deprecated
    public static void min2( double[]a, double[] b, double[] out ) {
        out[0] = a[0] <= b[0] ? a[0] : b[0];
        out[1] = a[1] <= b[1] ? a[1] : b[1];
    }

    @Deprecated
    public static void max2( double[] a, double[] b, double[] out ) {
        out[0] = a[0] >= b[0] ? a[0] : b[0];
        out[1] = a[1] >= b[1] ? a[1] : b[1];
    }

    @Deprecated
    public static double len2( double[] a ) {
        return Math.sqrt( lenSquared2( a ) );
    }

    @Deprecated
    public static double lenSquared2( double[] a ) {
        return a[0] * a[0] + a[1] * a[1];
    }

    @Deprecated
    public static double dist2( double[] a, double[] b ) {
        return Math.sqrt( distSquared2( a, b ) );
    }

    @Deprecated
    public static double distSquared2( double[] a, double[] b ) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        return dx * dx + dy * dy;
    }

    @Deprecated
    public static void normalize2( double[] a ) {
        double s = 1f / len2( a );
        a[0] *= s;
        a[1] *= s;
    }

    @Deprecated
    public static void normalize2( double[] a, double normLength, double[] out ) {
        double d = normLength / len2( a );
        out[0] = a[0] * d;
        out[1] = a[1] * d;
    }

    @Deprecated
    public static double dot2( double[] a, double[] b ) {
        return a[0] * b[0] + a[1] * b[1];
    }

    @Deprecated
    public static double dot2( double[] origin, double[] a, double[] b ) {
        return (a[0] - origin[0]) * (b[0] - origin[0]) +
               (a[1] - origin[1]) * (b[1] - origin[1]);
    }

    @Deprecated
    public static double cosAng2( double[] a, double[] b ) {
        return dot2( a, b ) / Math.sqrt( lenSquared2( a ) * lenSquared2( b ) );
    }

    @Deprecated
    public static double cosAng2( double[] origin, double[] a, double[] b ) {
        double ax = a[0] - origin[0];
        double ay = a[1] - origin[1];
        double bx = b[0] - origin[0];
        double by = b[1] - origin[1];

        double dd = ( ax*ax + ay*ay ) * ( bx*bx + by*by );
        return ( ax*bx + ay*by ) / Math.sqrt( dd );
    }

    @Deprecated
    public static double ang2( double[] a, double[] b ) {
        return Math.acos( cosAng2( a, b ) );
    }

    @Deprecated
    public static double ang2( double[] origin, double[] a, double[] b ) {
        return Math.acos( cosAng2( origin, a, b ) );
    }

    @Deprecated
    public static void lerp2( double[] a, double[] b, double p, double[] out ) {
        double q = 1.0f - p;
        out[0] = q * a[0] + p * b[0];
        out[1] = q * a[1] + p * b[1];
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    @Deprecated
    public static void reject2( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot2( a, ref ) / lenRef;
        a[0] -= ref[0] * parScale;
        a[1] -= ref[1] * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    @Deprecated
    public static void project2( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot2( a, ref ) / lenRef;
        a[0] = ref[0] * parScale;
        a[1] = ref[1] * parScale;
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    @Deprecated
    public static double linePointDistance2( double[] n1, double[] n2, double[] p ) {
        double c = (p[0] - n1[0]) * (p[1] - n2[1]) - (p[0] - n2[0]) * (p[1] - n1[1]);
        return c / dist2( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param s1 Start of line segment
     * @param s2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    @Deprecated
    public static double segmentPointDistance2( double[] s1, double[] s2, double[] p ) {
        double u0 =  p[0] - s1[0];
        double u1 =  p[1] - s1[1];
        double v0 = s2[0] - s1[0];
        double v1 = s2[1] - s1[1];

        double num = u0 * v0 + u1 * v1;
        double den = v0 * v0 + v1 * v1;
        double t   = num / den;

        if( den < SQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in <u0,u1>).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p[0] - s2[0];
            u1 = p[1] - s2[1];
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
        }

        return Math.sqrt( u0 * u0 + u1 * u1 );
    }

    /**
     * Finds intersection between two lines.
     *
     * @param a0  First point on line a
     * @param a1  Second point on line a
     * @param b0  First point on line b
     * @param b1  Second point on line b
     * @param out On return, holds point of intersection if lines are not parallel.
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    @Deprecated
    public static boolean lineIntersection2( double[] a0, double[] a1, double[] b0, double[] b1, double[] out ) {
        double bx = a1[0] - a0[0];
        double by = a1[1] - a0[1];
        double dx = b1[0] - b0[0];
        double dy = b1[1] - b0[1];
        double dotPerp = bx * dy - by * dx;
        if( dotPerp < SQRT_ABS_TOL && -dotPerp < SQRT_ABS_TOL ) {
            return false;
        }
        double cx = b0[0] - a0[0];
        double cy = b0[1] - a0[1];
        double t = (cx * dy - cy * dx) / dotPerp;

        out[0] = a0[0] + t * bx;
        out[1] = a0[1] + t * by;
        return true;
    }

    /**
     * Finds intersection between two line segments.
     *
     * @param a0  First point on segment a
     * @param a1  Second point on segment a
     * @param b0  First point on segment b
     * @param b1  Second point on segment b
     * @param out On return, holds point of intersection if exists.
     * @return true if lines intersect at single place.
     */
    @Deprecated
    public static boolean segmentIntersection2( double[] a0, double[] a1, double[] b0, double[] b1, double[] out ) {
        double bx = a1[0] - a0[0];
        double by = a1[1] - a0[1];
        double dx = b1[0] - b0[0];
        double dy = b1[1] - b0[1];
        double dotPerp = bx * dy - by * dx;
        if( dotPerp < SQRT_ABS_TOL && -dotPerp < SQRT_ABS_TOL ) {
            return false;
        }

        double cx = b0[0] - a0[0];
        double cy = b0[1] - a0[1];
        double t = (cx * dy - cy * dx) / dotPerp;
        if( t < 0 || t > 1 ) {
            return false;
        }

        double u = (cx * by - cy * bx) / dotPerp;
        if( u < 0 || u > 1 ) {
            return false;
        }

        out[0] = a0[0] + t * bx;
        out[1] = a0[1] + t * by;
        return true;
    }

    @Deprecated
    public static boolean isNaN2( double[] vec ) {
        return Double.isNaN( vec[0] ) ||
               Double.isNaN( vec[1] );

    }

    @Deprecated
    public static String format2( double[] vec ) {
        return String.format( "[ % 7.4f, % 7.4f ]", vec[0], vec[1] );
    }





    //=== DOUBLE[3] Functions ======================================

    @Deprecated
    public static void put( double x, double y, double z, double[] out ) {
        out[0] = x;
        out[1] = y;
        out[2] = z;
    }

    @Deprecated
    public static void put( Vec3 v, double[] out ) {
        out[0] = v.x;
        out[1] = v.y;
        out[2] = v.z;
    }

    @Deprecated
    public static void add3( double[] a, double[] b, double[] out ) {
        out[0] = a[0] + b[0];
        out[1] = a[1] + b[1];
        out[2] = a[2] + b[2];
    }

    @Deprecated
    public static void addTo3( double[] a, double[] out ) {
        out[0] += a[0];
        out[1] += a[1];
        out[2] += a[2];
    }

    @Deprecated
    public static void subtract3( double[] a, double[] b, double[] out ) {
        out[0] = a[0] - b[0];
        out[1] = a[1] - b[1];
        out[2] = a[2] - b[2];
    }

    @Deprecated
    public static void subtractFrom3( double[] a, double[] out ) {
        out[0] -= a[0];
        out[1] -= a[1];
        out[2] -= a[2];
    }

    @Deprecated
    public static void multAdd3( double sa, double[] a, double sb, double[] b, double[] out ) {
        out[0] = a[0] * sa + b[0] * sb;
        out[1] = a[1] * sa + b[1] * sb;
        out[2] = a[2] * sa + b[2] * sb;
    }

    @Deprecated
    public static void multAddTo3( double sa, double[] a, double sOut, double[] out ) {
        out[0] = sOut * out[0] + sa * a[0];
        out[1] = sOut * out[1] + sa * a[1];
        out[2] = sOut * out[2] + sa * a[2];
    }

    @Deprecated
    public static void min3( double[]a, double[] b, double[] out ) {
        out[0] = a[0] <= b[0] ? a[0] : b[0];
        out[1] = a[1] <= b[1] ? a[1] : b[1];
        out[2] = a[2] <= b[2] ? a[2] : b[2];
    }

    @Deprecated
    public static void max3( double[] a, double[] b, double[] out ) {
        out[0] = a[0] >= b[0] ? a[0] : b[0];
        out[1] = a[1] >= b[1] ? a[1] : b[1];
        out[2] = a[2] >= b[2] ? a[2] : b[2];
    }

    @Deprecated
    public static double len3( double[] a ) {
        return Math.sqrt( lenSquared3( a ) );
    }

    @Deprecated
    public static double lenSquared3( double[] a ) {
        return a[0] * a[0] + a[1] * a[1] + a[2] * a[2];
    }

    @Deprecated
    public static double dist3( double[] a, double[] b ) {
        return Math.sqrt( distSquared3( a, b ) );
    }

    @Deprecated
    public static double distSquared3( double[] a, double[] b ) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];
        return dx * dx + dy * dy + dz * dz;
    }

    @Deprecated
    public static void normalize3( double[] a ) {
        double s = 1 / len3( a );
        a[0] *= s;
        a[1] *= s;
        a[2] *= s;
    }

    @Deprecated
    public static void normalize3( double[] a, double normLength, double[] out ) {
        double d = normLength / len3( a );
        out[0] = a[0] * d;
        out[1] = a[1] * d;
        out[2] = a[2] * d;
    }

    @Deprecated
    public static void mult3( double sa, double[] a ) {
        a[0] *= sa;
        a[1] *= sa;
        a[2] *= sa;
    }

    @Deprecated
    public static void mult3( double sa, double[] a, double[] out ) {
        out[0] = sa * a[0];
        out[1] = sa * a[1];
        out[2] = sa * a[2];
    }

    @Deprecated
    public static void cross3( double[] a, int offA, double[] b, int offB, double[] out, int offOut ) {
        out[0+offOut] = a[1+offA] * b[2+offB] - b[1+offB] * a[2+offA];
        out[1+offOut] = a[2+offA] * b[0+offB] - b[2+offB] * a[0+offA];
        out[2+offOut] = a[0+offA] * b[1+offB] - b[0+offB] * a[1+offA];
    }

    @Deprecated
    public static void cross3( double[] a, double[] b, double[] out ) {
        out[0] = a[1] * b[2] - b[1] * a[2];
        out[1] = a[2] * b[0] - b[2] * a[0];
        out[2] = a[0] * b[1] - b[0] * a[1];
    }

    @Deprecated
    public static void cross3( double[] origin, double[] a, double[] b, double[] out ) {
        out[0] = (a[1] - origin[1]) * (b[2] - origin[2]) - (b[1] - origin[1]) * (a[2] - origin[2]);
        out[1] = (a[2] - origin[2]) * (b[0] - origin[0]) - (b[2] - origin[2]) * (a[0] - origin[0]);
        out[2] = (a[0] - origin[0]) * (b[1] - origin[1]) - (b[0] - origin[0]) * (a[1] - origin[1]);
    }

    @Deprecated
    public static double dot3( double[] a, double[] b ) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    @Deprecated
    public static double dot3( double[] origin, double[] a, double[] b ) {
        return (a[0] - origin[0]) * (b[0] - origin[0]) +
               (a[1] - origin[1]) * (b[1] - origin[1]) +
               (a[2] - origin[2]) * (b[2] - origin[2]);
    }

    @Deprecated
    public static double cosAng3( double[] a, double[] b ) {
        return dot3( a, b ) / Math.sqrt( lenSquared3( a ) * lenSquared3( b ) );
    }

    @Deprecated
    public static double cosAng3( double[] origin, double[] a, double[] b ) {
        double ax = a[0] - origin[0];
        double ay = a[1] - origin[1];
        double az = a[2] - origin[2];
        double bx = b[0] - origin[0];
        double by = b[1] - origin[1];
        double bz = b[2] - origin[2];

        double dd = ( ax*ax + ay*ay + az*az ) * ( bx*bx + by*by + bz*bz );
        return ( ax*bx + ay*by + az*bz ) / Math.sqrt( dd );
    }

    @Deprecated
    public static double ang3( double[] a, double[] b ) {
        return Math.acos( cosAng3( a, b ) );
    }

    @Deprecated
    public static double ang3( double[] origin, double[] a, double[] b ) {
        return Math.acos( cosAng3( origin, a, b ) );
    }

    @Deprecated
    public static void lerp3( double[] a, double[] b, double p, double[] out ) {
        double q = 1.0 - p;
        out[0] = q * a[0] + p * b[0];
        out[1] = q * a[1] + p * b[1];
        out[2] = q * a[2] + p * b[2];
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * orthogonal to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    @Deprecated
    public static void reject3( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1] + ref[2] * ref[2];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot3( a, ref ) / lenRef;
        a[0] -= ref[0] * parScale;
        a[1] -= ref[1] * parScale;
        a[2] -= ref[2] * parScale;
    }

    /**
     * Performs smallest possible modification to vector {@code a} to make it
     * parallel to vector {@code ref}.
     *
     * @param a    Vector to modify.
     * @param ref  Reference vector.
     */
    @Deprecated
    public static void project3( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1] + ref[2] * ref[2];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot3( a, ref ) / lenRef;
        a[0] = ref[0] * parScale;
        a[1] = ref[1] * parScale;
        a[2] = ref[2] * parScale;
    }

    /**
     * Finds signed axis-aligned unit-vector nearest to input vector.
     * For example, the nearest axis to [ 0.8, -1.3, 0.1 ] is [ 0.0, -1.0, 0.0 ].
     */
    @Deprecated
    public static void nearestAxis3( double x, double y, double z, double[] out ) {
        double ax = x >= 0 ? x : -x;
        double ay = y >= 0 ? y : -y;
        double az = z >= 0 ? z : -z;

        if( ax > ay && ax > az ) {
            out[0] = x >= 0 ? 1 : -1;
            out[1] = 0;
            out[2] = 0;
        } else if( ay > az ) {
            out[0] = 0;
            out[1] = y >= 0 ? 1 : -1;
            out[2] = 0;
        } else {
            out[0] = 0;
            out[1] = 0;
            out[2] = z >= 0 ? 1 : -1;
        }
    }

    /**
     * Picks a unit-length vector that is orthogonal to the input vector.
     */
    @Deprecated
    public static void chooseOrtho3( double x, double y, double z, double[] out3x1 ) {
        chooseOrtho3( x, y, z, 2, out3x1 );
    }

    /**
     * Picks a unitl-length vector that is orthogonal to the input vector.
     * <p>
     * This method allows the user to define a "zero-dimension", where the
     * vector that is returned by this method is guaranteed to have a zero coordinate
     * for that dimension. Additionally, the coordinate after the zero-dimension will
     * hold a non-negative mVal.
     * <p>
     * For example,
     * {@code chooseOrtho3( 1.0, 1.0, 1.0, 0, out )} <br>
     * will set <br>
     * {@code out = [  0.0000,  0.7071, -0.7071 ] }<br>
     * where {@code out[0]} is zero and {@code out[1]} is non-negative. <br>
     * {@code chooseOrtho3( 1.0, 1.0, 1.0, 2, out )}<br>
     * will set <br>
     * {@code out = [  0.7071, -0.7071,  0.0000 ] }<br>
     *
     * @param x       X-coord of vector.
     * @param y       Y-coord of vector.
     * @param z       Z-coord of vector.
     * @param zeroDim Number {0,1,2} indicating if the output x, y, or z axis should be zero.
     * @param out     Length-3 array to hold output axis.
     */
    @Deprecated
    public static void chooseOrtho3( double x, double y, double z, int zeroDim, double[] out ) {
        switch( zeroDim ) {
        case 2:
            if( y > SQRT_ABS_TOL || -y > SQRT_ABS_TOL ) {
                out[0] = 1;
                out[1] = -x/y;
                out[2] = 0;
            } else if ( x > SQRT_ABS_TOL || -x > SQRT_ABS_TOL ) {
                out[0] = -y/x;
                out[1] = 1;
                out[2] = 0;
            } else {
                out[0] = 1;
                out[1] = 0;
                out[2] = 0;
                // No need to normalize3.
                return;
            }
            break;

        case 1:
            if( x > SQRT_ABS_TOL || -x > SQRT_ABS_TOL ) {
                out[0] = -z / x;
                out[1] = 0;
                out[2] = 1;
            } else if( z > SQRT_ABS_TOL || -z > SQRT_ABS_TOL ) {
                out[0] = 1;
                out[1] = 0;
                out[2] = -x / z;
            } else {
                out[0] = 0;
                out[1] = 0;
                out[2] = 1;
                // No need to normalize3.
                return;
            }
            break;

        default:
            if( z > SQRT_ABS_TOL || -z > SQRT_ABS_TOL ) {
                out[0] = 0;
                out[1] = 1;
                out[2] = -y / z;
            } else if( y > SQRT_ABS_TOL || -y > SQRT_ABS_TOL ) {
                out[0] = 0;
                out[1] = -z / y;
                out[2] = 1;
            } else {
                out[0] = 0;
                out[1] = 1;
                out[2] = 0;
                // No need to normalize3.
                return;
            }
            break;
        }

        normalize3( out );
    }

    /**
     * Returns shortest distance between a line defined and a point.
     * @param n1 A point on the line
     * @param n2 A second point on the line.
     * @param p  Some point
     * @return Distance between <tt>p</tt> and line that passes through <tt>n1</tt> and <tt>n2</tt>.
     */
    @Deprecated
    public static double lineToPointDistance3( double[] n1, double[] n2, double[] p ) {
        double cx = (p[1] - n1[1]) * (p[2] - n2[2]) - (p[1] - n2[1]) * (p[2] - n1[2]);
        double cy = (p[2] - n1[2]) * (p[0] - n2[0]) - (p[2] - n2[2]) * (p[0] - n1[0]);
        double cz = (p[0] - n1[0]) * (p[1] - n2[1]) - (p[0] - n2[0]) * (p[1] - n1[1]);
        return Math.sqrt( cx * cx + cy * cy + cz * cz ) / dist3( n1, n2 );
    }

    /**
     * Returns shortest distance between a line segment and a point.
     *
     * @param n1 Start of line segment
     * @param n2 End of line segment
     * @param p Some point
     * @return smallest distance from point to any point on line segment.
     */
    @Deprecated
    public static double lineSegmentToPointDistance3( double[] n1, double[] n2, double[] p ) {
        double u0 = p[0] - n1[0];
        double u1 = p[1] - n1[1];
        double u2 = p[2] - n1[2];

        double v0 = n2[0] - n1[0];
        double v1 = n2[1] - n1[1];
        double v2 = n2[2] - n1[2];

        double num = u0 * v0 + u1 * v1 + u2 * v2;
        double den = v0 * v0 + v1 * v1 + v2 * v2;
        double t   = num / den;

        if( den < SQRT_ABS_TOL || t <= 0.0 ) {
            //Return distance from n1 to p (which is already stored in u0,u1,u2).
        } else if( t >= 1.0 ) {
            //Return distance from n2 to p.
            u0 = p[0] - n2[0];
            u1 = p[1] - n2[1];
            u2 = p[2] - n2[2];
        } else {
            //Make u perpendicular to line.
            u0 -= v0 * t;
            u1 -= v1 * t;
            u2 -= v2 * t;
        }

        return Math.sqrt( u0 * u0 + u1 * u1 + u2 * u2 );
    }

    /**
     * Finds intersection of line and plane. The intersection point is
     * only defined if this method returns 1, indicating that the line and
     * plane intersect at one point.
     *
     * @param line0      First point on line.
     * @param line1      Second point on line.
     * @param planePoint A Point on the plane.
     * @param planeNorm  Normal vector of plane.
     * @param optOut     length-3 array to hold point of intersection. May be {@code null}
     * @return 0 if no intersection,
     *         1 if point intersection ( line crosses plane )
     *         2 if line intersection ( line lies on plane )
     */
    @Deprecated
    public static int intersectLinePlane3( double[] line0,
                                           double[] line1,
                                           double[] planePoint,
                                           double[] planeNorm,
                                           double[] optOut )
    {
        double dx = line1[0] - line0[0];
        double dy = line1[1] - line0[1];
        double dz = line1[2] - line0[2];

        double num = (planePoint[0] - line0[0]) * planeNorm[0] +
                     (planePoint[1] - line0[1]) * planeNorm[1] +
                     (planePoint[2] - line0[2]) * planeNorm[2];

        double den = dx * planeNorm[0] + dy * planeNorm[1] + dz * planeNorm[2];

        if( den < SQRT_ABS_TOL && -den < SQRT_ABS_TOL ) {
            return num < SQRT_ABS_TOL && -num < SQRT_ABS_TOL ? 2 : 0;
        }

        if( optOut != null ) {
            double d = num / den;
            optOut[0] = d * dx + line0[0];
            optOut[1] = d * dy + line0[1];
            optOut[2] = d * dz + line0[2];
        }

        return 1;
    }

    /**
     * Finds "intersection", or smallest connecting line, between two 3d lines.
     * (Lines, not line segments).
     *
     * @param a0       First point on line a
     * @param a1       Second point on line a
     * @param b0       First point on line b
     * @param b1       Second point on line b
     * @param optOutA  On return, holds point on line a nearest to line b (optional)
     * @param optOutB  On return, holds point on line b nearest to line a (optional)
     * @return true if lines are not parallel and intersection was found.  False if lines are parallel.
     */
    @Deprecated
    public static boolean intersectLineLine3( double[] a0,
                                              double[] a1,
                                              double[] b0,
                                              double[] b1,
                                              double[] optOutA,
                                              double[] optOutB )
    {
        double da0b0b1b0 = (a0[0] - b0[0]) * (b1[0] - b0[0]) + (a0[1] - b0[1]) * (b1[1] - b0[1]) + (a0[2] - b0[2]) * (b1[2] - b0[2]);
        double db1b0a1a0 = (b1[0] - b0[0]) * (a1[0] - a0[0]) + (b1[1] - b0[1]) * (a1[1] - a0[1]) + (b1[2] - b0[2]) * (a1[2] - a0[2]);
        double da0b0a1a0 = (a0[0] - b0[0]) * (a1[0] - a0[0]) + (a0[1] - b0[1]) * (a1[1] - a0[1]) + (a0[2] - b0[2]) * (a1[2] - a0[2]);
        double db1b0b1b0 = (b1[0] - b0[0]) * (b1[0] - b0[0]) + (b1[1] - b0[1]) * (b1[1] - b0[1]) + (b1[2] - b0[2]) * (b1[2] - b0[2]);
        double da1a0a1a0 = (a1[0] - a0[0]) * (a1[0] - a0[0]) + (a1[1] - a0[1]) * (a1[1] - a0[1]) + (a1[2] - a0[2]) * (a1[2] - a0[2]);

        double num = da0b0b1b0 * db1b0a1a0 - da0b0a1a0 * db1b0b1b0;
        double den = da1a0a1a0 * db1b0b1b0 - db1b0a1a0 * db1b0a1a0;

        if( den < SQRT_ABS_TOL && -den < SQRT_ABS_TOL ) {
            return false;
        }

        double mua = num / den;
        if( optOutA != null ) {
            multAdd3( 1.0 - mua, a0, mua, a1, optOutA );
        }
        if( optOutB != null ) {
            double mub = ( da0b0b1b0 + mua * db1b0a1a0 ) / db1b0b1b0;
            multAdd3( 1.0 - mub, b0, mub, b1, optOutB );
        }

        return true;
    }

    @Deprecated
    public static boolean isNaN3( double[] vec ) {
        return vec[0] != vec[0] ||
               vec[1] != vec[1] ||
               vec[2] != vec[2];

    }

    @Deprecated
    public static String format3( double[] vec ) {
        return String.format( FORMAT3, vec[0], vec[1], vec[2] );
    }



    //== DOUBLE[4] Functions =======================================================

    @Deprecated
    public static void put4( double x, double y, double z, double w, double[] out ) {
        out[0] = x;
        out[1] = y;
        out[2] = z;
        out[3] = w;
    }

    @Deprecated
    public static void put4( Vec4 v, double[] out ) {
        out[0] = v.x;
        out[1] = v.y;
        out[2] = v.z;
        out[3] = v.w;
    }

    @Deprecated
    public static void put4( double[] v, double[] out ) {
        out[0] = v[0];
        out[1] = v[1];
        out[2] = v[2];
        out[3] = v[3];
    }

    @Deprecated
    public static void addTo4( double[] a, double[] out ) {
        out[0] += a[0];
        out[1] += a[1];
        out[2] += a[2];
        out[3] += a[3];
    }

    @Deprecated
    public static void subtract4( double[] a, double[] b, double[] out ) {
        out[0] = a[0] - b[0];
        out[1] = a[1] - b[1];
        out[2] = a[2] - b[2];
        out[3] = a[3] - b[3];
    }

    @Deprecated
    public static void subtractFrom4( double[] a, double[] out ) {
        out[0] -= a[0];
        out[1] -= a[1];
        out[2] -= a[2];
        out[3] -= a[3];
    }

    @Deprecated
    public static void multAdd4( double sa, double[] a, double sb, double[] b, double[] out ) {
        out[0] = a[0] * sa + b[0] * sb;
        out[1] = a[1] * sa + b[1] * sb;
        out[2] = a[2] * sa + b[2] * sb;
        out[3] = a[3] * sa + b[3] * sb;
    }

    @Deprecated
    public static void multAddTo4( double sa, double[] a, double sOut, double[] out ) {
        out[0] = sOut * out[0] + sa * a[0];
        out[1] = sOut * out[1] + sa * a[1];
        out[2] = sOut * out[2] + sa * a[2];
        out[3] = sOut * out[3] + sa * a[3];
    }

    @Deprecated
    public static double len4( double[] a ) {
        return Math.sqrt( lenSquared4( a ) );
    }

    @Deprecated
    public static double lenSquared4( double[] a ) {
        return a[0] * a[0] + a[1] * a[1] + a[2] * a[2] + a[3] * a[3];
    }

    @Deprecated
    public static void min4( double[]a, double[] b, double[] out ) {
        out[0] = a[0] <= b[0] ? a[0] : b[0];
        out[1] = a[1] <= b[1] ? a[1] : b[1];
        out[2] = a[2] <= b[2] ? a[2] : b[2];
        out[3] = a[3] <= b[3] ? a[3] : b[3];
    }

    @Deprecated
    public static void max4( double[] a, double[] b, double[] out ) {
        out[0] = a[0] >= b[0] ? a[0] : b[0];
        out[1] = a[1] >= b[1] ? a[1] : b[1];
        out[2] = a[2] >= b[2] ? a[2] : b[2];
        out[3] = a[3] >= b[3] ? a[3] : b[3];
    }

    @Deprecated
    public static double dist4( double[] a, double[] b ) {
        return Math.sqrt( distSquared4( a, b ) );
    }

    @Deprecated
    public static double distSquared4( double[] a, double[] b ) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        double dz = a[2] - b[2];
        double dw = a[3] - b[3];
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    @Deprecated
    public static void normalize4( double[] a ) {
        double s = 1.0 / len4( a );
        a[0] *= s;
        a[1] *= s;
        a[2] *= s;
        a[3] *= s;
    }

    @Deprecated
    public static void normalize4( double[] a, double normLength, double[] out ) {
        double d = normLength / len4( a );
        out[0] = a[0] * d;
        out[1] = a[1] * d;
        out[2] = a[2] * d;
        out[3] = a[3] * d;
    }

    @Deprecated
    public static void mult4( double sa, double[] a ) {
        a[0] *= sa;
        a[1] *= sa;
        a[2] *= sa;
        a[3] *= sa;
    }

    @Deprecated
    public static void mult4( double sa, double[] a, double[] out ) {
        out[0] = sa * a[0];
        out[1] = sa * a[1];
        out[2] = sa * a[2];
        out[3] = sa * a[3];
    }

    @Deprecated
    public static double dot4( double[] a, double[] b ) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3];
    }

    @Deprecated
    public static double dot4( double[] origin, double[] a, double[] b ) {
        return ( a[0] - origin[0] ) * ( b[0] - origin[0] ) +
               ( a[1] - origin[1] ) * ( b[1] - origin[1] ) +
               ( a[2] - origin[2] ) * ( b[2] - origin[2] ) +
               ( a[3] - origin[3] ) * ( b[3] - origin[3] );
    }

    @Deprecated
    public static double cosAng4( double[] a, double[] b ) {
        return dot4( a, b ) / Math.sqrt( lenSquared4( a ) * lenSquared4( b ) );
    }

    @Deprecated
    public static double cosAng4( double[] origin, double[] a, double[] b ) {
        double ax = a[0] - origin[0];
        double ay = a[1] - origin[1];
        double az = a[2] - origin[2];
        double aw = a[3] - origin[3];
        double bx = b[0] - origin[0];
        double by = b[1] - origin[1];
        double bz = b[2] - origin[2];
        double bw = b[3] - origin[3];

        double dd = ( ax*ax + ay*ay + az*az + aw*aw ) * ( bx*bx + by*by + bz*bz + bw*bw );
        return ( ax*bx + ay*by + az*bz + aw*bw ) / Math.sqrt( dd );
    }

    @Deprecated
    public static double ang4( double[] a, double[] b ) {
        return Math.acos( cosAng4( a, b ) );
    }

    @Deprecated
    public static double ang4( double[] origin, double[] a, double[] b ) {
        return Math.acos( cosAng4( origin, a, b ) );
    }

    @Deprecated
    public static void lerp4( double[] a, double[] b, double p, double[] out ) {
        double q = 1.0 - p;
        out[0] = q * a[0] + p * b[0];
        out[1] = q * a[1] + p * b[1];
        out[2] = q * a[2] + p * b[2];
        out[3] = q * a[3] + p * b[3];
    }

    @Deprecated
    public static String format4( double[] vec ) {
        return String.format( FORMAT4, vec[0], vec[1], vec[2], vec[3] );
    }



    //== Other Functions =======================================================
    

    private Vec() {}


    /**
     * @deprecated 
     * Use {@link #reject(Vec2, Vec2)}
     */
    @Deprecated
    public static void makeOrthoTo( Vec2 a, Vec2 ref ) {
        float lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
    }

    /**
     * @deprecated
     * Use {@link #project(Vec2, Vec2)}
     */
    @Deprecated
    public static void makeParallelTo( Vec2 a, Vec2 ref ) {
        float lenRef = ref.x * ref.x + ref.y * ref.y;
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
    }


    /**
     * @deprecated
     * Use {@link #reject(Vec3, Vec3)}
     */
    @Deprecated
    public static void makeOrthoTo( Vec3 a, Vec3 ref ) {
        float lenRef = lenSquared( ref );
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x -= ref.x * parScale;
        a.y -= ref.y * parScale;
        a.z -= ref.z * parScale;
    }

    /**
     * @deprecated
     * Use {@link #project(Vec3, Vec3)}
     */
    @Deprecated
    public static void makeParallelTo( Vec3 a, Vec3 ref ) {
        float lenRef = lenSquared( ref );
        if( lenRef < FSQRT_ABS_TOL ) {
            return;
        }
        float parScale = dot( a, ref ) / lenRef;
        a.x = ref.x * parScale;
        a.y = ref.y * parScale;
        a.z = ref.z * parScale;
    }

    /**
     * @deprecated
     * Use {@link #reject2(double[], double[])}
     */
    @Deprecated
    public static void makeOrthoTo2( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot2( a, ref ) / lenRef;
        a[0] -= ref[0] * parScale;
        a[1] -= ref[1] * parScale;
    }

    /**
     * @deprecated
     * Use {@link #project2(double[], double[])}
     */
    @Deprecated
    public static void makeParallelTo2( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot2( a, ref ) / lenRef;
        a[0] = ref[0] * parScale;
        a[1] = ref[1] * parScale;
    }

    /**
     * @deprecated
     * Use {@link #reject3(double[], double[])}
     */
    @Deprecated
    public static void makeOrthoTo3( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1] + ref[2] * ref[2];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot3( a, ref ) / lenRef;
        a[0] -= ref[0] * parScale;
        a[1] -= ref[1] * parScale;
        a[2] -= ref[2] * parScale;
    }

    /**
     * @deprecated
     * Use {@link #project3(double[], double[])}
     */
    @Deprecated
    public static void makeParallelTo3( double[] a, double[] ref ) {
        double lenRef = ref[0] * ref[0] + ref[1] * ref[1] + ref[2] * ref[2];
        if( lenRef < SQRT_ABS_TOL ) {
            return;
        }
        double parScale = dot3( a, ref ) / lenRef;
        a[0] = ref[0] * parScale;
        a[1] = ref[1] * parScale;
        a[2] = ref[2] * parScale;
    }


}
