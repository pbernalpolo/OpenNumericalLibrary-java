package numericalLibrary.algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link VectorSpaceElement}.
 * 
 * @param <T>   concrete type of {@link VectorSpaceElement}.
 */
public interface VectorSpaceElementTester<T extends VectorSpaceElement<T>>
    extends AdditiveAbelianGroupElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link VectorSpaceElement#scale(double)} returns new instance.
     */
    @Test
    default void scaleReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( T element : setElements ) {
            double scalar = rnd.nextGaussian();
            assertNotEquals( element , element.scale( scalar ) );
        }
    }
    
    
    /**
     * Tests that {@link VectorSpaceElement#scaleInplace(double)} is consistent with {@link VectorSpaceElement#scale(double)}.
     */
    @Test
    default void scaleInplaceConsistentWithScale()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( T element : setElements ) {
            double scalar = rnd.nextGaussian();
            T scaleResult = element.scale( scalar );
            T scaleInplaceResult = element.scaleInplace( scalar );
            assertTrue( scaleInplaceResult.equals( scaleResult ) );
        }
    }
    
    
    /**
     * Tests that {@link VectorSpaceElement#scaleInplace(double)} returns same instance.
     */
    @Test
    default void scaleInplaceReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( T element : setElements ) {
            double scalar = rnd.nextGaussian();
            assertEquals( element , element.scaleInplace( scalar ) );
        }
    }
    
    
    /**
     * Tests the compatibility of scalar multiplication with field multiplication.
     * That is {@code scalar1 * ( scalar2 * v ) == ( scalar1 * scalar2 ) * v}
     */
    @Test
    default void scalarMultiplicationIsCompatibleWithFieldMultiplication()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( T element : setElements ) {
            double scalar1 = rnd.nextGaussian();
            double scalar2 = rnd.nextGaussian();
            T scaleTwice = element.scale( scalar1 ).scale( scalar2 );  // scalar1 * ( scalar2 * v )
            T scaleOnce = element.scale( scalar1 * scalar2 );  // ( scalar1 * scalar2 ) * v
            assertTrue( scaleTwice.equalsApproximately( scaleOnce , 1.0e-14 ) );
        }
    }
    
    
    /**
     * Tests the identity element of scalar multiplication.
     * That is {@code 1 v == v}.
     */
    @Test
    default void scalarMultiplicationWithIdentityOfField()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            T scaleOne = element.scale( 1.0 );  // 1 * v
            assertTrue( scaleOne.equals( element ) );
        }
    }
    
    
    /**
     * Tests the distributivity of scalar multiplication with respect to vector addition.
     * That is {@code scalar * ( a + b ) == ( scalar * a ) + ( scalar * b )}
     */
    @Test
    default void distributivityOfScalarMultiplicationWithRespectToVectorAddition()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            double scalar = rnd.nextGaussian();
            T factorized = a.add( b ).scale( scalar );  // scalar * ( a + b )
            T distributed = a.scale( scalar ).add( b.scale( scalar ) );  // ( scalar * a ) + ( scalar * b )
            assertTrue( distributed.equalsApproximately( factorized , 1.0e-14 ) );
        }
    }
    
    
    /**
     * Tests the distributivity of scalar multiplication with respect to field addition.
     * That is {@code ( scalar1 + scalar2 ) * element == ( scalar1 * element ) + ( scalar2 * element )}.
     */
    @Test
    default void distributivityOfScalarMultiplicationWithRespectToFieldAddition()
    {
        List<T> setElements = this.getElementList();
        Random rnd = new Random( 42 );
        for( T element : setElements ) {
            double scalar1 = rnd.nextGaussian();
            double scalar2 = rnd.nextGaussian();
            T factorized = element.scale( scalar1 + scalar2 );  // ( scalar1 + scalar2 ) * element
            T distributed = element.scale( scalar1 ).add( element.scale( scalar2 ) );  // ( scalar1 * element ) + ( scalar2 * element )
            assertTrue( distributed.equalsApproximately( factorized , 1.0e-14 ) );
        }
    }
    
}
