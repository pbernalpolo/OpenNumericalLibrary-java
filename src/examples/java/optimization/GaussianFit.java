package optimization;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.optimization.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.LeastSquaresDataPair;
import numericalLibrary.optimization.LeastSquaresFunction;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.types.Matrix;
import numericalLibrary.util.GaussianFunction;



/**
 * Example that shows how to set up and use the Levenberg-Marquardt algorithm to fit a Gaussian function to empirical data.
 */
public class GaussianFit
{
    ////////////////////////////////////////////////////////////////
    // ENTRY POINT
    ////////////////////////////////////////////////////////////////
    
    public static void main( String[] args )
    {
        // Generate a known Gaussian function.
        GaussianFunction gaussianKnown = new GaussianFunction();
        Matrix parameters = Matrix.columnFromArray( new double[] { 3.0 , 2.0 , 5.0 } );
        gaussianKnown.setParameters( parameters );
        
        // Sample with the known Gaussian function.
        List<LeastSquaresDataPair<Double>> empiricalPairList = new ArrayList<LeastSquaresDataPair<Double>>();
        for( int m=0; m<10; m++ )
        {
            double x = m/5.0;
            gaussianKnown.setInput( x );
            empiricalPairList.add( new LeastSquaresDataPair<Double>( gaussianKnown.getOutput() , x ) );
        }
        
        // Find a solution using the Levenberg-Marquardt algorithm.
        LevenbergMarquardtAlgorithm<LeastSquaresDataPair<Double>> algorithm = new LevenbergMarquardtAlgorithm<LeastSquaresDataPair<Double>>();
        algorithm.setOptimizableFunctionInputList( empiricalPairList );
        GaussianFunction gaussianUnknown = new GaussianFunction();
        gaussianUnknown.setParameters( Matrix.columnFromArray( new double[] { 1.0 , 0.0 , 1.0 } ) );
        algorithm.setOptimizableFunction( new LeastSquaresFunction<Double>( gaussianUnknown ) );
        //algorithm.setStoppingCriterion( new IterationThresholdStoppingCriterion( 1000 ) );
        algorithm.setDampingFactor( 1.0e-8 );
        
        algorithm.initialize();
        algorithm.iterate();
        
        System.out.println( "Last solution was obtained after " + algorithm.getIterationLast() + " iterations, and produces an error of " + algorithm.getErrorLast() );
        System.out.println( "Last solution is:" );
        algorithm.getSolutionLast().print();
        
        System.out.println( "Best solution was obtained after " + algorithm.getIterationBest() + " iterations, and produces an error of " + algorithm.getErrorBest() );
        System.out.println( "Best solution is:" );
        algorithm.getSolutionBest().print();
    }
    
}
