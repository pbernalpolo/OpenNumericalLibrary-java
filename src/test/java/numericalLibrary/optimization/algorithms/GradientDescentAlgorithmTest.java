package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
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
        DifferentiableLoss loss = new NormSquaredLossFunction( 42 );
        loss.setParameters( MatrixReal.random( 42 , 1 , new Random( 42 ) ) );
        GradientDescentAlgorithm gd = new GradientDescentAlgorithm( loss );
        gd.setStoppingCriterion( new IterationThresholdStoppingCriterion( 100 ) );
        gd.setLearningRate( 1.0e-1 );
        gd.initialize();
        gd.iterate();
        MatrixReal optimizedParameters = loss.getParameters();
        //System.out.println( optimizedParameters.distanceFrom( Matrix.zero( 42 , 1 ) ) );
        assertTrue( optimizedParameters.equalsApproximately( MatrixReal.zero( 42 , 1 ) , 1.0e-3 ) );
    }
    
}
