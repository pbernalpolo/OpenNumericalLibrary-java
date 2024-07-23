package numericalLibrary.optimization;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link LevenbergMarquardtModelFunction}.
 * 
 * @param <T>   concrete type of {@link LevenbergMarquardtModelFunction}.
 */
public interface LevenbergMarquardtModelFunctionTest<T>
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
     * Tests that different inputs and parameters produce a different output.
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
            assertNotEquals( jacobian1 , jacobian2 );
        }
    }
    
    
    /**
     * Tests that {@link LevenbergMarquardtModelFunction#getJacobian()} returns a row matrix.
     */
    @Test
    default void getJacobianReturnsRowMatrix()
    {
        LevenbergMarquardtModelFunction<?> modelFunction = this.getModelFunction();
        assertEquals( 1 , modelFunction.getJacobian().rows() );
    }
    
}
