package numericalLibrary.optimization;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.MatrixReal;



/**
 * Implements test methods for {@link ErrorFunction}.
 * 
 * @param <T>   type of inputs to this {@link ErrorFunction}.
 */
public interface ErrorFunctionTester<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the {@link ErrorFunction} to be tested.
     * 
     * @return  {@link ErrorFunction} to be tested.
     */
    public ErrorFunction<T> getErrorFunction();
    
    
    /**
     * Returns a list of inputs to evaluate the {@link ErrorFunction}.
     * 
     * @return  list of inputs to evaluate the {@link ErrorFunction}.
     */
    public List<T> getInputList();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link ErrorFunction#getJacobian()} and {@link ErrorFunction#getOutput()} return {@link MatrixReal}s with same number of rows.
     */
    @Test
    default void jacobianHasSameRowsAsOutput()
    {
        ErrorFunction<T> errorFunction = this.getErrorFunction();
        List<T> inputList = this.getInputList();
        for( T input : inputList ) {
            // Set input.
            errorFunction.setInput( input );
            // Get output.
            MatrixReal output = errorFunction.getOutput();
            // Get jacobian.
            MatrixReal jacobian = errorFunction.getJacobian();
            // Check that jacobian and output have same number of rows.
            assertEquals( output.rows() , jacobian.rows() );
        }
    }
    
    
    /**
     * Tests that {@link ErrorFunction#getJacobian()} returns a {@link MatrixReal} with as many columns as degrees of freedom of parameter vector.
     */
    @Test
    default void jacobianHasSameColumnsAsDegreesOfFreedomOfParameterVector()
    {
        // Get parameters from model function.
        ErrorFunction<T> modelFunction = this.getErrorFunction();
        // Get the Jacobian.
        MatrixReal jacobian = modelFunction.getJacobian();
        // Check that the Jacobian has as many columns as the number of parameters.
        assertEquals( modelFunction.degreesOfFreedom() , jacobian.columns() );
    }
    
}
