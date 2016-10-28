/*
 * Copyright (c) 2016. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 */

package bits.vec;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;
import static bits.vec.Tests.*;


/**
 * @author Philip DeCamp
 */
public class TestPhi {

    Random rand = new Random( 8 );

    @Test
    public void testNcdfAndInv() {
        for( int i = 0; i < 100; i++ ) {
            double v = rand.nextDouble() * 8 - 4;
            double gg = Phi.ncdf( v );
            assertNear( v, Phi.ncdfInvFast( gg ) );
            assertNear( v, Phi.ncdfInv( gg ) );
        }

        assertNear( 0.0, Phi.ncdf( Double.NEGATIVE_INFINITY ) );
        assertNear( 1.0, Phi.ncdf( Double.POSITIVE_INFINITY ) );
        assertTrue( Double.NEGATIVE_INFINITY == Phi.ncdfInv( 0 ) );
        assertTrue( Double.POSITIVE_INFINITY == Phi.ncdfInv( 1.0 ) );
    }

}
