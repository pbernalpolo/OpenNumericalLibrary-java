package levenbergMarquardtAlgorithm;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.optimization.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.LevenbergMarquardtEmpiricalPair;
import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.types.Matrix;
import numericalLibrary.util.GaussianFunction;



public class GaussianFit
{

    public GaussianFit()
    {
        // TODO Auto-generated constructor stub
    }


    public static void main( String[] args )
    {
        // Generate a known Gaussian function.
        GaussianFunction gaussianKnown = new GaussianFunction();
        Matrix parameters = Matrix.columnFromArray( new double[] { 3.0 , 2.0 , 5.0 } );
        gaussianKnown.setParameters( parameters );
        
        // Sample with the known Gaussian function.
        List<LevenbergMarquardtEmpiricalPair<Double>> empiricalPairs = new ArrayList<LevenbergMarquardtEmpiricalPair<Double>>();
        for( int m=0; m<10; m++ )
        {
            double x = m/5.0;
            gaussianKnown.setInput( x );
            empiricalPairs.add( new LevenbergMarquardtEmpiricalPair<Double>( gaussianKnown.getOutput() , x ) );
        }
        
        // Find a solution using the Levenberg-Marquardt algorithm.
        LevenbergMarquardtAlgorithm<Double> algorithm = new LevenbergMarquardtAlgorithm<Double>();
        algorithm.setEmpiricalPairs( empiricalPairs );
        algorithm.setInitialGuess( Matrix.columnFromArray( new double[] { 1.0 , 0.0 , 1.0 } ) );
        algorithm.setModelFunction( new GaussianFunction() );
        //algorithm.setStoppingCriterion( new IterationThresholdStoppingCriterion( 1000 ) );
        
        algorithm.iterate();
        
        System.out.println( "Last solution was obtained after " + algorithm.getIterationLast() + " iterations, and produces an error of " + algorithm.getErrorLast() );
        System.out.println( "Last solution is:" );
        algorithm.getSolutionLast().print();
        
        System.out.println( "Best solution was obtained after " + algorithm.getIterationBest() + " iterations, and produces an error of " + algorithm.getErrorBest() );
        System.out.println( "Best solution is:" );
        algorithm.getSolutionBest().print();
    }
    
}
