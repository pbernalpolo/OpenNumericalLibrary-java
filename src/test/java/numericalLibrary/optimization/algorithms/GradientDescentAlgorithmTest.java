package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.NormSquaredLossFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.types.MatrixReal;



/**
 * Implements test methods for {@link GradientDescentAlgorithm}.
 */
class GradientDescentAlgorithmTest
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Checks that {@link GradientDescentAlgorithm} converges to zero when the {@link NormSquaredLossFunction} is used.
     */
    @Test
    public void gradientDescentOnNormSquaredGoesToZero()
    {
    	NormSquaredLossFunction loss = new NormSquaredLossFunction( 42 );
        loss.setParameters( MatrixReal.random( 42 , 1 , new Random( 42 ) ) );
        GradientDescentAlgorithm gd = new GradientDescentAlgorithm();
        gd.setLearningRate( 1.0e-1 );
        gd.iterate( loss , new IterationThresholdStoppingCriterion( 100 ) );
        MatrixReal optimizedParameters = loss.getParameters();
        //System.out.println( optimizedParameters.distanceFrom( Matrix.zero( 42 , 1 ) ) );
        assertTrue( optimizedParameters.equalsApproximately( MatrixReal.zero( 42 , 1 ) , 1.0e-3 ) );
    }
    
}
