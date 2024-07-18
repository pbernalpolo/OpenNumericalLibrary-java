package algebraicStructures;


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
public interface AdditiveAbelianGroupElementTester<T extends AdditiveAbelianGroupElement<T>>
    extends AdditiveMagmaElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that addition is associative.
     * That is {@code a + ( b + c ) == ( a + b ) + c}.
     */
    @Test
    default void additionIsAssociative()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-2; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            T c = setElements.get( i+2 );
            T sum1 = a.add( b.add( c ) );  // a + ( b + c )
            T sum2 = ( a.add( b ) ).add( c );  // ( a + b ) + c
            assertTrue( sum1.equalsApproximately( sum2 , 1.0e-14 ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#identityAdditive()} returns the identity element under the addition operation.
     * That is {@code 0 + e == e + 0}.
     */
    @Test
    default void setToZeroReturnsIdentityUnderAddition()
    {
        List<T> setElements = this.getElementList();
        T zero = setElements.get( 0 ).copy().setToZero();
        for( T element : setElements ) {
            T identityLeft = zero.add( element );  // 0 + e
            T identityRight = element.add( zero );  // e + 0
            assertTrue( identityLeft.equals( element ) );
            assertTrue( identityRight.equals( element ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#identityAdditive()} returns new instance.
     */
    @Test
    default void identityAdditiveReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertNotEquals( element , element.identityAdditive() );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#setToZero()} is consistent with {@link AdditiveAbelianGroupElement#identityAdditive()}.
     */
    @Test
    default void setToZeroConsistentWithIdentityAdditive()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            T one = element.identityAdditive();
            element.setToZero();
            assertTrue( element.equals( one ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#setToZero()} returns same instance.
     */
    @Test
    default void setToZeroReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.setToZero() );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#inverseAdditive()} returns the additive inverse, or opposite.
     * That is {@code e + (-e) == (-e) + e}.
     */
    @Test
    default void inverseAdditiveBehavesAsExpected()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            T zero = element.copy().setToZero();
            T elementPlusInverse = element.add( element.inverseAdditive() );  // e + (-e)
            T inversePlusElement = element.inverseAdditive().add( element );  // (-e) + e
            assertTrue( elementPlusInverse.equals( zero ) );
            assertTrue( inversePlusElement.equals( zero ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#inverseAdditive()} returns new instance.
     */
    @Test
    default void inverseAdditiveReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                assertNotEquals( element , element.inverseAdditive() );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#inverseAdditiveInplace()} is consistent with {@link AdditiveAbelianGroupElement#inverseAdditive()}.
     */
    @Test
    default void inverseAdditiveInplaceConsistentWithInverseAdditive()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            T inverseAdditiveResult = element.inverseAdditive();
            T inverseAdditiveInplaceResult = element.inverseAdditiveInplace();
            assertTrue( inverseAdditiveInplaceResult.equals( inverseAdditiveResult ) );
        }
    }
    
    
    /**
     * Tests that {@link AdditiveAbelianGroupElement#inverseAdditiveInplace()} returns same instance.
     */
    @Test
    default void inverseAdditiveInplaceReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                assertEquals( element , element.inverseAdditiveInplace() );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
    
    /**
     * Tests that addition is commutative.
     * That is {@code a + b == b + a}.
     */
    @Test
    default void additionIsCommutative()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            T ab = a.add( b );  // a + b
            T ba = b.add( a );  // b + a
            assertTrue( ab.equals( ba ) );
        }
    }
    
}
