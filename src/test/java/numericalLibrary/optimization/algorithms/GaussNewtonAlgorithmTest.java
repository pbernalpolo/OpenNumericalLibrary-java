package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.optimization.lossFunctions.NormSquaredLossFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Implements test methods for {@link GaussNewtonAlgorithm}.
 */
public class GaussNewtonAlgorithmTest
{
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Checks that {@link GaussNewtonAlgorithm} converges to zero in one iteration when the {@link NormSquaredLossFunction} is used.
     */
    @Test
    public void gaussNewtonOnNormSquaredConvergesInOneIteration()
    {
        LocallyQuadraticLoss loss = new NormSquaredLossFunction( 42 );
        loss.setParameters( Matrix.random( 1 , 42 , new Random() ) );
        GaussNewtonAlgorithm gna = new GaussNewtonAlgorithm( loss );
        gna.setStoppingCriterion( new IterationThresholdStoppingCriterion( 100 ) );
        gna.initialize();
        gna.step();
        Matrix optimizedParameters = loss.getParameters();
        assertTrue( optimizedParameters.equalsApproximately( Matrix.zero( 1 , 42 ) , 1.0e-6 ) );
    }
    
}
