package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.manifolds.Atlas;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Base class for each {@link Atlas} mapping {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * More concretely, {@link UnitQuaternionAtlas} maps 3d rotation transformations to {@link Vector3}s in the Euclidean space.
 * Since both  q  and  -q  represent the same 3d rotation transformation, they will be mapped to the same {@link Vector3}.
 * <p>
 * A chart is defined for each {@link UnitQuaternion}.
 * Such chart transforms the {@link UnitQuaternion} associated with the chart with the origin of the Euclidean space.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 */
public abstract class UnitQuaternionAtlas
    implements Atlas<UnitQuaternion, Vector3>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Defines the map from {@link UnitQuaternion}s to {@link Vector3}s for the chart centered at the identity {@link UnitQuaternion}.
     * <p>
     * The identity {@link UnitQuaternion} is mapped with the zero {@link Vector3}.
     * 
     * @param q     {@link UnitQuaternion} to be mapped with an element in the 3-dimensional Euclidean space.
     * @return  {@link Vector3} mapped from the input {@link UnitQuaternion}.
     */
    public abstract Vector3 toChartCenteredAtIdentity( UnitQuaternion q );
    
    
    /**
     * Defines the map from {@link Vector3}s to {@link UnitQuaternion}s for the chart centered at the identity {@link UnitQuaternion}.
     * <p>
     * The zero {@link Vector3} is mapped with the identity {@link UnitQuaternion}.
     * 
     * @param e     {@link Vector3} to be mapped with a {@link UnitQuaternion}.
     * @return  {@link UnitQuaternion} mapped with the input {@link Vector3}.
     */
    public abstract UnitQuaternion toManifoldFromChartCenteredAtIdentity( Vector3 e );
    
    
    /**
     * Returns true if the {@link UnitQuaternion} is contained in the domain of the chart centered at the identity {@link UnitQuaternion}.
     * 
     * @param q     {@link UnitQuaternion} that we want to know if belongs to the chart domain.
     * @return  true if the {@link UnitQuaternion} is contained in the domain of the chart centered at the identity {@link UnitQuaternion}; false otherwise.
     */
    public abstract boolean isContainedInChartCenteredAtIdentityDomain( UnitQuaternion q );
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Note that the chart image is the same for every chart of a {@link UnitQuaternionAtlas} (regardless of where it is centered).
     */
    public abstract boolean isContainedInChartImage( Vector3 e );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link UnitQuaternion} used to select the chart.
     * This {@link UnitQuaternion} will be mapped by the chart to the origin of the Euclidean space.
     */
    private UnitQuaternion chartSelector;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link UnitQuaternionAtlas}.
     */
    public UnitQuaternionAtlas()
    {
        // Default chartSelector.
        this.setChartSelector( UnitQuaternion.one() );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Such {@link UnitQuaternion} will be mapped by the chart to the origin of the Euclidean space.
     * 
     * @see #chartSelector
     */
    public void setChartSelector( UnitQuaternion theChartSelector )
    {
        this.chartSelector = theChartSelector;
    }
    
    
    /**
     * Transforms a {@link UnitQuaternion} to the perspective of the {@link #chartSelector}.
     * <p>
     * This method is used to generalize the definition of the chart using {@link #toChartCenteredAtIdentity(UnitQuaternion)} and {@link #toManifoldFromChartCenteredAtIdentity(Vector3)}.
     * The generalization is achieved expressing the {@link UnitQuaternion} as a rotation defined by the {@link #chartSelector}:
     * q = q0 * delta_q0_q
     * where  q0  is the {@link #chartSelector}, and  delta_q0_q  is the input {@link UnitQuaternion} expressed in the perspective of the {@link #chartSelector}.
     * This leads to solve such {@link UnitQuaternion} as
     * delta_q0_q = q0^{-1} * q
     * where  q  is the input {@link UnitQuaternion}.
     * 
     * @param q     {@link UnitQuaternion} to be transformed into the perspective of the {@link #chartSelector}.
     * @return  input {@link UnitQuaternion} expressed from the perspective of the {@link #chartSelector}.
     */
    public UnitQuaternion toChartSelectorPerspective( UnitQuaternion q )
    {
        // delta = q0^{-1} * q
        return this.chartSelector.inverseMultiplicative().multiply( q );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Vector3 toChart( UnitQuaternion q )
    {
        UnitQuaternion delta = this.toChartSelectorPerspective( q );
        return this.toChartCenteredAtIdentity( delta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternion toManifold( Vector3 e )
    {
        // q = q0 * delta_q0_q
        UnitQuaternion delta = this.toManifoldFromChartCenteredAtIdentity( e );
        return this.chartSelector.multiply( delta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isContainedInChartDomain( UnitQuaternion q )
    {
        UnitQuaternion delta = this.toChartSelectorPerspective( q );
        return this.isContainedInChartCenteredAtIdentityDomain( delta );
    }
    
}
