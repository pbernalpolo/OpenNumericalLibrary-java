package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.DifferentiableLoss;
import numericalLibrary.optimization.lossFunctions.NormSquaredLossFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link GradientDescentAlgorithm}.
 */
public class GradientDescentAlgorithmTest
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
        loss.setParameters( Matrix.random( 1 , 42 , new Random() ) );
        GradientDescentAlgorithm gd = new GradientDescentAlgorithm( loss );
        gd.setStoppingCriterion( new IterationThresholdStoppingCriterion( 100 ) );
        gd.setLearningRate( 1.0e-1 );
        gd.initialize();
        gd.iterate();
        Matrix optimizedParameters = loss.getParameters();
        assertTrue( optimizedParameters.equalsApproximately( Matrix.zero( 1 , 42 ) , 1.0e-6 ) );
    }
    
}
