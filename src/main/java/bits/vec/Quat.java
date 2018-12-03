/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;


import java.util.NoSuchElementException;
import java.util.Random;


/**
 * Methods for quaternions.
 *
 * @author decamp
 */
public final class Quat {

    public float x;
    public float y;
    public float z;
    public float w;

    
    public Quat() {}


    public Quat( float x, float y, float z, float w ) {
        set( x, y, z, w );
    }

    
    public Quat( Quat copy ) {
        set( copy );
    }

    

    public float el( int d ) {
        switch( d ) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        case 3: return w;
        default:
            throw new NoSuchElementException();
        }
    }


    public void el( int d, float v ) {
        switch( d ) {
        case 0: x = v; return;
        case 1: y = v; return;
        case 2: z = v; return;
        case 3: w = v; return;
        default:
            throw new NoSuchElementException();
        }
    }


    public void set( float x, float y, float z, float w ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    public void set( double x, double y, double z, double w ) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        this.w = (float)w;
    }


    public void set( Quat copy ) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
        this.w = copy.w;
    }


    public void set( Quatd copy ) {
        this.x = (float)copy.x;
        this.y = (float)copy.y;
        this.z = (float)copy.z;
        this.w = (float)copy.w;
    }



    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Quat ) ) {
            return false;
        }

        Quat v = (Quat)obj;
        // v == this is necessary to catch NaNs.
        return v == this || x == v.x && y == v.y && z == v.z && w == v.w;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits( x + y + z + w );
    }

    @Override
    public String toString() {
        return String.format( Vec.FORMAT4, x, y, z, w);
    }    
    
    
    
    //=== Quat Functions =======================================================
        

    public static void conjugate( Quat a, Quat out ) {
        out.x = -a.x;
        out.y = -a.y;
        out.z = -a.z;
        out.w =  a.w;
    }
  
    
    public static void invert( Quat a, Quat out ) {
        float s = 1 / ( a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w );
        out.x = -a.x * s;
        out.y = -a.y * s;
        out.z = -a.z * s;
        out.w =  a.w * s;
    }


    public static float len( Quat a ) {
        return (float)Math.sqrt( lenSquared( a ) );
    }


    public static float lenSquared( Quat a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w;
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    public static void matToQuat( Mat3 mat, Quat out ) {
        final float m00 = mat.m00;
        final float m11 = mat.m11;
        final float m22 = mat.m22;
        float tr = m00 + m11 + m22;
                
        if( tr > 0 ) {
            float sc = (float)Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;
        
        } else if( (m00 > m11) & (m00 > m22) ) {
            float sc = (float)Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;
        
        } else if( m11 > m22 ) {
            float sc = (float)Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;
        
        } else {
            float sc = (float)Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    public static void matToQuat( Mat4 mat, Quat out ) {
        final float m00 = mat.m00;
        final float m11 = mat.m11;
        final float m22 = mat.m22;
        float tr = m00 + m11 + m22;

        if( tr > 0 ) {
            float sc = (float)Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            float sc = (float)Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;

        } else if( m11 > m22 ) {
            float sc = (float)Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;

        } else {
            float sc = (float)Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }
    
    }

    /**
     * Multiply two quaternions.
     * @param a   Input quaternion
     * @param b   Input quaternion
     * @param out Receives product quaternion. May be same object as either input.
     */
    public static void mult( Quat a, Quat b, Quat out ) {
        // These local copies had no effect in performance tests, but whatevs.
        final float ax = a.x;
        final float ay = a.y;
        final float az = a.z;
        final float aw = a.w;
        final float bx = b.x;
        final float by = b.y;
        final float bz = b.z;                          
        final float bw = b.w;                          
                                                       
        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiply quaternion and vector.
     *
     * @param quat Input quaternion.
     * @param vec  Input vector.
     * @param out  Output vector. May be same as either input.
     */
    public static void multVec( Quat quat, Vec3 vec, Vec3 out ) {
        final float qx = quat.x;
        final float qy = quat.y;
        final float qz = quat.z;
        final float qw = quat.w;
        final float vx = vec.x;
        final float vy = vec.y;
        final float vz = vec.z;

        out.x = ( qw * qw + qx * qx - qy * qy - qz * qz ) * vx +
                ( 2  * ( qx * qy - qw * qz ) )            * vy +
                ( 2  * ( qx * qz + qw * qy ) )            * vz;

        out.y = ( 2 * ( qx * qy + qw * qz ) )             * vx +
                ( qw * qw - qx * qx + qy * qy - qz * qz ) * vy +
                ( 2 * ( qy * qz - qw * qx ) )             * vz;

        out.z = ( 2 * ( qx * qz - qw * qy ) )             * vx +
                ( 2 * ( qy * qz + qw * qx ) )             * vy +
                ( qw * qw - qx * qx - qy * qy + qz * qz ) * vz;
    }


    public static boolean near( Quat a, Quat b ) {
        return
            Tol.near( a.x, b.x ) &&
            Tol.near( a.y, b.y ) &&
            Tol.near( a.z, b.z ) &&
            Tol.near( a.w, b.w );
    }


    public static boolean near( Quat a, Quat b, float relErr, float absErr ) {
        return
            Tol.equal( a.x, b.x, relErr, absErr ) &&
            Tol.equal( a.y, b.y, relErr, absErr ) &&
            Tol.equal( a.z, b.z, relErr, absErr ) &&
            Tol.equal( a.w, b.w, relErr, absErr );
    }

    /**
     * Normalize quaternion to a valid unit-length.
     */
    public static void normalize( Quat q ) {
        float s = 1f / len( q );
        q.x *= s;
        q.y *= s;
        q.z *= s;
        q.w *= s;
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param rand0 Arbitrary val in [0,1]
     * @param rand1 Arbitrary val in [0,1]
     * @param rand2 Arbitrary val in [0,1]
     * @param out   Holds quaternion on return.
     */
    public static void randToQuat( float rand0, float rand1, float rand2, Quat out ) {
        float sign0 = 1f;
        float sign1 = 1f;
        float sign2 = 1f;

        // Sort three numbers.
        // Use the sort order to generate 3 random booleans for mSign values.
        if( rand0 > rand1 ) {
            sign0 = -1;
            sign1 = -1;
            float swap = rand0;
            rand0 = rand1;
            rand1 = swap;
        }
        if( rand1 > rand2 ) {
            sign1 = -sign1;
            sign2 = -sign2;
            float swap = rand1;
            rand1 = rand2;
            rand2 = swap;

            if( rand0 > rand1 ) {
                sign0 = -sign0;
                sign1 = -sign1;
                swap  = rand0;
                rand0 = rand1;
                rand1 = swap;
            }
        }

        out.x = sign0 * (rand0        );
        out.y = sign1 * (rand1 - rand0);
        out.z = sign2 * (rand2 - rand1);
        out.w = 1.0f - rand2;
        normalize( out );
    }

    /**
     * Converts axis-rotation to quaternion representation.
     * @param rads Radians of rotation
     * @param x    X-coord of rotation axis
     * @param y    Y-coord of rotation axis
     * @param z    Z-coord of rotation axis
     * @param out  Length-4 array that holds rotation on return.
     */
    public static void getRotation( float rads, float x, float y, float z, Quat out ) {
        float cos = (float)Math.cos( rads * 0.5 );
        float len = (float)Math.sqrt( x * x + y * y + z * z );
        float sin = (float)Math.sin( rads * 0.5 ) / len;
        out.x = sin * x;
        out.y = sin * y;
        out.z = sin * z;
        out.w = cos;
    }

    
    public static void rotationBetweenUnitVecs( Vec3 src, Vec3 dst, Quat out ) {
        float r = Vec.dot( src, dst ) + 1;
        
        if( r < 0.000001f ) {
            r = 0;
            if( Math.abs( src.x ) > Math.abs( src.z ) ) {
                out.x = -src.y;
                out.y = src.x;
                out.z = 0;
                out.w = 0;
            } else {
                out.x = 0;
                out.y = -src.z;
                out.z = src.y;
                out.w = 0;
            }

        } else {
            out.x = src.y * dst.z - dst.y * src.z;
            out.y = src.z * dst.x - dst.z * src.x;
            out.z = src.x * dst.y - dst.x * src.y;
            out.w = r;
        }
                
        normalize( out );
    }
    
    /**
     * Multiplies quaternion with axis-rotation.
     *
     * @param q    Input quaternion.
     * @param rads Degree of  rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param out  receives output
     */
    public static void rotate( Quat q, float rads, float x, float y, float z, Quat out ) {
        final float ax = q.x;
        final float ay = q.y;
        final float az = q.z;
        final float aw = q.w;
        
        float cos = (float)Math.cos( rads * 0.5 );
        float len = (float)Math.sqrt( x * x + y * y + z * z );
        float sin = (float)Math.sin( rads * 0.5 ) / len;
        float bx = sin * x;
        float by = sin * y;
        float bz = sin * z;
        float bw = cos;
        
        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiplies axis-rotation with quaternion.
     *
     * @param rads Degree of rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param q    Input quaternion.
     * @param out  receives output
     */
    public static void preRotate( float rads, float x, float y, float z, Quat q, Quat out ) {
        final float cos = (float)Math.cos( rads * 0.5 );
        final float len = (float)Math.sqrt( x * x + y * y + z * z );
        final float sin = (float)Math.sin( rads * 0.5 ) / len;
        final float ax = sin * x;
        final float ay = sin * y;
        final float az = sin * z;
        final float aw = cos;
        final float bx = q.x;
        final float by = q.y;
        final float bz = q.z;
        final float bw = q.w;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }
    
    
    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    public static void quatToMat( Quat quat, Mat3 out ) {
        final float x = quat.x;
        final float y = quat.y;
        final float z = quat.z;
        final float w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
    }

    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    public static void quatToMat( Quat quat, Mat4 out ) {
        final float x = quat.x;
        final float y = quat.y;
        final float z = quat.z;
        final float w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m30 = 0;
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m31 = 0;
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
        out.m32 = 0;
        out.m03 = 0;
        out.m13 = 0;
        out.m23 = 0;
        out.m33 = 1;
    }
    
    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param out   Holds output quaternion on return.
     */
    public static void sampleUniform( Random rand, Quat out ) {
        randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), out );
    }

    /**
     * Computes spherical interpolation between two quaternions.
     * @param qa  Quaternior
     * @param qb  Quaternion
     * @param t   Blend factor
     * @param out Length-4 vec to receive output.
     */
    public static void slerp( Quat qa, Quat qb, float t, Quat out ) {
        // Calculate angle between them.
        float cosHalfTheta = qa.x * qb.x + qa.y * qb.y + qa.z * qb.z + qa.w * qb.w;

        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if( cosHalfTheta >= 1.0 || cosHalfTheta <= -1.0 ) {
            out.set( qa );
            return;
        }

        // Calculate temporary values.
        float halfTheta = (float)Math.acos( cosHalfTheta );
        float sinHalfTheta = (float)Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if( sinHalfTheta < 0.00001f && sinHalfTheta > -0.00001f ) {
            out.x = ( qa.x * 0.5f + qb.x * 0.5f );
            out.y = ( qa.y * 0.5f + qb.y * 0.5f );
            out.z = ( qa.z * 0.5f + qb.z * 0.5f );
            out.w = ( qa.w * 0.5f + qb.w * 0.5f );
            return;
        }

        float ratioA = (float)Math.sin( (1 - t) * halfTheta ) / sinHalfTheta;
        float ratioB = (float)Math.sin( t * halfTheta ) / sinHalfTheta;
        //calculate Quaternion.
        out.x = ( qa.x * ratioA + qb.x * ratioB );
        out.y = ( qa.y * ratioA + qb.y * ratioB );
        out.z = ( qa.z * ratioA + qb.z * ratioB );
        out.w = ( qa.w * ratioA + qb.w * ratioB );
    }
    
    /**
     * Formats quaternion for printing.
     */
    @Deprecated
    public static String format( Quat quat ) {
        return quat.toString();
    }


    //=== QUATD Functions =======================================================


    public static void conjugate( Quatd a, Quatd out ) {
        out.x = -a.x;
        out.y = -a.y;
        out.z = -a.z;
        out.w =  a.w;
    }

    /**
     * Formats quaternion for printing.
     */
    @Deprecated
    public static String format( Quatd quat ) {
        return quat.toString();
    }


    public static void invert( Quatd a, Quatd out ) {
        double s = 1 / ( a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w );
        out.x = -a.x * s;
        out.y = -a.y * s;
        out.z = -a.z * s;
        out.w =  a.w * s;
    }

    
    public static double len( Quatd a ) {
        return Math.sqrt( lenSquared( a ) );
    }


    public static double lenSquared( Quatd a ) {
        return a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w;
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    public static void matToQuat( Mat3d mat, Quatd out ) {
        final double m00 = mat.m00;
        final double m11 = mat.m11;
        final double m22 = mat.m22;
        double tr = m00 + m11 + m22;

        if( tr > 0 ) {
            double sc = Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            double sc = Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;

        } else if( m11 > m22 ) {
            double sc = Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;

        } else {
            double sc = Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    public static void matToQuat( Mat4d mat, Quatd out ) {
        final double m00 = mat.m00;
        final double m11 = mat.m11;
        final double m22 = mat.m22;
        double tr = m00 + m11 + m22;

        if( tr > 0 ) {
            double sc = Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            double sc = Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;

        } else if( m11 > m22 ) {
            double sc = Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;

        } else {
            double sc = Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }

    }

    /**
     * Multiply two quaternions.
     * @param a   Input quaternion
     * @param b   Input quaternion
     * @param out Receives product quaternion. May be same object as either input.
     */
    public static void mult( Quatd a, Quatd b, Quatd out ) {
        // These local copies had no effect in performance tests, but whatevs.
        final double ax = a.x;
        final double ay = a.y;
        final double az = a.z;
        final double aw = a.w;
        final double bx = b.x;
        final double by = b.y;
        final double bz = b.z;
        final double bw = b.w;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiply quaternion and vector.
     *
     * @param quat Input quaternion.
     * @param vec  Input vector.
     * @param out  Output vector. May be same as either input.
     */
    public static void multVec( Quatd quat, Vec3d vec, Vec3d out ) {
        final double qx = quat.x;
        final double qy = quat.y;
        final double qz = quat.z;
        final double qw = quat.w;
        final double vx = vec.x;
        final double vy = vec.y;
        final double vz = vec.z;

        out.x = ( qw * qw + qx * qx - qy * qy - qz * qz ) * vx +
                ( 2  * ( qx * qy - qw * qz ) )            * vy +
                ( 2  * ( qx * qz + qw * qy ) )            * vz;

        out.y = ( 2 * ( qx * qy + qw * qz ) )             * vx +
                ( qw * qw - qx * qx + qy * qy - qz * qz ) * vy +
                ( 2 * ( qy * qz - qw * qx ) )             * vz;

        out.z = ( 2 * ( qx * qz - qw * qy ) )             * vx +
                ( 2 * ( qy * qz + qw * qx ) )             * vy +
                ( qw * qw - qx * qx - qy * qy + qz * qz ) * vz;
    }

    /**
     * Normalize quaternion to a valid unit-length.
     */
    public static void normalize( Quatd q ) {
        double s = 1.0 / len( q );
        q.x *= s;
        q.y *= s;
        q.z *= s;
        q.w *= s;
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param rand0 Arbitrary val in [0,1]
     * @param rand1 Arbitrary val in [0,1]
     * @param rand2 Arbitrary val in [0,1]
     * @param out   Holds quaternion on return.
     */
    public static void randToQuat( double rand0, double rand1, double rand2, Quatd out ) {
        double sign0 = 1f;
        double sign1 = 1f;
        double sign2 = 1f;

        // Sort three numbers.
        // Use the sort order to generate 3 random booleans for mSign values.
        if( rand0 > rand1 ) {
            sign0 = -1;
            sign1 = -1;
            double swap = rand0;
            rand0 = rand1;
            rand1 = swap;
        }
        if( rand1 > rand2 ) {
            sign1 = -sign1;
            sign2 = -sign2;
            double swap = rand1;
            rand1 = rand2;
            rand2 = swap;

            if( rand0 > rand1 ) {
                sign0 = -sign0;
                sign1 = -sign1;
                swap  = rand0;
                rand0 = rand1;
                rand1 = swap;
            }
        }

        out.x = sign0 * (rand0        );
        out.y = sign1 * (rand1 - rand0);
        out.z = sign2 * (rand2 - rand1);
        out.w = 1.0f - rand2;
        normalize( out );
    }

    /**
     * Converts axis-rotation to quaternion representation.
     * @param rads Radians of rotation
     * @param x    X-coord of rotation axis
     * @param y    Y-coord of rotation axis
     * @param z    Z-coord of rotation axis
     * @param out  Length-4 array that holds rotation on return.
     */
    public static void getRotation( double rads, double x, double y, double z, Quatd out ) {
        double cos = Math.cos( rads * 0.5 );
        double len = Math.sqrt( x * x + y * y + z * z );
        double sin = Math.sin( rads * 0.5 ) / len;
        out.x = sin * x;
        out.y = sin * y;
        out.z = sin * z;
        out.w = cos;
    }


    public static void rotationBetweenUnitVecs( Vec3d src, Vec3d dst, Quatd out ) {
        double r = Vec.dot( src, dst ) + 1;

        if( r < 0.000001f ) {
            r = 0;
            if( Math.abs( src.x ) > Math.abs( src.z ) ) {
                out.x = -src.y;
                out.y = src.x;
                out.z = 0;
                out.w = 0;
            } else {
                out.x = 0;
                out.y = -src.z;
                out.z = src.y;
                out.w = 0;
            }

        } else {
            out.x = src.y * dst.z - dst.y * src.z;
            out.y = src.z * dst.x - dst.z * src.x;
            out.z = src.x * dst.y - dst.x * src.y;
            out.w = r;
        }

        normalize( out );
    }

    /**
     * Multiplies quaternion with axis-rotation.
     *
     * @param q    Input quaternion.
     * @param rads Degree of  rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param out  receives output
     */
    public static void rotate( Quatd q, double rads, double x, double y, double z, Quatd out ) {
        final double ax = q.x;
        final double ay = q.y;
        final double az = q.z;
        final double aw = q.w;

        double cos = Math.cos( rads * 0.5 );
        double len = Math.sqrt( x * x + y * y + z * z );
        double sin = Math.sin( rads * 0.5 ) / len;
        double bx = sin * x;
        double by = sin * y;
        double bz = sin * z;
        double bw = cos;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiplies axis-rotation with quaternion.
     *
     * @param rads Degree of rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param q    Input quaternion.
     * @param out  receives output
     */
    public static void preRotate( double rads, double x, double y, double z, Quatd q, Quatd out ) {
        final double cos = Math.cos( rads * 0.5 );
        final double len = Math.sqrt( x * x + y * y + z * z );
        final double sin = Math.sin( rads * 0.5 ) / len;
        final double ax = sin * x;
        final double ay = sin * y;
        final double az = sin * z;
        final double aw = cos;
        final double bx = q.x;
        final double by = q.y;
        final double bz = q.z;
        final double bw = q.w;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }


    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    public static void quatToMat( Quatd quat, Mat3d out ) {
        final double x = quat.x;
        final double y = quat.y;
        final double z = quat.z;
        final double w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
    }

    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    public static void quatToMat( Quatd quat, Mat4d out ) {
        final double x = quat.x;
        final double y = quat.y;
        final double z = quat.z;
        final double w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m30 = 0;
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m31 = 0;
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
        out.m32 = 0;
        out.m03 = 0;
        out.m13 = 0;
        out.m23 = 0;
        out.m33 = 1;
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param out   Holds output quaternion on return.
     */
    public static void sampleUniform( Random rand, Quatd out ) {
        randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), out );
    }

    /**
     * Computes spherical interpolation between two quaternions.
     * @param qa  Quaternior
     * @param qb  Quaternion
     * @param t   Blend factor
     * @param out Length-4 vec to receive output.
     */
    public static void slerp( Quatd qa, Quatd qb, double t, Quatd out ) {
        // Calculate angle between them.
        double cosHalfTheta = qa.x * qb.x + qa.y * qb.y + qa.z * qb.z + qa.w * qb.w;

        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if( cosHalfTheta >= 1.0 || cosHalfTheta <= -1.0 ) {
            out.set( qa );
            return;
        }

        // Calculate temporary values.
        double halfTheta = Math.acos( cosHalfTheta );
        double sinHalfTheta = Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if( sinHalfTheta < 0.00001f && sinHalfTheta > -0.00001f ) {
            out.x = ( qa.x * 0.5f + qb.x * 0.5f );
            out.y = ( qa.y * 0.5f + qb.y * 0.5f );
            out.z = ( qa.z * 0.5f + qb.z * 0.5f );
            out.w = ( qa.w * 0.5f + qb.w * 0.5f );
            return;
        }

        double ratioA = Math.sin( (1 - t) * halfTheta ) / sinHalfTheta;
        double ratioB = Math.sin( t * halfTheta ) / sinHalfTheta;
        //calculate Quaternion.
        out.x = ( qa.x * ratioA + qb.x * ratioB );
        out.y = ( qa.y * ratioA + qb.y * ratioB );
        out.z = ( qa.z * ratioA + qb.z * ratioB );
        out.w = ( qa.w * ratioA + qb.w * ratioB );
    }




    //=== Vec4 Functions =======================================================

    @Deprecated
    public static void conjugate( Vec4 a, Vec4 out ) {
        out.x = -a.x;
        out.y = -a.y;
        out.z = -a.z;
        out.w =  a.w;
    }

    /**
     * Formats quaternion for printing.
     */
    @Deprecated
    public static String format( Vec4 quat ) {
        return Vec.format( quat );
    }

    @Deprecated
    public static void invert( Vec4 a, Vec4 out ) {
        float s = 1 / ( a.x * a.x + a.y * a.y + a.z * a.z + a.w * a.w );
        out.x = -a.x * s;
        out.y = -a.y * s;
        out.z = -a.z * s;
        out.w =  a.w * s;
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    @Deprecated
    public static void matToQuat( Mat3 mat, Vec4 out ) {
        final float m00 = mat.m00;
        final float m11 = mat.m11;
        final float m22 = mat.m22;
        float tr = m00 + m11 + m22;

        if( tr > 0 ) {
            float sc = (float)Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            float sc = (float)Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;

        } else if( m11 > m22 ) {
            float sc = (float)Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;

        } else {
            float sc = (float)Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Input matrix.
     * @param out Receives output quaternion
     */
    @Deprecated
    public static void matToQuat( Mat4 mat, Vec4 out ) {
        final float m00 = mat.m00;
        final float m11 = mat.m11;
        final float m22 = mat.m22;
        float tr = m00 + m11 + m22;

        if( tr > 0 ) {
            float sc = (float)Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out.x = (mat.m21 - mat.m12) / sc;
            out.y = (mat.m02 - mat.m20) / sc;
            out.z = (mat.m10 - mat.m01) / sc;
            out.w = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            float sc = (float)Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out.x = 0.25f * sc;
            out.y = (mat.m01 + mat.m10) / sc;
            out.z = (mat.m02 + mat.m20) / sc;
            out.w = (mat.m21 - mat.m12) / sc;

        } else if( m11 > m22 ) {
            float sc = (float)Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out.x = (mat.m01 + mat.m10) / sc;
            out.y = 0.25f * sc;
            out.z = (mat.m12 + mat.m21) / sc;
            out.w = (mat.m02 - mat.m20) / sc;

        } else {
            float sc = (float)Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out.x = (mat.m02 + mat.m20) / sc;
            out.y = (mat.m12 + mat.m21) / sc;
            out.z = 0.25f * sc;
            out.w = (mat.m10 - mat.m01) / sc;
        }

    }

    /**
     * Multiply two quaternions.
     * @param a   Input quaternion
     * @param b   Input quaternion
     * @param out Receives product quaternion. May be same object as either input.
     */
    @Deprecated
    public static void mult( Vec4 a, Vec4 b, Vec4 out ) {
        // These local copies had no effect in performance tests, but whatevs.
        final float ax = a.x;
        final float ay = a.y;
        final float az = a.z;
        final float aw = a.w;
        final float bx = b.x;
        final float by = b.y;
        final float bz = b.z;
        final float bw = b.w;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiply quaternion and vector.
     *
     * @param quat Input quaternion.
     * @param vec  Input vector.
     * @param out  Output vector. May be same as either input.
     */
    @Deprecated
    public static void multVec( Vec4 quat, Vec3 vec, Vec3 out ) {
        final float qx = quat.x;
        final float qy = quat.y;
        final float qz = quat.z;
        final float qw = quat.w;
        final float vx = vec.x;
        final float vy = vec.y;
        final float vz = vec.z;

        out.x = ( qw * qw + qx * qx - qy * qy - qz * qz ) * vx +
                ( 2  * ( qx * qy - qw * qz ) )            * vy +
                ( 2  * ( qx * qz + qw * qy ) )            * vz;

        out.y = ( 2 * ( qx * qy + qw * qz ) )             * vx +
                ( qw * qw - qx * qx + qy * qy - qz * qz ) * vy +
                ( 2 * ( qy * qz - qw * qx ) )             * vz;

        out.z = ( 2 * ( qx * qz - qw * qy ) )             * vx +
                ( 2 * ( qy * qz + qw * qx ) )             * vy +
                ( qw * qw - qx * qx - qy * qy + qz * qz ) * vz;
    }

    /**
     * Normalize quaternion to a valid unit-length.
     */
    @Deprecated
    public static void normalize( Vec4 q ) {
        Vec.normalize( q );
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param rand0 Arbitrary val in [0,1]
     * @param rand1 Arbitrary val in [0,1]
     * @param rand2 Arbitrary val in [0,1]
     * @param out   Holds quaternion on return.
     */
    @Deprecated
    public static void randToQuat( float rand0, float rand1, float rand2, Vec4 out ) {
        float sign0 = 1f;
        float sign1 = 1f;
        float sign2 = 1f;

        // Sort three numbers.
        // Use the sort order to generate 3 random booleans for mSign values.
        if( rand0 > rand1 ) {
            sign0 = -1;
            sign1 = -1;
            float swap = rand0;
            rand0 = rand1;
            rand1 = swap;
        }
        if( rand1 > rand2 ) {
            sign1 = -sign1;
            sign2 = -sign2;
            float swap = rand1;
            rand1 = rand2;
            rand2 = swap;

            if( rand0 > rand1 ) {
                sign0 = -sign0;
                sign1 = -sign1;
                swap  = rand0;
                rand0 = rand1;
                rand1 = swap;
            }
        }

        out.x = sign0 * (rand0        );
        out.y = sign1 * (rand1 - rand0);
        out.z = sign2 * (rand2 - rand1);
        out.w = 1.0f - rand2;
        normalize( out );
    }

    /**
     * Converts axis-rotation to quaternion representation.
     * @param rads Radians of rotation
     * @param x    X-coord of rotation axis
     * @param y    Y-coord of rotation axis
     * @param z    Z-coord of rotation axis
     * @param out  Length-4 array that holds rotation on return.
     */
    @Deprecated
    public static void getRotation( float rads, float x, float y, float z, Vec4 out ) {
        float cos = (float)Math.cos( rads * 0.5 );
        float len = (float)Math.sqrt( x * x + y * y + z * z );
        float sin = (float)Math.sin( rads * 0.5 ) / len;
        out.x = sin * x;
        out.y = sin * y;
        out.z = sin * z;
        out.w = cos;
    }

    @Deprecated
    public static void rotationBetweenUnitVecs( Vec3 src, Vec3 dst, Vec4 out ) {
        float r = Vec.dot( src, dst ) + 1;

        if( r < 0.000001f ) {
            r = 0;
            if( Math.abs( src.x ) > Math.abs( src.z ) ) {
                out.x = -src.y;
                out.y = src.x;
                out.z = 0;
                out.w = 0;
            } else {
                out.x = 0;
                out.y = -src.z;
                out.z = src.y;
                out.w = 0;
            }

        } else {
            out.x = src.y * dst.z - dst.y * src.z;
            out.y = src.z * dst.x - dst.z * src.x;
            out.z = src.x * dst.y - dst.x * src.y;
            out.w = r;
        }

        normalize( out );
    }

    /**
     * Multiplies quaternion with axis-rotation.
     *
     * @param q    Input quaternion.
     * @param rads Degree of  rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param out  receives output
     */
    @Deprecated
    public static void rotate( Vec4 q, float rads, float x, float y, float z, Vec4 out ) {
        final float ax = q.x;
        final float ay = q.y;
        final float az = q.z;
        final float aw = q.w;

        float cos = (float)Math.cos( rads * 0.5 );
        float len = (float)Math.sqrt( x * x + y * y + z * z );
        float sin = (float)Math.sin( rads * 0.5 ) / len;
        float bx = sin * x;
        float by = sin * y;
        float bz = sin * z;
        float bw = cos;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }

    /**
     * Multiplies axis-rotation with quaternion.
     *
     * @param rads Degree of rotation.
     * @param x    X-Coord of rotation axis.
     * @param y    Y-Coord of rotation axis.
     * @param z    Z-Coord of rotation axis.
     * @param q    Input quaternion.
     * @param out  receives output
     */
    @Deprecated
    public static void preRotate( float rads, float x, float y, float z, Vec4 q, Vec4 out ) {
        final float cos = (float)Math.cos( rads * 0.5 );
        final float len = (float)Math.sqrt( x * x + y * y + z * z );
        final float sin = (float)Math.sin( rads * 0.5 ) / len;
        final float ax = sin * x;
        final float ay = sin * y;
        final float az = sin * z;
        final float aw = cos;
        final float bx = q.x;
        final float by = q.y;
        final float bz = q.z;
        final float bw = q.w;

        out.x = aw * bx + ax * bw + ay * bz - az * by;
        out.y = aw * by - ax * bz + ay * bw + az * bx;
        out.z = aw * bz + ax * by - ay * bx + az * bw;
        out.w = aw * bw - ax * bx - ay * by - az * bz;
    }


    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    @Deprecated
    public static void quatToMat( Vec4 quat, Mat3 out ) {
        final float x = quat.x;
        final float y = quat.y;
        final float z = quat.z;
        final float w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
    }

    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Input quaternion.
     * @param out  Receives output.
     */
    @Deprecated
    public static void quatToMat( Vec4 quat, Mat4 out ) {
        final float x = quat.x;
        final float y = quat.y;
        final float z = quat.z;
        final float w = quat.w;

        out.m00 = 1 - 2 * ( y * y + z * z );
        out.m10 =     2 * ( x * y + z * w );
        out.m20 =     2 * ( x * z - y * w );
        out.m30 = 0;
        out.m01 =     2 * ( x * y - z * w );
        out.m11 = 1 - 2 * ( x * x + z * z );
        out.m21 =     2 * ( y * z + x * w );
        out.m31 = 0;
        out.m02 =     2 * ( x * z + y * w );
        out.m12 =     2 * ( y * z - x * w );
        out.m22 = 1 - 2 * ( x * x + y * y );
        out.m32 = 0;
        out.m03 = 0;
        out.m13 = 0;
        out.m23 = 0;
        out.m33 = 1;
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param out   Holds output quaternion on return.
     */
    @Deprecated
    public static void sampleUniform( Random rand, Vec4 out ) {
        randToQuat( rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), out );
    }

    /**
     * Computes spherical interpolation between two quaternions.
     * @param qa  Quaternior
     * @param qb  Quaternion
     * @param t   Blend factor
     * @param out Length-4 vec to receive output.
     */
    @Deprecated
    public static void slerp( Vec4 qa, Vec4 qb, float t, Vec4 out ) {
        // Calculate angle between them.
        float cosHalfTheta = Vec.dot( qa, qb );

        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if( cosHalfTheta >= 1.0 || cosHalfTheta <= -1.0 ) {
            Vec.put( qa, out );
            return;
        }

        // Calculate temporary values.
        float halfTheta = (float)Math.acos( cosHalfTheta );
        float sinHalfTheta = (float)Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if( sinHalfTheta < 0.00001f && sinHalfTheta > -0.00001f ) {
            out.x = ( qa.x * 0.5f + qb.x * 0.5f );
            out.y = ( qa.y * 0.5f + qb.y * 0.5f );
            out.z = ( qa.z * 0.5f + qb.z * 0.5f );
            out.w = ( qa.w * 0.5f + qb.w * 0.5f );
            return;
        }

        float ratioA = (float)Math.sin( (1 - t) * halfTheta ) / sinHalfTheta;
        float ratioB = (float)Math.sin( t * halfTheta ) / sinHalfTheta;
        //calculate Quaternion.
        out.x = ( qa.x * ratioA + qb.x * ratioB );
        out.y = ( qa.y * ratioA + qb.y * ratioB );
        out.z = ( qa.z * ratioA + qb.z * ratioB );
        out.w = ( qa.w * ratioA + qb.w * ratioB );
    }



    //=== Double[4] Functions =======================================================

    @Deprecated
    public static void conjugate( double[] a, double[] out ) {
        out[0] = -a[0];
        out[1] = -a[1];
        out[2] = -a[2];
        out[3] =  a[3];
    }

    /**
     * Formats quaternion for printing.
     */
    @Deprecated
    public static String format4( double[] quat ) {
        return Vec.format4( quat );
    }

    @Deprecated
    public static void invert( double[] a, double[] out ) {
        double s = 1 / ( a[0] * a[0] + a[1] * a[1] + a[2] * a[2] + a[3] * a[3] );
        out[0] = -a[0] * s;
        out[1] = -a[1] * s;
        out[2] = -a[2] * s;
        out[3] =  a[3] * s;
    }

    /**
     * Converts a rotation matrix to an equivalent quaternion.
     * Non-rotation matrices will produce undefined results.
     *
     * @param mat Length-16 array holding rotation matrix.
     * @param out Length-4 array that holds equivalent quaternion on return.
     */
    @Deprecated
    public static void mat4ToQuat( double[] mat, double[] out ) {
        final double m00 = mat[ 0];
        final double m11 = mat[ 5];
        final double m22 = mat[10];
        double tr = m00 + m11 + m22;

        if( tr > 0 ) {
            double sc = Math.sqrt( tr + 1.0 ) * 2; // S=4*qw 
            out[0] = (mat[ 6] - mat[ 9]) / sc;
            out[1] = (mat[ 8] - mat[ 2]) / sc;
            out[2] = (mat[ 1] - mat[ 4]) / sc;
            out[3] = 0.25f * sc;

        } else if( (m00 > m11) & (m00 > m22) ) {
            double sc = Math.sqrt( 1.0 + m00 - m11 - m22 ) * 2; // S=4*qx 
            out[0] = 0.25f * sc;
            out[1] = (mat[ 4] + mat[ 1]) / sc;
            out[2] = (mat[ 8] + mat[ 2]) / sc;
            out[3] = (mat[ 6] - mat[ 9]) / sc;

        } else if( m11 > m22 ) {
            double sc = Math.sqrt( 1.0 + m11 - m00 - m22 ) * 2; // S=4*qy
            out[0] = (mat[ 4] + mat[ 1]) / sc;
            out[1] = 0.25f * sc;
            out[2] = (mat[ 9] + mat[ 6]) / sc;
            out[3] = (mat[ 8] - mat[ 2]) / sc;

        } else {
            double sc = Math.sqrt( 1.0 + m22 - m00 - m11 ) * 2; // S=4*qz
            out[0] = (mat[ 8] + mat[ 2]) / sc;
            out[1] = (mat[ 9] + mat[ 6]) / sc;
            out[2] = 0.25f * sc;
            out[3] = (mat[ 1] - mat[ 4]) / sc;
        }
    }

    /**
     * Multiply two quaternions.
     * @param out Length-4 array to hold output on return. May be the same array as one of the inputs.
     */
    @Deprecated
    public static void mult( double[] a, double[] b, double[] out ) {
        // These local copies had no effect in performance tests, but whatevs.
        final double ax = a[0];
        final double ay = a[1];
        final double az = a[2];
        final double aw = a[3];
        final double bx = b[0];
        final double by = b[1];
        final double bz = b[2];
        final double bw = b[3];

        out[0] = aw * bx + ax * bw + ay * bz - az * by;
        out[1] = aw * by - ax * bz + ay * bw + az * bx;
        out[2] = aw * bz + ax * by - ay * bx + az * bw;
        out[3] = aw * bw - ax * bx - ay * by - az * bz;
    }

    @Deprecated
    public static void multVec3( double[] quat, double[] vec, double[] out ) {
        final double qx = quat[0];
        final double qy = quat[1];
        final double qz = quat[2];
        final double qw = quat[3];
        final double vx = vec[0];
        final double vy = vec[1];
        final double vz = vec[2];

        out[0] = ( qw * qw + qx * qx - qy * qy - qz * qz ) * vx +
                 ( 2  * ( qx * qy - qw * qz ) )            * vy +
                 ( 2  * ( qx * qz + qw * qy ) )            * vz;

        out[1] = ( 2 * ( qx * qy + qw * qz ) )             * vx +
                 ( qw * qw - qx * qx + qy * qy - qz * qz ) * vy +
                 ( 2 * ( qy * qz - qw * qx ) )             * vz;

        out[2] = ( 2 * ( qx * qz - qw * qy ) )             * vx +
                 ( 2 * ( qy * qz + qw * qx ) )             * vy +
                 ( qw * qw - qx * qx - qy * qy + qz * qz ) * vz;
    }

    /**
     * Normalize quaternion to a valid unit-length.
     */
    @Deprecated
    public static void normalize( double[] q ) {
        Vec.normalize4( q );
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param rand0 Arbitrary val in [0,1]
     * @param rand1 Arbitrary val in [0,1]
     * @param rand2 Arbitrary val in [0,1]
     * @param out   Holds quaternion on return.
     */
    @Deprecated
    public static void randToQuat( double rand0, double rand1, double rand2, double[] out ) {
        double sign0 = 1.0;
        double sign1 = 1.0;
        double sign2 = 1.0;

        // Sort three numbers.
        // Use the sort order to generate 3 random booleans for mSign values.
        if( rand0 > rand1 ) {
            sign0 = -1;
            sign1 = -1;
            double swap = rand0;
            rand0 = rand1;
            rand1 = swap;
        }
        if( rand1 > rand2 ) {
            sign1 = -sign1;
            sign2 = -sign2;
            double swap = rand1;
            rand1 = rand2;
            rand2 = swap;

            if( rand0 > rand1 ) {
                sign0 = -sign0;
                sign1 = -sign1;
                swap  = rand0;
                rand0 = rand1;
                rand1 = swap;
            }
        }

        out[0] = sign0 * (rand0        );
        out[1] = sign1 * (rand1 - rand0);
        out[2] = sign2 * (rand2 - rand1);
        out[3] = 1.0f - rand2;
        normalize( out );
    }

    /**
     * Converts axis-rotation to quaternion representation.
     * @param rads Radians of rotation
     * @param x    X-coord of rotation axis
     * @param y    Y-coord of rotation axis
     * @param z    Z-coord of rotation axis
     * @param out  Length-4 array that holds rotation on return.
     */
    @Deprecated
    public static void rotation( double rads, double x, double y, double z, double[] out ) {
        double cos = Math.cos( rads * 0.5 );
        double len = Math.sqrt( x * x + y * y + z * z );
        double sin = Math.sin( rads * 0.5 ) / len;
        out[0] = cos;
        out[1] = sin * x;
        out[2] = sin * y;
        out[3] = sin * z;
    }

    /**
     * Converts quaternion to dim4 rotation matrix.
     *
     * @param quat Length-4 array holding quaternion.
     * @param out  Length-16 array that holds equivalent matrix on return.
     */
    @Deprecated
    public static void quatToMat4( double[] quat, double[] out ) {
        final double x = quat[0];
        final double y = quat[1];
        final double z = quat[2];
        final double w = quat[3];

        out[ 0] = 1 - 2 * ( y * y + z * z );
        out[ 1] =     2 * ( x * y + z * w );
        out[ 2] =     2 * ( x * z - y * w );
        out[ 3] = 0;
        out[ 4] =     2 * ( x * y - z * w );
        out[ 5] = 1 - 2 * ( x * x + z * z );
        out[ 6] =     2 * ( y * z + x * w );
        out[ 7] = 0;
        out[ 8] =     2 * ( x * z + y * w );
        out[ 9] =     2 * ( y * z - x * w );
        out[10] = 1 - 2 * ( x * x + y * y );
        out[11] = 0;
        out[12] = 0;
        out[13] = 0;
        out[14] = 0;
        out[15] = 1;
    }

    /**
     * Converts three random numbers from [0,1] into a quaternion
     * in such a way that if the samples are independent and
     * uniformly distributed, then the resulting quaternions will
     * uniformly represent the domain of quaternion rotations.
     * <p>
     * One implication of this function is that it can be used
     * to uniformly sample a 2-sphere.
     *
     * @param out   Holds output quaternion on return.
     */
    @Deprecated
    public static void sampleUniform( Random rand, double[] out ) {
        randToQuat( rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), out );
    }

    /**
     * Computes spherical interpolation between two quaternions.
     * @param qa  Quaternior
     * @param qb  Quaternion
     * @param t   Blend factor
     * @param out Length-4 array that holds quaternion output on return.
     */
    @Deprecated
    public static void slerp( double[] qa, double[] qb, double t, double[] out ) {
        // Calculate angle between them.
        double cosHalfTheta = Vec.dot4( qa, qb );

        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if( cosHalfTheta >= 1.0 || cosHalfTheta <= -1.0 ) {
            Vec.put4( qa, out );
            return;
        }

        // Calculate temporary values.
        double halfTheta    = Math.acos( cosHalfTheta );
        double sinHalfTheta = Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if( sinHalfTheta < 0.00001 && sinHalfTheta > -0.00001 ) {
            out[0] = ( qa[0] * 0.5 + qb[0] * 0.5 );
            out[1] = ( qa[1] * 0.5 + qb[1] * 0.5 );
            out[2] = ( qa[2] * 0.5 + qb[2] * 0.5 );
            out[3] = ( qa[3] * 0.5 + qb[3] * 0.5 );
            return;
        }

        double ratioA = Math.sin( (1 - t) * halfTheta ) / sinHalfTheta;
        double ratioB = Math.sin( t * halfTheta ) / sinHalfTheta;
        //calculate Quaternion.
        out[0] = ( qa[0] * ratioA + qb[0] * ratioB );
        out[1] = ( qa[1] * ratioA + qb[1] * ratioB );
        out[2] = ( qa[2] * ratioA + qb[2] * ratioB );
        out[3] = ( qa[3] * ratioA + qb[3] * ratioB );
    }

    @Deprecated
    public static void rotation( float rads, float x, float y, float z, Vec4 out ) {
        getRotation( rads, x, y, z, out );
    }


}
