package numericalLibrary.optimization;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.lossFunctions.MeanSquaredErrorLQL;
import numericalLibrary.optimization.lossFunctions.RobustMeanSquaredErrorLQL;
import numericalLibrary.types.Matrix;
import numericalLibrary.util.GaussianFunction;



/**
 * Example that shows how to set up and use {@link IterativeOptimizationAlgorithm}s to fit a Gaussian function to empirical data.
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
        Matrix parameters = Matrix.fromArrayAsRow( new double[] { 3.0 , 2.0 , 5.0 } );
        gaussianKnown.setParameters( parameters );
        
        // Sample with the known Gaussian function.
        List<Double> inputList = new ArrayList<Double>();
        List<Matrix> targetList = new ArrayList<Matrix>();
        for( int m=0; m<10; m++ )
        {
            double x = m/5.0;
            gaussianKnown.setInput( x );
            inputList.add( x );
            targetList.add( gaussianKnown.getOutput() );
        }
        
        // Define the loss function.
        GaussianFunction gaussianUnknown = new GaussianFunction();
        gaussianUnknown.setParameters( Matrix.fromArrayAsRow( new double[] { 1.0 , 0.0 , 1.0 } ) );
        //MeanSquaredErrorLQL<Double> loss = new MeanSquaredErrorLQL<Double>( gaussianUnknown );
        RobustMeanSquaredErrorLQL<Double> loss = new RobustMeanSquaredErrorLQL<Double>( gaussianUnknown );
        loss.setInputListAndTargetList( inputList , targetList );
        
        // Find a solution using the Levenberg-Marquardt algorithm.
        //GradientDescentAlgorithm algorithm = new GradientDescentAlgorithm( loss );
        LevenbergMarquardtAlgorithm algorithm = new LevenbergMarquardtAlgorithm( loss );
        algorithm.setDampingFactor( 1.0e-4 );
        
        algorithm.initialize();
        System.out.println( "error after " + algorithm.getIteration() + " iterations: " + algorithm.getCost() );
        for( int i=0; i<50; i++) {
            algorithm.step();
            System.out.println( "error after " + algorithm.getIteration() + " iterations: " + algorithm.getCost() );
        }
        //algorithm.iterate();
        System.out.println();
        
        System.out.println( "Last solution was obtained after " + algorithm.getIteration() + " iterations, and produces an error of " + algorithm.getCost() );
        System.out.println( "Last solution is:" );
        gaussianUnknown.getParameters().print();
    }
    
}
