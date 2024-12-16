package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.manifolds.DifferentiableAtlas;
import numericalLibrary.types.Matrix;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Base class for each {@link DifferentiableAtlas} mapping {@link UnitQuaternion}s to {@link Vector3}s.
 * <p>
 * Extends {@link UnitQuaternionAtlas} providing the Jacobian of the transition map.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 */
public abstract class UnitQuaternionDifferentiableAtlas
    extends UnitQuaternionAtlas
    implements DifferentiableAtlas<UnitQuaternion, Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns the Jacobian of the transition map.
     * <p>
     * The transition map is defined by 2 charts; first centered at  q0  and second centered at  p0.
     * Any {@link UnitQuaternion}  q  can be expressed from both charts as:
     * <ul>
     *  <li> q = q0 * delta_q0_q
     *  <li> q = p0 * delta_p0_q
     * </ul>
     * Then, we can define the transition map from the chart centered at q0 to the chart centered at p0 as:
     * e_p0 = phi( delta_p0_q )
     *      = phi( p0^{-1} * q )
     *      = phi( p0^{-1} * q0 * delta_q0_q )
     *      = phi( p0^{-1} * q0 * phi^{-1}( e_q0 ) )
     * The input quaternion {@code delta} is p0^{-1} * q0 .
     * 
     * @param delta     {@link UnitQuaternion} that defines the transition map.
     */
    public abstract Matrix jacobianOfTransitionMap( UnitQuaternion delta );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public Matrix jacobianOfTransitionMap( UnitQuaternion initialChartSelector , UnitQuaternion finalChartSelector )
    {
        this.setChartSelector( initialChartSelector );
        UnitQuaternion delta = this.toChartSelectorPerspective( finalChartSelector );
        return this.jacobianOfTransitionMap( delta );
    }
    
}
