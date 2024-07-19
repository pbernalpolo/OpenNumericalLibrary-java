package algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link MultiplicativeAbelianGroupElement}.
 * 
 * @param <T>   concrete type of {@link MultiplicativeAbelianGroupElement}.
 */
public interface MultiplicativeAbelianGroupElementTester<T extends MultiplicativeAbelianGroupElement<T>>
    extends MultiplicativeGroupElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that multiplication is commutative.
     * That is {@code a * b == b * a}.
     */
    @Test
    default void multiplicationIsCommutative()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            T ab = a.multiply( b );  // a * b
            T ba = b.multiply( a );  // b * a
            assertTrue( ab.equals( ba ) );
        }
    }
    
}
