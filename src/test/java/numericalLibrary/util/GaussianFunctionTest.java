package numericalLibrary.util;


import numericalLibrary.optimization.LevenbergMarquardtModelFunction;
import numericalLibrary.optimization.LevenbergMarquardtModelFunctionTest;



/**
 * Implements test methods for {@link ComplexNumber}.
 */
class GaussianFunctionTest
    implements
        LevenbergMarquardtModelFunctionTest
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public LevenbergMarquardtModelFunction<?> getModelFunction()
    {
        return new GaussianFunction();
    }
    
}
