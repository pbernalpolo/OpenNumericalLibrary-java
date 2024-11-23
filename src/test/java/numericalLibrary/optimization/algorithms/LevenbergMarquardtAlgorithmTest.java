package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.optimization.lossFunctions.NormSquaredLossFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link LevenbergMarquardtAlgorithm}.
 */
public class LevenbergMarquardtAlgorithmTest
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Checks that {@link LevenbergMarquardtAlgorithm} converges to zero when the {@link NormSquaredLossFunction} is used.
     */
    @Test
    public void levenbergMarquardtOnNormSquaredConvergesToZero()
    {
        LocallyQuadraticLoss loss = new NormSquaredLossFunction( 42 );
        loss.setParameters( Matrix.random( 1 , 42 , new Random() ) );
        LevenbergMarquardtAlgorithm lma = new LevenbergMarquardtAlgorithm( loss );
        lma.setStoppingCriterion( new IterationThresholdStoppingCriterion( 100 ) );
        lma.setDampingFactor( 1.0e-4 );
        lma.initialize();
        lma.iterate();
        Matrix optimizedParameters = loss.getParameters();
        assertTrue( optimizedParameters.equalsApproximately( Matrix.zero( 1 , 42 ) , 1.0e-6 ) );
    }
    
}
