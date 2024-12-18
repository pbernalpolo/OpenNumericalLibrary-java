package numericalLibrary.optimization.algorithms;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.optimization.lossFunctions.LocallyQuadraticLoss;
import numericalLibrary.optimization.lossFunctions.NormSquaredLossFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.types.MatrixReal;



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
        loss.setParameters( MatrixReal.random( 42 , 1 , new Random( 42 ) ) );
        LevenbergMarquardtAlgorithm lma = new LevenbergMarquardtAlgorithm( loss );
        lma.setStoppingCriterion( new IterationThresholdStoppingCriterion( 5 ) );
        lma.setDampingFactor( 1.0e-4 );
        lma.initialize();
        lma.iterate();
        MatrixReal optimizedParameters = loss.getParameters();
        //System.out.println( optimizedParameters.distanceFrom( Matrix.zero( 42 , 1 ) ) );
        assertTrue( optimizedParameters.equalsApproximately( MatrixReal.zero( 42 , 1 ) , 1.0e-18 ) );
    }
    
}
