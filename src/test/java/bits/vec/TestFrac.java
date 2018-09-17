/*
 * Copyright (c) 2015. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Philip DeCamp
 */
public class TestFrac {

    @Test
    public void testMultFracs() {
        Frac c = new Frac();
        assertTrue( Frac.mult( 1, 4, 1, 4, c ) );
        assertEquals( new Frac( 1, 16 ), c );

        assertTrue( Frac.mult( 4, 1, 1, 4, c ) );
        assertEquals( new Frac( 1, 1 ), c );

        assertFalse( Frac.mult( Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, c ) );
        assertEquals( new Frac( 0x7FFFFFFF, 1 ), c );

        assertFalse( Frac.mult( Integer.MIN_VALUE, 1, Integer.MAX_VALUE, 1, c ) );
        assertEquals( new Frac( 0x80000001, 1 ), c );

        assertTrue( Frac.mult( Integer.MIN_VALUE, 0, Integer.MAX_VALUE, 1, c ) );
        assertEquals( new Frac( -1, 0 ), c );

        assertTrue( Frac.mult( Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 1, c ) );
        assertEquals( new Frac( 1, 0 ), c );

        assertTrue( Frac.mult( -1, 0, Integer.MAX_VALUE, 1, c ) );
        assertEquals( new Frac( -1, 0 ), c );

        assertTrue( Frac.mult( -1, 0, 1, 0, c ) );
        assertEquals( new Frac( -1, 0 ), c );
    }

    @Test
    public void testMultLong() throws FileNotFoundException {
        Random rand = new Random( System.currentTimeMillis() );
               
        
        long[] aSet = { 
                Long.MIN_VALUE, 
                Long.MIN_VALUE + 1,
                Integer.MIN_VALUE,
                -1, 
                0, 
                1,
                0x0FFFFFFFFFFFFFFEL,
                Integer.MAX_VALUE,
                Long.MAX_VALUE - 1, 
                Long.MAX_VALUE 
        };
        
        int[] bSet = { 
                Integer.MIN_VALUE, 
                -1, 
                0, 
                1,
                0xB504F330,
                Integer.MAX_VALUE 
        };
        
        int[] cSet = bSet;
        
        int[] roundSet = {
                Frac.ROUND_DOWN,
                Frac.ROUND_INF,
                Frac.ROUND_NEAR_INF,
                Frac.ROUND_UP,
                Frac.ROUND_ZERO
        };
        
        PrintWriter p = new PrintWriter(new File( "/tmp/t.txt"));
        
        for( int i = 0; i < 10000; i++ ) {
            int j = i;
            long va = aSet[j % aSet.length];
            j /= aSet.length;
            int vb = bSet[j % bSet.length];
            j /= bSet.length;
            int vc = cSet[j % bSet.length];
            j /= cSet.length;
            int round = roundSet[j % roundSet.length];
            j /= roundSet.length;
            
            if( j > 0 ) {
                va = rand.nextLong();
                vb = rand.nextInt();
                vc = rand.nextInt();
                round = roundSet[ rand.nextInt( roundSet.length ) ];
            }

            long actual = Frac.multLong( va, vb, vc, round );
            long expected = bigMult( va, vb, vc, round );
            long err = actual - expected;
            
            if( err != 0 ) {
                System.out.format( "0x%016X * 0x%016X / 0x%016X (round: %d) =   0x%016X  (- 0x%016X = %X)\n",
                        va, vb, vc, round, actual, expected, err );
                System.out.flush();
            }

            p.format( "t(%d, %d, %d, %d, %d)\n", va, vb, vc, round, expected );
            assertEquals( 0, err );
        }
        
        p.flush();
    }
    
    @Test
    public void testGcd() {
        assertEquals( 4, Frac.gcd( 4 * 7, 4 * 13 ) );
        assertEquals( 4, Frac.gcd( 4 * 13, 4 * 7 ) );
        assertEquals( 2, Frac.gcd( 2, Long.MIN_VALUE ) );
        assertEquals( 2, Frac.gcd( Long.MIN_VALUE, Long.MAX_VALUE - 1 ) );
        assertEquals( 2, Frac.gcd( Long.MAX_VALUE - 1, Long.MIN_VALUE ) );
        assertEquals( Long.MAX_VALUE, Frac.gcd( Long.MAX_VALUE, Long.MIN_VALUE + 1 ) );
        assertEquals( Long.MIN_VALUE, Frac.gcd( Long.MIN_VALUE, Long.MIN_VALUE ) );
        assertEquals( Long.MAX_VALUE, Frac.gcd( Long.MAX_VALUE, Long.MAX_VALUE ) );
    }

    @Test
    public void testToDouble() {
        assertTrue( Double.isNaN( new Frac( 0, 0 ).toDouble() ) );
        assertTrue( Double.POSITIVE_INFINITY == new Frac( 1, 0 ).toDouble() );
        assertTrue( Double.NEGATIVE_INFINITY == new Frac( -1, 0 ).toDouble() );
    }

    @Test
    public void testReduce() {
        Frac out = new Frac();

        // Normal operation.
        Frac.reduce( 4, 8, Integer.MAX_VALUE, out );
        assertCanonical( 1, 2, out );
        Frac.reduce( 8, 4, Integer.MAX_VALUE, out );
        assertCanonical( 2, 1, out );
        Frac.reduce( 137, 48, Integer.MAX_VALUE, out );
        assertCanonical( 137, 48, out );
        Frac.reduce( 137, 48, 32, out );
        assertCanonical( 20, 7, out );
        Frac.reduce( 48, 137, 32, out );
        assertCanonical( 7, 20, out );

        // Special cases
        assertTrue( Frac.reduce( 10000, 0, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 0, out );
        assertTrue( Frac.reduce( -10000, 0, Integer.MAX_VALUE, out ) );
        assertCanonical( -1, 0, out );
        assertTrue( Frac.reduce( 0, 0, Integer.MAX_VALUE, out ) );
        assertCanonical( 0, 0, out );

        // Bit limit tests.
        assertFalse( Frac.reduce( Long.MAX_VALUE, Long.MAX_VALUE - 1, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 1, out );
        assertFalse( Frac.reduce( Long.MIN_VALUE, Long.MIN_VALUE + 1, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 1, out );
        assertTrue( Frac.reduce( Long.MIN_VALUE, Long.MIN_VALUE >> 1, Integer.MAX_VALUE, out ) );
        assertCanonical( 2, 1, out );
        assertTrue( Frac.reduce( Long.MIN_VALUE >> 1, Long.MIN_VALUE, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 2, out );
        assertFalse( Frac.reduce( Long.MAX_VALUE >> 1, Long.MIN_VALUE, Integer.MAX_VALUE, out ) );
        assertCanonical( -1, 2, out );
        assertFalse( Frac.reduce( Long.MIN_VALUE >> 1, Long.MAX_VALUE, Integer.MAX_VALUE, out ) );
        assertCanonical( -1, 2, out );

        assertTrue( Frac.reduce( Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 1, out );
        assertTrue( Frac.reduce( Integer.MAX_VALUE, 1, Integer.MAX_VALUE, out ) );
        assertCanonical( Integer.MAX_VALUE, 1, out );
        assertTrue( Frac.reduce( Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, out ) );
        assertCanonical( 1, 1, out );
        assertFalse( Frac.reduce( Integer.MIN_VALUE, 1, Integer.MAX_VALUE, out ) );
        assertCanonical( -Integer.MAX_VALUE, 1, out );
        assertFalse( Frac.reduce( Integer.MIN_VALUE, 3, Integer.MAX_VALUE, out ) );
        assertCanonical( -1431655765, 2, out );
    }

    @Test
    public void testDoubleToRational() {
        Frac ra = new Frac();

        Frac.doubleToRational( Double.NEGATIVE_INFINITY, ra );
        assertCanonical( -1, 0, ra );

        Frac.doubleToRational( Double.NaN, ra );
        assertCanonical( 0, 0, ra );

        Frac.doubleToRational( Double.POSITIVE_INFINITY, ra );
        assertCanonical( 1, 0, ra );

        Frac.doubleToRational( 0.0, ra );
        assertCanonical( 0, 1, ra );

        Frac.doubleToRational( 0.5, ra );
        assertCanonical( 1, 2, ra );

        Frac.doubleToRational( -0.5, ra );
        assertCanonical( -1, 2, ra );

        Frac.doubleToRational( Math.PI, ra );
        assertCanonical( 1881244168, 598818617, ra );
    }

    @Test
    public void testPrint() {
        System.out.println( Frac.multLong( Long.MAX_VALUE, Integer.MAX_VALUE, 1 ) );
    }


    static void assertCanonical( int a, int b, Frac rat ) {
        assertTrue( rat.isCanonical() );
        assertEquals( new Frac( a, b ), rat );
    }


    
    static long bigMult( long a, long b, long c, int round ) {
        if( a == 0 || b == 0 ) {
            return 0;
        }
        
        if( c == 0 ) {
            return ( a ^ b ) < 0 ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
        
        BigInteger ret = bigMultRaw( a, b, c, round );
        ret = ret.max( BigInteger.valueOf( Long.MIN_VALUE ) );
        ret = ret.min( BigInteger.valueOf( Long.MAX_VALUE ) );
        return ret.longValue();
    }
    

    static BigInteger bigMultRaw( long a, long b, long c, int round ) {
        BigInteger ia = BigInteger.valueOf( a );
        BigInteger ib = BigInteger.valueOf( b );
        BigInteger ic = BigInteger.valueOf( c );
        BigInteger[] vals = ia.multiply( ib ).divideAndRemainder( ic );

        if( vals[1].longValue() == 0 ) {
            return vals[0];
        }

        boolean negative = a < 0 ^ b < 0 ^ c < 0;

        switch( round ) {
        case Frac.ROUND_ZERO:
            return vals[0];

        case Frac.ROUND_NEAR_INF: {
            BigInteger halfc = BigInteger.valueOf( Math.abs( c / 2 ) );
            int cmp = vals[1].abs().compareTo( halfc );
            if( cmp < 0 || c == 0 && (c & 1) == 0 ) {
                return vals[0];
            }

            // Remainder is more than half of divisor. Round up
            // Fallthrough
        }
        case Frac.ROUND_INF:
            if( !negative ) {
                return vals[0].add( BigInteger.ONE );
            } else {
                return vals[0].subtract( BigInteger.ONE );
            }

        case Frac.ROUND_DOWN:
            return !negative ? vals[0] : vals[0].subtract( BigInteger.ONE );

        case Frac.ROUND_UP:
            return !negative ? vals[0].add( BigInteger.ONE ) : vals[0];
        }

        throw new IllegalArgumentException( "Invalid rounding type.");
    }


    static int randRound( Random rand ) {
        switch( rand.nextInt( 5 ) ) {
        case 0:
            return Frac.ROUND_ZERO;
        case 1:
            return Frac.ROUND_INF;
        case 2:
            return Frac.ROUND_DOWN;
        case 3:
            return Frac.ROUND_UP;
        case 4:
        default:
            return Frac.ROUND_NEAR_INF;
        }
    }
   
}
