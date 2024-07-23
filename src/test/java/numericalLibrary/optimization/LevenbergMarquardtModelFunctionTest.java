package numericalLibrary.optimization;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link LevenbergMarquardtModelFunction}.
 * 
 * @param <T>   concrete type of {@link LevenbergMarquardtModelFunction}.
 */
public interface LevenbergMarquardtModelFunctionTest
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    public LevenbergMarquardtModelFunction<?> getModelFunction();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    @Test
    default void getJacobianReturnsRowMatrix()
    {
        LevenbergMarquardtModelFunction<?> modelFunction = this.getModelFunction();
        assertEquals( 1 , modelFunction.getJacobian().rows() );
    }
    
}
