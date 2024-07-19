package types;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algebraicStructures.FieldElementTester;
import algebraicStructures.MetricSpaceElementTester;
import algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link DualNumber}.
 */
class DualNumberTest
    implements
        FieldElementTester<DualNumber>,
        VectorSpaceElementTester<DualNumber>,
        MetricSpaceElementTester<DualNumber>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<DualNumber> getElementList()
    {
        List<DualNumber> output = new ArrayList<DualNumber>();
        output.add( DualNumber.zero() );
        output.add( DualNumber.zero() );
        output.add( DualNumber.one() );
        output.add( DualNumber.one() );
        output.add( DualNumber.epsilon() );
        output.add( DualNumber.epsilon() );
        Random rng = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            DualNumber r = DualNumber.random( rng );
            output.add( r );
            output.add( r.copy() );
        }
        //output.add( new DualNumber( -1.0 , 0.0 ) );
        //output.add( new DualNumber( 1.0 , 0.0 ) );
        //output.add( new DualNumber( -2.0 , 3.0 ) );
        return output;
    }
    
}
