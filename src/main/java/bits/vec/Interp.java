/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;


/**
 * Interpolation functions
 * 
 * @author decamp
 */
public final class Interp {

    /**
     * Linear interpolation.
     *
     * @param x0 Value before sample
     * @param x1 Value after sample
     * @param t  Parameterized position to sample, parameterized between [0,1].
     * @return   interpolated mVal
     */
    public static float lerp( float x0, float x1, float t ) {
        return x0 * ( 1.0f - t ) + x1 * t;
    }


    public static float bezier( float begin, float control, float end, float t ) {
        final float s = 1f - t;
        return s * s * begin + s * t * control + t * t * end;
    }


    public static void bezier( float[] begin, float[] control, float[] end, float t, float[] out ) {
        final int dim = begin.length;
        final float s  = 1f - t;
        final float c0 = s * s;
        final float c1 = 2f * s * t;
        final float c2 = t * t;

        for( int i = 0; i < dim; i++ ) {
            out[i] = c0 * begin[i] + c1 * control[i] + c2 * end[i];
        }
    }


    public static float bezier( float begin, float control0, float control1, float end, float t ) {
        final float s = 1f - t;
        return s * s * s * begin +
               3f * s * s * t * control0 +
               3f * s * t * t * control1 +
               t * t * t * end;
    }


    public static void bezier( float[] begin, float[] control0, float[] control1, float[] end, float t, float[] out ) {
        final int    dim = begin.length;
        final float s   = 1f - t;
        final float c0  = s * s * s;
        final float c1  = 3f * s * s * t;
        final float c2  = 3f * s * t * t;
        final float c3  = t * t * t;

        for( int i = 0; i < dim; i++ ) {
            out[i] = c0 * begin[i] + c1 * control0[i] + c2 * control1[i] + c3 * end[i];
        }
    }

    /**
     * Cubic spline interpolation.
     *  
     * @param x0 Point before point before sample.
     * @param x1 Point before sample.
     * @param x2 Point after sample.
     * @param x3 Point after point after sample.
     * @param t  Curve position to sample, parameterized between [0,1].
     * @return interpolated point.
     */
    public static float cubic( float x0, float x1, float x2, float x3, float t ) {
        float tt = t * t;
        float a0 = x3 - x2 - x0 + x1;
        float a1 = x0 - x1 - a0;
        float a2 = x2 - x0;
        return a0 * t * tt + a1 * tt + a2 * t + x1;
    }

    /**
     * Cubic spline interpolation.
     *
     * @param x0  Point before point before sample.
     * @param x1  Point before sample.
     * @param x2  Point after sample.
     * @param x3  Point after point after sample.
     * @param t   Curve position to sample, parameterized between [0,1].
     * @param out Holds interpolated point on return.
     */
    public static void cubic( float[] x0, float[] x1, float[] x2, float[] x3, float t, float[] out ) {
        final int dim = x0.length;
        for( int i = 0; i < dim; i++ ) {
            out[i] = cubic( x0[i], x1[i], x2[i], x3[i], t );
        }
    }

    /**
     * Catmull-Rom spline interpolation.
     * <p>
     * Similar to cubic spline, but smoother
     * due to use of derivatives.
     * 
     * @param x0 Point before point before sample.
     * @param x1 Point before sample.
     * @param x2 Point after sample.
     * @param x3 Point after point after sample.
     * @param t  Curve position to sample, parameterized between [0,1].
     * @return interpolated point.
     */
    public static float catmull( float x0, float x1, float x2, float x3, float t ) {
        float tt = t * t;
        float a0 = -0.5f * x0 + 1.5f * x1 - 1.5f * x2 + 0.5f * x3;
        float a1 = x0 - 2.5f * x1 + 2f * x2 - 0.5f * x3;
        float a2 = -0.5f * x0 + 0.5f * x2;
        return a0 * t * tt + a1 * tt + a2 * t + x1;
    }

    /**
     * Catmull-Rom spline interpolation.
     * <p>
     * Similar to cubic spline, but smoother
     * due to use of derivatives.
     *
     * @param x0  Point before point before sample.
     * @param x1  Point before sample.
     * @param x2  Point after sample.
     * @param x3  Point after point after sample.
     * @param t   Curve position to sample, parameterized between [0,1].
     * @param out On return, holds interpolated point.
     */
    public static void catmull( float[] x0, float[] x1, float[] x2, float[] x3, float t, float[] out ) {
        final int dim = x0.length;
        for( int i = 0; i < dim; i++ ) {
            out[i] = catmull( x0[i], x1[i], x2[i], x3[i], t );
        }
    }

    /**
     * Hermite spline interpolation.
     * 
     * @param x0 Point before point before sample
     * @param x1 Point before sample
     * @param x2 Point after sample
     * @param x3 Point after point after sample
     * @param t  Curve position to sample, parameterized between [0,1].
     * @param tension 1 is high, 0 is normal, -1 is low
     * @param bias    0 is even, positive is towards first segment, negative is towards other.
     * @return interpolated point
     */
    public static float hermite(
            float x0,
            float x1,
            float x2,
            float x3,
            float t,
            float tension,
            float bias
    ) {
        float tt  = t * t;
        float ttt = tt * t;
        tension = 0.5f - 0.5f * tension;
        
        float m0  = (x1-x0) * (1+bias) * tension +
                    (x2-x1) * (1-bias) * tension;
        
        float m1 = (x2-x1) * (1+bias) * tension +
                   (x3-x2) * (1-bias) * tension;
        
        float a0 =  2 * ttt - 3 * tt     + 1;
        float a1 =      ttt - 2 * tt + t    ;
        float a2 =      ttt -     tt        ;
        float a3 = -2 * ttt + 3 * tt        ;
        
        return a0*x1 + a1*m0 + a2*m1 + a3*x2;
    }

    /**
     * Hermite spline interpolation.
     *
     * @param x0 Point before point before sample
     * @param x1 Point before sample
     * @param x2 Point after sample
     * @param x3 Point after point after sample
     * @param t  Curve position to sample, parameterized between [0,1].
     * @param tension 1 is high, 0 is normal, -1 is low
     * @param bias    0 is even, positive is towards first segment, negative is towards other.
     * @param out     Holds interpolated point on return.
     */
    public static void hermite(
            float[] x0,
            float[] x1,
            float[] x2,
            float[] x3,
            float t,
            float tension,
            float bias,
            float[] out
    ) {
        final int dim = x0.length;
        final float tt  = t * t;
        final float ttt = tt * t;
        final float a0  =  2 * ttt - 3 * tt     + 1;
        final float a1  =      ttt - 2 * tt + t    ;
        final float a2  =      ttt -     tt        ;
        final float a3  = -2 * ttt + 3 * tt        ;

        tension = 0.5f - 0.5f * tension;

        for( int i = 0; i < dim; i++ ) {
            float m0  = (x1[i]-x0[i]) * (1+bias) * tension +
                        (x2[i]-x1[i]) * (1-bias) * tension;

            float m1 = (x2[i]-x1[i]) * (1+bias) * tension +
                       (x3[i]-x2[i]) * (1-bias) * tension;

            out[i] = a0 * x1[i] + a1 * m0 + a2 * m1 + a3 * x2[i];
        }
    }


    private Interp() {}
    
}
