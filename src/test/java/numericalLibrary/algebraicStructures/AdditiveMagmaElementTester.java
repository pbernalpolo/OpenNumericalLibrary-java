package numericalLibrary.algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link AdditiveAbelianGroupElement}.
 * 
 * @param <T>   concrete type of {@link AdditiveAbelianGroupElement}.
 */
public interface AdditiveMagmaElementTester<T extends AdditiveMagmaElement<T>>
    extends SetElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link AdditiveMagmaElement#add(AdditiveMagmaElement)} returns new instance.
     */
    @Test
    default void addReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i );
            T second = setElements.get( i+1 );
            assertNotEquals( first , first.add( second ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveMagmaElement#addInplace(AdditiveMagmaElement)} is consistent with {@link AdditiveMagmaElement#add(AdditiveMagmaElement)}.
     */
    @Test
    default void addInplaceConsistentWithAdd()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i ).copy();  // "copy" avoids changing the value when using "addInplace".
            T second = setElements.get( i+1 );
            T addResult = first.add( second );
            T addInplaceResult = first.addInplace( second );
            assertTrue( addResult.equals( addInplaceResult ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveMagmaElement#add(AdditiveMagmaElement)} returns same instance.
     */
    @Test
    default void addInplaceReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.addInplace( element ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveMagmaElement#setToSum(AdditiveMagmaElement, AdditiveMagmaElement)} is consistent with {@link AdditiveMagmaElement#add(AdditiveMagmaElement)}.
     */
    @Test
    default void setToSumConsistentWithAdd()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T first = setElements.get( i );
            T second = setElements.get( i+1 );
            T copy = first.copy();
            T addResult = first.add( second );
            T setToSumResult = copy.setToSum( first , second );
            assertTrue( addResult.equals( setToSumResult ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveMagmaElement#setToSum(AdditiveMagmaElement, AdditiveMagmaElement)} returns same instance.
     */
    @Test
    default void setToSumReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.setToSum( element , element ) );
        }
    }
    
}
