package numericalLibrary.optimization.lossFunctions;


import java.util.List;

import numericalLibrary.optimization.ModelFunction;
import numericalLibrary.types.Matrix;



/**
 * {@link LocallyQuadraticLoss} defined as:
 * L(\theta) = \sum_i || f( x_i , \theta ) ||^2
 * where:
 * - f is a {@link ModelFunction},
 * - x_i is the i-th input to the {@link ModelFunction} f,
 * - \theta is the parameter vector.
 * 
 * @param <T>   type of inputs to the {@link ModelFunction} f.
 */
public class MeanSquaredFunctionLQL<T>
    extends MeanLocallyQuadraticLoss<T>
    implements LocallyQuadraticLoss
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link MeanSquaredFunctionLQL}.
     * 
     * @param modelFunction   {@link ModelFunction} used to define the error.
     */
    public MeanSquaredFunctionLQL( ModelFunction<T> modelFunction )
    {
        super( modelFunction );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Sets the list of inputs to the {@link #modelFunction} whose output is to be optimized.
     * 
     * @param modelFunctionInputList     list of inputs to the {@link #modelFunction} whose output is to be optimized.
     */
    public void setInputList( List<T> modelFunctionInputList )
    {
        this.setInputList( modelFunctionInputList );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    protected void cleanItem( int index )
    {
        T input = this.inputList.get( index );
        // Introduce input in function.
        this.modelFunction.setInput( input );
        // Obtain output and Jacobian from function.
        Matrix functionOutput = this.modelFunction.getOutput();
        Matrix functionJacobian = this.modelFunction.getJacobian();
        double outputSquared = functionOutput.normFrobeniusSquared();
        // Add contribution to the jacobian.
        this.J.addProduct( functionOutput.transpose() , functionJacobian );
        // Add contribution to Gauss-Newton matrix.
        this.JTWJ.addProduct( functionJacobian.transpose() , functionJacobian );
        // Add contribution to loss.
        this.cost += outputSquared;
    }
    
}
