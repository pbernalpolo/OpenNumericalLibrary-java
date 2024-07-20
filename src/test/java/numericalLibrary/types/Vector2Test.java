package numericalLibrary.types;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElementTester;
import numericalLibrary.algebraicStructures.MetricSpaceElementTester;
import numericalLibrary.algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link Vector2}.
 */
class Vector2Test
    implements
        AdditiveAbelianGroupElementTester<Vector2>,
        VectorSpaceElementTester<Vector2>,
        MetricSpaceElementTester<Vector2>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<Vector2> getElementList()
    {
        List<Vector2> output = new ArrayList<Vector2>();
        output.add( Vector2.zero() );
        output.add( Vector2.i() );
        output.add( Vector2.j() );
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            output.add( Vector2.random( randomNumberGenerator ) );
        }
        return output;
    }
    
}
