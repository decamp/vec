/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

/**
 * Tolerance aware functions for comparing numbers that are approximately equal. 
 *  
 * @author Philip DeCamp  
 */
public final class Tol {
    
    public static final double EPS          = 0x0.0000000000001p-1;    // 64-bit machine epsilon
    public static final double ABS_TOL      = 0x0.0000000000001p-1017; // Double.MIN_VALUE * 32.0
    public static final double REL_TOL      = 0x0.0000000000001p4;     // EPS * 32.0
    public static final double SQRT_ABS_TOL = Math.sqrt( ABS_TOL );
    public static final double SQRT_REL_TOL = Math.sqrt( REL_TOL );
    
    public static final float FEPS          = 0x0.000002p-1f;          // 32-bit machine epsilon
    public static final float FABS_TOL      = 0x0.000002p-121f;        // Float.MIN_VALUE * 32.0f
    public static final float FREL_TOL      = 0x0.000002p4f;           // FEPS * 32.0f
    public static final float FSQRT_ABS_TOL = (float)Math.sqrt( FABS_TOL );
    public static final float FSQRT_REL_TOL = (float)Math.sqrt( FREL_TOL );


    /**
     * Equivalent to {@code approxError( a, b, SQRT_REL_TOL, SQRT_ABS_TOL );}
     */
    public static boolean near( double a, double b ) {
        return equal( a, b, SQRT_REL_TOL, SQRT_ABS_TOL );
    }

    /**
     * Equivalent to {@code approxError( a, b, REL_TOL, ABS_TOL );}
     */
    public static boolean equal( double a, double b ) {
        return equal( a, b, REL_TOL, ABS_TOL );
    }
    
    /**
     * Computes if two numbers are approximately equal. 
     * This method will compute both absolute and relative differences, and
     * return true if either difference is below a threshold.
     * <p>
     * The absolute error is simply the magnitude of the difference between {@code a} and {@code b}:
     * {@code Math.abs(a-b)}.
     * The absolute error is normally very small. The default mVal is ABS_TOL.
     * <p>
     * The relative error is the absolute error divided by the larger of the two magnitudes of {@code a} and
     * {@code b}: {@code abs(a-b)/max(abs(a),abs(b))}. A functional relTol should be no smaller than
     * Tolerance.EPS.  The default mVal is REL_TOL.
     * 
     * @param a           Input value
     * @param b           Input value
     * @param relTol Maximum relative error.
     * @param absTol Maximum absolute error.
     * @return true if {@code a} and {@code b} are approximately equal.
     */
    public static boolean equal( double a, double b, double relTol, double absTol ) {
        double diff = a - b;
        
        if( diff < 0.0 ) {
            diff = -diff;
        }        
        
        if( diff < absTol ) {  
            return true;
        }
        
        if( a < 0.0 ) {
            a = -a;
        }
        
        if( b < 0.0 ) {
            b = -b;
        }
        
        return diff < ( a > b ? a : b ) * relTol;
    }

    /**
     * Compares two numbers with allowance for errors.
     * 
     * @param a A mVal.
     * @param b A mVal.
     * @return 0 if a approximately equals b, -1 if a is smaller, 1 if a is greater.
     * 
     * @see #equal
     */
    public static int comp( double a, double b ) {
        return equal( a, b, REL_TOL, ABS_TOL ) ? 0 : (a < b ? -1 : 1 );
    }
    
    /**
     * Compares two numbers with allowance for errors.
     * 
     * @param a A mVal.
     * @param b A mVal.
     * @param relTol   Maximum relative error
     * @param absTol   Maximum absolute error.
     * @return 0 if a approximately equals b, -1 if a is smaller, 1 if a is greater.
     * 
     * @see #equal
     */
    public static int comp( double a, double b, double relTol, double absTol ) {
        return equal( a, b, relTol, absTol ) ? 0 : ( a < b ? -1 : 1 );
    }
    
    /**
     * Equivalent to {@code isZero( v, ref, REL_TOL, ABS_TOL );}
     */
    public static boolean isZero( double v, double ref ) {
        return isZero( v, ref, REL_TOL, ABS_TOL ); 
    }
    
    /**
     * Determines if some mVal is effectively zero compared to some reference.
     * This method can be called when determining if values {@code ref} and 
     * {@code v} can be combined in a meaningful way, for example, before
     * computing {@code ref / v}. 
     * <p>
     * Like {@code equal}, this method makes to checks. The first check
     * ensures that {@code abs(v)} is above some minimum absolute error. 
     * The second ensures that {@code abs(v / ref)} is above some minimum relative error.
     * 
     * @param v       Some mVal
     * @param ref     A reference mVal that might be divided or multiplied by
     * @param relTol  Minimum ratio between abs(v) and abs(ref) for abs(v) to be considered non-zero.
     * @param absTol  Minimum mVal abs(v) must be to be considered non-zero.
     * @return true iff v is approximately equal to 0 relative to ref.
     */
    public static boolean isZero( double v, double ref, double relTol, double absTol ) {
        if( v < absTol && -v < absTol ) {
            return true;
        }
        
        relTol *= ( ref >= 0.0 ? ref : -ref );
        return v < relTol && -v < relTol;
    }


    /**
     * Equivalent to {@code approxError( a, b, FSQRT_REL_TOL, FSQRT_ABS_TOL );}
     */
    public static boolean near( float a, float b ) {
        return equal( a, b, FSQRT_REL_TOL, FSQRT_ABS_TOL );
    }

    /**
     * Equivalent to {@code approxError( a, b, FREL_TOL, FABS_TOL );}
     */
    public static boolean equal( float a, float b ) {
        return equal( a, b, FREL_TOL, FABS_TOL );
    }
    
    /**
     * Computes if two numbers are approximately equal, protecting around rounding 
     * errors. This method will compute both absolute and relative differences, and
     * return true if either difference is below a threshold.
     * <p>
     * The absolute error is simply the magnitude of the difference between {@code a} and {@code b}:
     * {@code Math.abs(a-b)}.
     * The absolute error is normally very small. The default is FABS_TOL.
     * <p>
     * The relative error is the absolute error divided by the larger of the two magnitudes of {@code a} and
     * {@code b}: {@code abs(a-b)/max(abs(a),abs(b))}. A functional relTol should be no smaller than
     * Tolerance.FEPS. The default mVal is FREL_TOL.
     * 
     * @param a           Input value
     * @param b           Input value
     * @param relTol Maximum relative error.
     * @param absTol Maximum absolute error.
     * @return true if {@code a} and {@code b} are approximately equal.
     */
    public static boolean equal( float a, float b, float relTol, float absTol ) {
        float diff = a - b;
        
        if( diff < 0.0f ) { 
            diff = -diff; 
        }

        if( diff < absTol ) { 
            return true; 
        }

        if( a < 0.0f ) { 
            a = -a; 
        }

        if( b < 0.0f ) { 
            b = -b; 
        }

        return diff < ( a > b ? a : b ) * relTol;
    }

    /**
     * Compares two numbers with allowance for errors.
     * 
     * @param a A mVal.
     * @param b A mVal.
     * @return 0 if a approximately equals b, -1 if a is smaller, 1 if a is greater.
     * 
     * @see #equal
     */
    public static int comp( float a, float b ) {
        return equal( a, b, FREL_TOL, FABS_TOL ) ? 0 : (a < b ? -1 : 1 );
    }
    
    /**
     * Compares two numbers with allowance for errors.
     * 
     * @param a A mVal.
     * @param b A mVal.
     * @param relTol Maximum relative error.
     * @param absTol Maximum absolute error.
     * @return 0 if a approximately equals b, -1 if a is smaller, 1 if a is greater.
     * 
     * @see #equal
     */
    public static int comp( float a, float b, float relTol, float absTol ) {
        return equal( a, b, relTol, absTol ) ? 0 : ( a < b ? -1 : 1 );
    }
    
    /**
     * Equivalent to {@code isZero( v, ref, REL_TOL, ABS_TOL );}
     */
    public static boolean isZero( float v, float ref ) {
        return isZero( v, ref, FREL_TOL, FABS_TOL ); 
    }
    
    /**
     * Determines if some mVal is effectively zero compared to some reference.
     * This method can be called when determining if values {@code ref} and 
     * {@code v} can be combined in a meaningful way, for example, before
     * computing {@code ref / v}. 
     * <p>
     * Like {@code equal}, this method makes to checks. The first check
     * ensures that {@code abs(v)} is above some minimum absolute error. 
     * The second ensures that {@code abs(v / ref)} is above some minimum relative error.
     * 
     * @param v       Some mVal
     * @param ref     A reference mVal that might be divided or multiplied by
     * @param relTol  Minimum ratio between abs(v) and abs(ref) for abs(v) to be considered non-zero.
     * @param absTol  Minimum mVal abs(v) must be to be considered non-zero.
     * @return true iff v is approximately equal to 0 relative to ref.
     */
    public static boolean isZero( float v, float ref, float relTol, float absTol ) {
        if( v < absTol && -v < absTol ) {
            return true;
        }
        
        relTol *= ( ref >= 0.0 ? ref : -ref );
        return v < relTol && -v < relTol;
    }

    
        
    private Tol() {}


    @Deprecated
    public static boolean approxEqual( double a, double b ) {
        return equal( a, b );
    }

    @Deprecated
    public static boolean approxEqual( double a, double b, double maxRelError, double maxAbsError ) {
        return equal( a, b, maxRelError, maxAbsError );
    }

    @Deprecated
    public static int approxComp( double a, double b ) {
        return comp( a, b );
    }

    @Deprecated
    public static int approxComp( double a, double b, double maxRelErr, double maxAbsErr ) {
        return comp( a, b, maxRelErr, maxAbsErr );
    }

    @Deprecated
    public static boolean approxZero( double v, double ref ) {
        return isZero( v, ref );
    }

    @Deprecated
    public static boolean approxZero( double v, double ref, double relErr, double absErr ) {
        return isZero( v, ref, relErr, absErr );
    }

    @Deprecated
    public static boolean approxEqual( float a, float b ) {
        return approxEqual( a, b, FREL_TOL, FABS_TOL );
    }

    @Deprecated
    public static boolean approxEqual( float a, float b, float maxRelError, float maxAbsError ) {
        return equal( a, b, maxRelError, maxAbsError );
    }

    @Deprecated
    public static int approxComp( float a, float b ) {
        return comp( a, b );
    }

    @Deprecated
    public static int approxComp( float a, float b, float maxRelErr, float maxAbsErr ) {
        return comp( a, b, maxRelErr, maxAbsErr );
    }

    @Deprecated
    public static boolean approxZero( float v, float ref ) {
        return isZero( v, ref );
    }

    @Deprecated
    public static boolean approxZero( float v, float ref, float relErr, float absErr ) {
        return isZero( v, ref, relErr, absErr );
    }
    

    @Deprecated 
    public static boolean approxEqual( double a, double b, double maxRelError ) {
        return approxEqual( a, b, maxRelError, ABS_TOL );
    }

    @Deprecated 
    public static boolean approxZero( float v, float ref, float relErr ) {
        return approxZero( v, ref, relErr, FABS_TOL );
    }

    @Deprecated 
    public static boolean approxZero( double v ) {
        return v < ABS_TOL && -v < ABS_TOL;
    }

    @Deprecated 
    public static boolean approxZero( float v ) {
        return v < FABS_TOL && v < -FABS_TOL;
    }
    
    @Deprecated 
    public static boolean approxZero( double v, double ref, double relErr ) {
        return approxZero( v, ref, relErr, ABS_TOL );
    }
    
    @Deprecated 
    public static boolean approxEqual( float a, float b, float maxRelError ) {
        return approxEqual( a, b, maxRelError, FABS_TOL );
    }
    
    @Deprecated public static final double TOL = 1E-10;
    @Deprecated public static final double ABS_ERR       = 0x0.0000000000001p-1017; // Double.MIN_VALUE * 32.0
    @Deprecated public static final double REL_ERR       = 0x0.0000000000001p4;     // EPS * 32.0
    @Deprecated public static final double SQRT_ABS_ERR  = Math.sqrt( ABS_TOL );
    @Deprecated public static final float  FTOL = 1E-5f;
    @Deprecated public static final float  FABS_ERR       = 0x0.000002p-121f;        // Float.MIN_VALUE * 32.0f
    @Deprecated public static final float  FREL_ERR       = 0x0.000002p4f;           // FEPS * 32.0f
    @Deprecated public static final float  FSQRT_ABS_ERR  = (float)Math.sqrt( FABS_ERR );

}
