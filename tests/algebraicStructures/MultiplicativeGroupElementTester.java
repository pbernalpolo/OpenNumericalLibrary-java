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
public interface MultiplicativeGroupElementTester<T extends MultiplicativeGroupElement<T>>
    extends MultiplicativeMagmaElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that multiplication is associative.
     * That is {@code a * ( b * c ) == ( a * b ) * c}.
     */
    @Test
    default void multiplicationIsAssociative()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-2; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            T c = setElements.get( i+2 );
            T product1 = a.multiply( b.multiply( c ) );  // a * ( b * c )
            T product2 = ( a.multiply( b ) ).multiply( c );  // ( a * b ) * c
            assertTrue( product1.equalsApproximately( product2 , 1.0e-14 ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#identityMultiplicative()} returns the identity element under the multiplication operation.
     * That is {@code 1 * e == e * 1}.
     */
    @Test
    default void identityMultiplicativeReturnsIdentityUnderMultiplication()
    {
        List<T> setElements = this.getElementList();
        T one = setElements.get( 0 ).identityMultiplicative();
        for( T element : setElements ) {
            T identityLeft = one.multiply( element );  // 1 * e
            T identityRight = element.multiply( one );  // e * 1
            assertTrue( identityLeft.equals( element ) );
            assertTrue( identityRight.equals( element ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#identityMultiplicative()} returns new instance.
     */
    @Test
    default void identityMultiplicativeReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertNotEquals( element , element.identityMultiplicative() );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#setToOne()} is consistent with {@link MultiplicativeGroupElement#identityMultiplicative()}.
     */
    @Test
    default void setToOneConsistentWithIdentityMultiplicative()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            T one = element.identityMultiplicative();
            element.setToOne();
            assertTrue( element.equals( one ) );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#setToOne()} returns same instance.
     */
    @Test
    default void setToOneReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            assertEquals( element , element.setToOne() );
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#inverseMultiplicative()} returns the multiplicative inverse, or reciprocal.
     * That is {@code e * e^{-1} == e^{-1} * e}.
     */
    @Test
    default void inverseMultiplicativeBehavesAsExpected()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                T one = element.identityMultiplicative();
                T elementTimesInverse = element.multiply( element.inverseMultiplicative() );  // e * e^{-1}
                T inverseTimesElement = element.inverseMultiplicative().multiply( element );  // e^{-1} * e
                assertTrue( elementTimesInverse.equalsApproximately( one , 1.0e-10 ) );
                assertTrue( inverseTimesElement.equalsApproximately( one , 1.0e-10 ) );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#inverseMultiplicative()} returns new instance.
     */
    @Test
    default void inverseMultiplicativeReturnsNewInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                assertNotEquals( element , element.inverseMultiplicative() );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#inverseMultiplicativeInplace()} is consistent with {@link MultiplicativeGroupElement#inverseMultiplicative()}.
     */
    @Test
    default void inverseMultiplicativeInplaceConsistentWithInverseMultiplicative()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                T inverseMultiplicativeResult = element.inverseMultiplicative();
                T inverseMultiplicativeInplaceResult = element.inverseMultiplicativeInplace();
                assertTrue( inverseMultiplicativeInplaceResult.equals( inverseMultiplicativeResult ) );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
    
    /**
     * Tests that {@link MultiplicativeGroupElement#inverseMultiplicativeInplace()} returns same instance.
     */
    @Test
    default void inverseMultiplicativeInplaceReturnsSameInstance()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            try {
                assertEquals( element , element.inverseMultiplicativeInplace() );
            } catch( IllegalArgumentException e ) {
                // This could happen if the multiplicative inverse is undefined.
            }
        }
    }
    
}
