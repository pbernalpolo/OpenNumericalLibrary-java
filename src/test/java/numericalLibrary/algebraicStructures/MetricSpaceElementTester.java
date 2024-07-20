package numericalLibrary.algebraicStructures;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link MetricSpaceElement}.
 * 
 * @param <T>   concrete type of {@link MetricSpaceElement}.
 */
public interface MetricSpaceElementTester<T extends MetricSpaceElement<T>>
    extends SetElementTester<T>
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that the distance from a point to itself is zero.
     * That is  d(x,x) == 0 .
     */
    @Test
    default void distanceFromItselfIsZero()
    {
        List<T> setElements = this.getElementList();
        for( T element : setElements ) {
            double distance = element.distanceFrom( element );
            assertTrue( distance < 1.0e-7 );
        }
    }
    
    
    /**
     * Tests that the distance between different elements is always positive.
     * That is  x != y  =>  d(x,y) > 0 .
     */
    @Test
    default void positivityOfDistance()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            if( !a.equals( b ) ) {
                double distance = a.distanceFrom( b );
                assertTrue( distance > 0.0 );
            }
        }
    }
    
    
    /**
     * Tests that the distance from x to y is always the same as the distance from y to x.
     * That is  d(x,y) == d(y,x) .
     */
    @Test
    default void symmetryOfDistance()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-1; i++ ) {
            T a = setElements.get( i );
            T b = setElements.get( i+1 );
            double distance1 = a.distanceFrom( b );
            double distance2 = b.distanceFrom( a );
            assertEquals( distance1 , distance2 );
        }
    }
    
    
    /**
     * Tests that the distance satisfies the triangle inequality.
     * That is  d(x,z) <= d(x,y) + d(y,z)
     */
    @Test
    default void triangleInequalityForDistance()
    {
        List<T> setElements = this.getElementList();
        for( int i=0; i<setElements.size()-2; i++ ) {
            T x = setElements.get( i );
            T y = setElements.get( i+1 );
            T z = setElements.get( i+2 );
            double distanceXZ = x.distanceFrom( z );
            double distanceXY = x.distanceFrom( y );
            double distanceYZ = y.distanceFrom( z );
            assertTrue( distanceXZ <= distanceXY + distanceYZ );
        }
    }
    
}
