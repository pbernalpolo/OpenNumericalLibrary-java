package types;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algebraicStructures.FieldElementTester;
import algebraicStructures.MetricSpaceElementTester;
import algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link ComplexNumber}.
 */
class ComplexNumberTest
    implements
        FieldElementTester<ComplexNumber>,
        VectorSpaceElementTester<ComplexNumber>,
        MetricSpaceElementTester<ComplexNumber>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public List<ComplexNumber> getElementList()
    {
        List<ComplexNumber> output = new ArrayList<ComplexNumber>();
        output.add( ComplexNumber.zero() );
        output.add( ComplexNumber.zero() );
        output.add( ComplexNumber.one() );
        output.add( ComplexNumber.one() );
        output.add( ComplexNumber.i() );
        output.add( ComplexNumber.i() );
        Random rng = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            ComplexNumber r = ComplexNumber.random( rng );
            output.add( r );
            output.add( r.copy() );
        }
        return output;
    }
    
}
