package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.manifolds.DifferentiableAtlas;
import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.RealNumber;



/**
 * Base class for each {@link DifferentiableAtlas} mapping {@link ComplexNumber}s to {@link RealNumber}s.
 * <p>
 * Extends {@link UnitComplexNumberAtlas} providing the Jacobian of the transition map.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 */
public abstract class UnitComplexNumberDifferentiableAtlas
    extends UnitComplexNumberAtlas
    implements DifferentiableAtlas<ComplexNumber, RealNumber>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the Jacobian of the transition map.
     * <p>
     * The transition map is defined by 2 charts; first centered at  z0  and second centered at  w0.
     * Any {@link ComplexNumber}  z  can be expressed from both charts as:
     * <ul>
     *  <li> z = z0 * delta_z0_z
     *  <li> z = w0 * delta_w0_z
     * </ul>
     * Then, we can define the transition map from the chart centered at z0 to the chart centered at w0 as:
     * e_w0 = phi( delta_w0_z )
     *      = phi( w0^{-1} * z )
     *      = phi( w0^{-1} * z0 * delta_z0_z )
     *      = phi( w0^{-1} * z0 * phi^{-1}( e_z0 ) )
     * The input {@link ComplexNumber} {@code delta} is  w0^{-1} * z0 .
     * 
     * @param delta     {@link ComplexNumber} that defines the transition map.
     */
    public abstract MatrixReal jacobianOfTransitionMap( ComplexNumber delta );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal jacobianOfTransitionMap( ComplexNumber initialChartSelector , ComplexNumber finalChartSelector )
    {
        this.setChartSelector( initialChartSelector );
        ComplexNumber delta = this.toChartSelectorPerspective( finalChartSelector );
        return this.jacobianOfTransitionMap( delta );
    }
    
}
