package numericalLibrary.algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link FieldElement}.
 * 
 * @param <T>   concrete type of {@link FieldElement}.
 */
public interface FieldElementTester<T extends FieldElement<T>>
    extends
        AdditiveAbelianGroupElementTester<T>,
        MultiplicativeAbelianGroupElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link MultiplicativeAbelianGroupElement#multiply(Multipliable)} distributes over {@link AdditiveAbelianGroupElement#add(Addable)}.
     * That is {@code a * ( b + c ) == ( a * b ) + ( a * c ) }.
     */
    @Test
    default void multiplicationDistributesOverAddition()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-2; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            T c = setElements.get( i+2 );
            T factorized = a.multiply( b.add( c ) );  // a * ( b + c )
            T distributed = a.multiply( b ).add( a.multiply( c ) );  // ( a * b ) + ( a * c )
            assertTrue( factorized.equalsApproximately( distributed , 1.0e-14 ) );
        }
    }
    
}
