package numericalLibrary.optimization.gaussianFit;


import java.util.ArrayList;
import java.util.List;

import numericalLibrary.optimization.algorithms.GradientDescentAlgorithm;
import numericalLibrary.optimization.algorithms.IterativeOptimizationAlgorithm;
import numericalLibrary.optimization.algorithms.LevenbergMarquardtAlgorithm;
import numericalLibrary.optimization.lossFunctions.PlainMeanSquaredErrorLocallyQuadraticLoss;
import numericalLibrary.optimization.lossFunctions.RobustMeanSquaredErrorLocallyQuadraticLoss;
import numericalLibrary.optimization.robustFunctions.MaximumDistanceRobustFunction;
import numericalLibrary.optimization.robustFunctions.RobustFunction;
import numericalLibrary.types.MatrixReal;



/**
 * Example that shows how to use the optimization package.
 * <p>
 * In particular, some data is fit to a Gaussian function.
 */
class GaussianFitExample
{
    ////////////////////////////////////////////////////////////////
    // ENTRY POINT
    ////////////////////////////////////////////////////////////////
    
    public static void main( String[] args )
    {
        // Generate a known Gaussian function.
        GaussianFunction gaussianKnown = new GaussianFunction();
        gaussianKnown.setHeight( 3.0 );
        gaussianKnown.setCenter( 2.0 );
        gaussianKnown.setStandardDeviation( 5.0 );
        
        // Sample with the known Gaussian function.
        List<GaussianFunctionFitErrorFunctionInput> inputTargetList = new ArrayList<GaussianFunctionFitErrorFunctionInput>();
        for( int m=0; m<10; m++ )
        {
            double x = m/5.0;
            gaussianKnown.setInput( x );
            inputTargetList.add( new GaussianFunctionFitErrorFunctionInput( x , gaussianKnown.getOutput() ) );
        }
        
        // Create a new Gaussian function to find the parameters that best fit to the generated data.
        GaussianFunction gaussianUnknown = new GaussianFunction();
        gaussianUnknown.setHeight( 1.0 );
        gaussianUnknown.setCenter( 0.0 );
        gaussianUnknown.setStandardDeviation( 1.0 );
        
        // Create the error function that defines the error to be optimized.
        GaussianFunctionFitErrorFunction errorFunction = new GaussianFunctionFitErrorFunction( gaussianUnknown );
        
        // Use the error function to create the loss function to be optimized.
        //PlainMeanSquaredErrorLocallyQuadraticLoss<GaussianFunctionFitErrorFunctionInput> loss = new PlainMeanSquaredErrorLocallyQuadraticLoss<GaussianFunctionFitErrorFunctionInput>( errorFunction );
        RobustMeanSquaredErrorLocallyQuadraticLoss<GaussianFunctionFitErrorFunctionInput> loss = new RobustMeanSquaredErrorLocallyQuadraticLoss<GaussianFunctionFitErrorFunctionInput>( errorFunction );
        loss.setRobustFunction( new MaximumDistanceRobustFunction( 1.0e1 ) );
        loss.setInputList( inputTargetList );
        
        // Find a solution using some IterativeOptimizationAlgorithm.
        //GradientDescentAlgorithm algorithm = new GradientDescentAlgorithm();
        //algorithm.setLearningRate( 1.0e-1 );
        LevenbergMarquardtAlgorithm algorithm = new LevenbergMarquardtAlgorithm();
        algorithm.setDampingFactor( 1.0e-8 );
        
        System.out.println( "error after 0 iterations: " + loss.getCost() );
        for( int i=0; i<20; i++) {
            algorithm.step( loss );
            System.out.println( "error after " + i + " iterations: " + loss.getCost() );
        }
        System.out.println();
        
        System.out.println( "Last solution is:" );
        System.out.println( "Height: " + gaussianUnknown.getHeight() );
        System.out.println( "Center: " + gaussianUnknown.getCenter() );
        System.out.println( "Standard deviation: " + gaussianUnknown.getStandardDeviation() );
        
    }
    
}
