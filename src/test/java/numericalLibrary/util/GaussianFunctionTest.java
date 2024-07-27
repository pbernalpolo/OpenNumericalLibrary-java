package numericalLibrary.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import numericalLibrary.optimization.LevenbergMarquardtModelFunction;
import numericalLibrary.optimization.LevenbergMarquardtModelFunctionTester;
import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link GaussianFunction}.
 */
class GaussianFunctionTest
    implements
        LevenbergMarquardtModelFunctionTester<Double>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public LevenbergMarquardtModelFunction<Double> getModelFunction()
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
    public List<Matrix> getParameterList()
    {
        List<Matrix> output = new ArrayList<Matrix>();
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<100; i++ ) {
            output.add( Matrix.columnFromArray( new double[] {
                    randomNumberGenerator.nextGaussian() ,
                    randomNumberGenerator.nextGaussian() ,
                    randomNumberGenerator.nextGaussian() } ) );
        }
        return output;
    }
    
}
