package numericalLibrary.optimization;


import java.util.List;

import numericalLibrary.optimization.stoppingCriteria.IterationThresholdStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.MaximumIterationsWithoutImprovementStoppingCriterion;
import numericalLibrary.optimization.stoppingCriteria.OrOperatorOnStoppingCriteria;
import numericalLibrary.optimization.stoppingCriteria.StoppingCriterion;
import numericalLibrary.types.Matrix;



/**
 * Implements the Levenberg-Marquardt algorithm.
 * 
 * @see <a href>https://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm</a>
 */
public class LevenbergMarquardtAlgorithm<T>
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    private LevenbergMarquardtModelFunction<T> modelFunction;
    
    private List<LevenbergMarquardtEmpiricalPair<T>> empiricalPairs;
    
    private StoppingCriterion stoppingCriterion;
    
    private Matrix thetaInitial;
    
    private Matrix theta;
    
    private Matrix thetaBest;
    
    private Matrix y;
    
    private Matrix f;
    private Matrix J;
    
    private Matrix delta;
    
    private double error;
    private double errorBest;
    
    private int iteration;
    private int iterationBest;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public LevenbergMarquardtAlgorithm()
    {
        // Default stopping criteria.
        MaximumIterationsWithoutImprovementStoppingCriterion first = new MaximumIterationsWithoutImprovementStoppingCriterion( 20 );
        IterationThresholdStoppingCriterion second = new IterationThresholdStoppingCriterion( 1000 );
        this.stoppingCriterion = new OrOperatorOnStoppingCriteria( first , second );
        // Initialize best error.
        this.errorBest = Double.MAX_VALUE;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public void setEmpiricalPairs( List<LevenbergMarquardtEmpiricalPair<T>> theEmpiricalPairs )
    {
        this.empiricalPairs = theEmpiricalPairs;
    }
    
    
    public void setModelFunction( LevenbergMarquardtModelFunction<T> theModelFunction )
    {
        this.modelFunction = theModelFunction;
    }
    
    
    public void setInitialGuess( Matrix initialGuess )
    {
        this.thetaInitial = initialGuess;
    }
    
    
    public void setStoppingCriterion( StoppingCriterion theStoppingCriterion )
    {
        this.stoppingCriterion = theStoppingCriterion;
    }
    
    
    public void initialize()
    {
        this.theta = thetaInitial.copy();
        this.thetaBest = thetaInitial.copy();
        // Build y vector.
        this.y = Matrix.empty( this.empiricalPairs.size() , 1 );
        for( int i=0; i<this.empiricalPairs.size(); i++ )
        {
            this.y.setEntry( i,0 , this.empiricalPairs.get( i ).getY() );
        }
        // Allocate space for f and J.
        this.f = Matrix.empty( this.empiricalPairs.size() , 1 );
        this.J = Matrix.empty( this.empiricalPairs.size() , this.theta.rows() );
        // Initialize iterations.
        this.iteration = 0;
    }
    
    
    public void step()
    {
        // Build the f vector and the Jacobian matrix.
        this.modelFunction.setParameters( this.theta );
        for( int i=0; i<this.empiricalPairs.size(); i++ )
        {
            this.modelFunction.setInput( this.empiricalPairs.get( i ).getX() );
            f.setEntry( i,0 , this.modelFunction.getOutput() );
            J.setSubmatrix( i,0 , this.modelFunction.getJacobian() );
        }
        // Compute delta.
        Matrix JT = J.transpose();
        Matrix JTJ = JT.multiply( J );
        Matrix fMinusY = f.subtractInplace( y );
        Matrix JTfMinusY = JT.multiply( fMinusY );
        JTJ.choleskyDecompositionInplace();
        this.delta = JTfMinusY.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( JTJ );
        // Update theta.
        this.theta.subtractInplace( this.delta );
        // Update iteration.
        this.iteration++;
        // Update error.
        this.error = fMinusY.transpose().multiply( fMinusY ).entry( 0,0 );
        if( this.error < this.errorBest ) {
            this.errorBest = this.error;
            this.thetaBest = this.theta;
            this.iterationBest = this.iteration;
        }
    }
    
    
    public void iterate()
    {
        this.initialize();
        // Iterate.
        do {
            this.step();
        } while( !this.stoppingCriterion.isFinished( this ) );
    }
    
    
    public Matrix getSolutionLast()
    {
        return this.theta;
    }
    
    
    public Matrix getSolutionBest()
    {
        return this.thetaBest;
    }
    
    
    public int getIterationLast()
    {
        return this.iteration;
    }
    
    
    public int getIterationBest()
    {
        return this.iterationBest;
    }
    
    
    public double getErrorLast()
    {
        return this.error;
    }
    
    
    public double getErrorBest()
    {
        return this.errorBest;
    }
    
}
