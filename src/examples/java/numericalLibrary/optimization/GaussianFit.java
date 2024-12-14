package numericalLibrary.optimization;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.lossFunctions.MeanSquaredErrorFromTargets;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
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
        Matrix parameters = Matrix.fromArrayAsColumn( new double[] { 3.0 , 2.0 , 5.0 } );
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
        gaussianUnknown.setParameters( Matrix.fromArrayAsColumn( new double[] { 1.0 , 0.0 , 1.0 } ) );
        MeanSquaredErrorFromTargets<Double> loss = new MeanSquaredErrorFromTargets<Double>( gaussianUnknown );
        loss.setInputListAndTargetList( inputList , targetList );
        
        // Find a solution using the Levenberg-Marquardt algorithm.
        //GradientDescentAlgorithm algorithm = new GradientDescentAlgorithm( mseLoss );
        //algorithm.setLearningRate( 1.0e-1 );
        LevenbergMarquardtAlgorithm algorithm = new LevenbergMarquardtAlgorithm( loss );
        algorithm.setDampingFactor( 1.0e-4 );
        algorithm.setStoppingCriterion( new MaximumIterationsWithoutImprovementStoppingCriterion( 4 ) );
        
        algorithm.initialize();
        /*loss.updateCost();
        System.out.println( "error after " + algorithm.getIteration() + " iterations: " + loss.getCost() );
        for( int i=0; i<50; i++) {
            algorithm.step();
            loss.updateCost();
            System.out.println( "error after " + algorithm.getIteration() + " iterations: " + loss.getCost() );
        }*/
        algorithm.iterate();
        System.out.println();
        
        System.out.println( "Last solution was obtained after " + algorithm.getIteration() + " iterations, and produces an error of " + loss.getCost() );
        System.out.println( "Last solution is:" );
        gaussianUnknown.getParameters().print();
    }
    
}
