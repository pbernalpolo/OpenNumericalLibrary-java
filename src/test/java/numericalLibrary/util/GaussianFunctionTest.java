package numericalLibrary.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.optimization.ModelFunctionTester;
import numericalLibrary.types.MatrixReal;



/**
 * Implements test methods for {@link GaussianFunction}.
 */
class GaussianFunctionTest
    implements
        ModelFunctionTester<Double>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public ModelFunction<Double> getModelFunction()
    {
        return new GaussianFunction();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Double> getInputList()
    {
        List<Double> output = new ArrayList<Double>();
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            output.add( randomNumberGenerator.nextGaussian() );
        }
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<MatrixReal> getParameterList()
    {
        List<MatrixReal> output = new ArrayList<MatrixReal>();
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            output.add( MatrixReal.fromArrayAsColumn( new double[] {
                    randomNumberGenerator.nextGaussian() ,
                    randomNumberGenerator.nextGaussian() ,
                    randomNumberGenerator.nextGaussian() } ) );
        }
        return output;
    }
    
}
