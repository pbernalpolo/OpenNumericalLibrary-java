package numericalLibrary.algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link SetElement}.
 * 
 * @param <T>   concrete type of {@link SetElement}.
 */
public interface SetElementTester<T extends SetElement<T>>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns a list of {@link SetElement}s to be tested.
     * <p>
     * The list must contain at least 3 elements.
     * 
     * @return  list of {@link SetElement}s to be tested.
     */
    public List<T> getElementList();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link SetElement#equals(SetElement)} returns true if elements are the same, and false otherwise.
     */
    @Test
    default void elementIsEqualToItselfAndDifferentToOthers()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            if( a == b ) {
                assertTrue( a.equals( b ) );
            }
        }
    }
    
    
    /**
     * Tests that {@link SetElement#equalsApproximately(SetElement, double)} returns true if {@link SetElement#equals(SetElement)} returns true.
     */
    @Test
    default void equalsApproximatelyReturnsTrueIfEqualsReturnsTrue()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            if( a.equals( b ) ) {
                assertTrue( a.equalsApproximately( b , 1.0e-7 ) );
            }
        }
    }
    
    
    /**
     * Tests that {@link SetElement#toString()} returns the same {@link String} if elements are equal, and different string if elements are different.
     */
    @Test
    default void toStringAreEqualIfElementsAreEqual()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            assertEquals( a.equals( b ) , a.toString().equals( b.toString() ) );
        }
    }
    
    
    /**
     * Tests that {@link SetElement#setTo(SetElement)} returns element equal to {@code other}.
     */
    @Test
    default void setToReturnsEqualElement()
    {
        List<T> setElements = this.getElementList();
        for( int i=1; i<setElements.size(); i++ ) {
            T first = setElements.get( 0 );
            T second = setElements.get( i );
            assertTrue( first.setTo( second ).equals( second ) );
        }
    }
    
    
    /**
     * Tests that {@link SetElement#setTo(SetElement)} returns same instance.
     */
    @Test
    default void setToReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.setTo( element ) );
        }
    }
    
    
    /**
     * Tests that {@link SetElement#copy()} returns object equal to {@code this}.
     */
    @Test
    default void copyReturnsEqualToThis()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertTrue( element.copy().equals( element ) );
        }
    }
    
    
    /**
     * Tests that {@link SetElement#copy()} returns new instance.
     */
    @Test
    default void copyReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertNotEquals( element , element.copy() );
        }
    }
    
    
    /**
     * Tests that {@link SetElement#print()} returns same instance.
     */
    @Test
    default void printReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        T element = setElements.get( 0 );
        assertEquals( element , element.print() );
        /*for( T element : setElements ) {
            assertEquals( element , element.print() );
        }*/
    }
    
}
