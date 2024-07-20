package numericalLibrary.types;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElementTester;
import numericalLibrary.algebraicStructures.MetricSpaceElementTester;
import numericalLibrary.algebraicStructures.MultiplicativeGroupElementTester;
import numericalLibrary.algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link Quaternion}.
 */
class QuaternionTest
    implements
        AdditiveAbelianGroupElementTester<Quaternion>,
        MultiplicativeGroupElementTester<Quaternion>,
        VectorSpaceElementTester<Quaternion>,
        MetricSpaceElementTester<Quaternion>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<Quaternion> getElementList()
    {
        List<Quaternion> output = new ArrayList<Quaternion>();
        output.add( Quaternion.zero() );
        output.add( Quaternion.zero() );
        output.add( Quaternion.one() );
        output.add( Quaternion.one() );
        output.add( Quaternion.i() );
        output.add( Quaternion.i() );
        output.add( Quaternion.j() );
        output.add( Quaternion.j() );
        output.add( Quaternion.k() );
        output.add( Quaternion.k() );
        Random rng = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            Quaternion r = Quaternion.random( rng );
            output.add( r );
            output.add( r.copy() );
        }
        return output;
    }
    
}
