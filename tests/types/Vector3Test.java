package types;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import algebraicStructures.AdditiveAbelianGroupElementTester;
import algebraicStructures.MetricSpaceElementTester;
import algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link Vector3}.
 */
class Vector3Test
    implements
        AdditiveAbelianGroupElementTester<Vector3>,
        VectorSpaceElementTester<Vector3>,
        MetricSpaceElementTester<Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<Vector3> getElementList()
    {
        List<Vector3> output = new ArrayList<Vector3>();
        output.add( Vector3.zero() );
        output.add( Vector3.i() );
        output.add( Vector3.j() );
        output.add( Vector3.k() );
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            output.add( Vector3.random( randomNumberGenerator ) );
        }
        return output;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link Vector3#crossProductMatrix()} is consistent with {@link Vector3#crossProduct(Vector3)}.
     */
    @Test
    void crossProductMatrixConsistentWithCrossProduct()
    {
        Random randomNumberGenerator = new Random( 42 );
        Vector3 a = Vector3.random( randomNumberGenerator );
        Vector3 b = Vector3.random( randomNumberGenerator );
        Vector3 axb = a.crossProduct( b );
        Vector3 ax_b = a.crossProductMatrix().applyToRealVector3( b );
        assertTrue( axb.equalsApproximately( ax_b , 1.0e-16 ) );
    }
    
}
