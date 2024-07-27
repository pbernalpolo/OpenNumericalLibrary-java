package numericalLibrary.optimization;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link LevenbergMarquardtModelFunction}.
 * 
 * @param <T>   concrete type of {@link LevenbergMarquardtModelFunction}.
 */
public interface LevenbergMarquardtModelFunctionTester<T>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the {@link LevenbergMarquardtModelFunction} to be tested.
     * 
     * @return  {@link LevenbergMarquardtModelFunction} to be tested.
     */
    public LevenbergMarquardtModelFunction<T> getModelFunction();
    
    
    /**
     * Returns a list of inputs to evaluate the {@link LevenbergMarquardtModelFunction}.
     * <p>
     * The size of the list must be the same as the size returned by {@link #getParameterList()}.
     * 
     * @return  list of inputs to evaluate the {@link LevenbergMarquardtModelFunction}.
     */
    public List<T> getInputList();
    
    
    /**
     * Returns a list of parameters to evaluate the {@link LevenbergMarquardtModelFunction}.
     * <p>
     * The size of the list must be the same as the size returned by {@link #getInputList()}.
     * 
     * @return  list of parameters to evaluate the {@link LevenbergMarquardtModelFunction}.
     */
    public List<Matrix> getParameterList();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Checks that {@link #getInputList()} has same size as {@link #getParameterList()}.
     */
    @Test
    default void getInputListAndGetParameterListHaveSameSize()
    {
        assertEquals( this.getInputList().size() , this.getParameterList().size() );
    }
    
    
    /**
     * Tests that {@link LevenbergMarquardtModelFunction#getParameters()} returns a column {@link Matrix}.
     */
    @Test
    default void getParametersReturnColumnMatrix()
    {
        // Get parameters from model function.
        LevenbergMarquardtModelFunction<T> modelFunction = this.getModelFunction();
        Matrix parameters = modelFunction.getParameters();
        // They must be a column matrix.
        assertEquals( 1 , parameters.cols() );
    }
    
    
    /**
     * Tests that calling {@link LevenbergMarquardtModelFunction#getParameters()}, then {@link LevenbergMarquardtModelFunction#setParameters(Matrix)}, and then {@link LevenbergMarquardtModelFunction#getParameters()} again returns same parameters we started with.
     */
    @Test
    default void setParametersGetParametersDoesNotChangeParameters()
    {
        // Get model function.
        LevenbergMarquardtModelFunction<T> modelFunction = this.getModelFunction();
        // Get parameter list.
        List<Matrix> parameterList = this.getParameterList();
        for( int i=0; i<parameterList.size(); i++ ) {
            // Take parameters.
            Matrix parameters0 = parameterList.get( i );
            // Set parameters in model function,
            modelFunction.setParameters( parameters0 );
            // and get parameters back.
            Matrix parameters = modelFunction.getParameters();
            // Check that we obtain same parameters again.
            assertTrue( parameters.equals( parameters0 ) );
        }
    }
    
    
    /**
     * Tests that different inputs and parameters produce a different output.
     */
    @Test
    default void differentInputsProduceDifferentOutputs()
    {
        LevenbergMarquardtModelFunction<T> modelFunction = this.getModelFunction();
        List<Matrix> parameterList = this.getParameterList();
        List<T> inputList = this.getInputList();
        for( int i=0; i<inputList.size()-1; i++ ) {
            // Produce output with parameter and input 1.
            modelFunction.setParameters( parameterList.get( i ) );
            modelFunction.setInput( inputList.get( i ) );
            double output1 = modelFunction.getOutput();
            // Produce output with parameter and input 2.
            modelFunction.setParameters( parameterList.get( i+1 ) );
            modelFunction.setInput( inputList.get( i+1 ) );
            double output2 = modelFunction.getOutput();
            // Check that the outputs are different.
            assertNotEquals( output1 , output2 );
        }
    }
    
    
    /**
     * Tests that different inputs and parameters produce a different Jacobian.
     */
    @Test
    default void differentInputsProduceDifferentJacobian()
    {
        LevenbergMarquardtModelFunction<T> modelFunction = this.getModelFunction();
        List<Matrix> parameterList = this.getParameterList();
        List<T> inputList = this.getInputList();
        for( int i=0; i<inputList.size()-1; i++ ) {
            // Produce output with parameter and input 1.
            modelFunction.setParameters( parameterList.get( i ) );
            modelFunction.setInput( inputList.get( i ) );
            Matrix jacobian1 = modelFunction.getJacobian();
            // Produce output with parameter and input 2.
            modelFunction.setParameters( parameterList.get( i+1 ) );
            modelFunction.setInput( inputList.get( i+1 ) );
            Matrix jacobian2 = modelFunction.getJacobian();
            // Check that the Jacobians are different.
            assertFalse( jacobian1.equals( jacobian2 ) );
        }
    }
    
    
    /**
     * Tests that {@link LevenbergMarquardtModelFunction#getJacobian()} returns a row {@link Matrix}.
     */
    @Test
    default void getJacobianReturnsRowMatrix()
    {
        LevenbergMarquardtModelFunction<?> modelFunction = this.getModelFunction();
        assertEquals( 1 , modelFunction.getJacobian().rows() );
    }
    
    
    /**
     * Tests that {@link LevenbergMarquardtModelFunction#getJacobian()} returns a {@link Matrix} with as many columns as parameters.
     */
    @Test
    default void jacobianHasSameColumnsAsParameters()
    {
        // Get parameters from model function.
        LevenbergMarquardtModelFunction<T> modelFunction = this.getModelFunction();
        Matrix parameters = modelFunction.getParameters();
        // Get the Jacobian.
        Matrix jacobian = modelFunction.getJacobian();
        // Check that the Jacobian has as many columns as the number of parameters.
        assertEquals( parameters.rows() , jacobian.cols() );
    }
    
}
