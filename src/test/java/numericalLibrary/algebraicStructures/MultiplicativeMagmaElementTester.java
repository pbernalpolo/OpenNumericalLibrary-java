package algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link MultiplicativeGroupElement}.
 * 
 * @param <T>   concrete type of {@link MultiplicativeGroupElement}.
 */
public interface MultiplicativeMagmaElementTester<T extends MultiplicativeMagmaElement<T>>
    extends SetElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link MultiplicativeMagmaElement#multiply(MultiplicativeMagmaElement)} returns new instance.
     */
    @Test
    default void multiplyReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i );
            T second = setElements.get( i+1 );
            assertNotEquals( first , first.multiply( second ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeMagmaElement#multiplyInplace(MultiplicativeMagmaElement)} is consistent with {@link MultiplicativeMagmaElement#multiply(MultiplicativeMagmaElement)}.
     */
    @Test
    default void multiplyInplaceConsistentWithMultiply()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i ).copy();  // "copy" avoids changing the value when using "multiplyInplace".
            T second = setElements.get( i+1 );
            T multiplyResult = first.multiply( second );
            T multiplyInplaceResult = first.multiplyInplace( second );
            assertTrue( multiplyResult.equals( multiplyInplaceResult ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeMagmaElement#multiplyInplace(MultiplicativeMagmaElement)} returns same instance.
     */
    @Test
    default void multiplyInplaceReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.multiplyInplace( element ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeMagmaElement#setToProduct(MultiplicativeMagmaElement, MultiplicativeMagmaElement)} is consistent with {@link MultiplicativeMagmaElement#multiply(MultiplicativeMagmaElement)}.
     */
    @Test
    default void setToProductConsistentWithMultiply()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i );
            T second = setElements.get( i+1 );
            T copy = first.copy();
            T multiplyResult = first.multiply( second );
            T setToProductResult = copy.setToProduct( first , second );
            assertTrue( multiplyResult.equals( setToProductResult ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeMagmaElement#setToProduct(MultiplicativeMagmaElement, MultiplicativeMagmaElement)} returns same instance.
     */
    @Test
    default void setToProductReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.setToProduct( element , element ) );
        }
    }
    
}
