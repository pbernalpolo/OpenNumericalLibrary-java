package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import numericalLibrary.manifolds.Atlas;
import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;



/**
 * Base class for each {@link Atlas} mapping {@link ComplexNumber}s in the S1 sphere to {@link RealNumber}s in the Euclidean space.
 * <p>
 * More concretely, {@link UnitComplexNumberAtlas} maps 3d rotation transformations to {@link RealNumber}s in the Euclidean space.
 * <p>
 * A chart is defined for each {@link ComplexNumber}.
 * Such chart transforms the {@link ComplexNumber} associated with the chart with the origin of the Euclidean space.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 */
public abstract class UnitComplexNumberAtlas
    implements Atlas<ComplexNumber, RealNumber>
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Defines the map from {@link ComplexNumber}s to {@link RealNumber}s for the chart centered at the identity {@link ComplexNumber}.
     * <p>
     * The identity {@link ComplexNumber} is mapped with the zero {@link RealNumber}.
     * 
     * @param z     {@link ComplexNumber} to be mapped with an element in the 1-dimensional Euclidean space.
     * @return  {@link RealNumber} mapped from the input {@link ComplexNumber}.
     */
    public abstract RealNumber toChartCenteredAtIdentity( ComplexNumber z );
    
    
    /**
     * Defines the map from {@link RealNumber}s to {@link ComplexNumber}s for the chart centered at the identity {@link ComplexNumber}.
     * <p>
     * The zero {@link RealNumber} is mapped with the identity {@link ComplexNumber}.
     * 
     * @param e     {@link RealNumber} to be mapped with a {@link ComplexNumber}.
     * @return  {@link ComplexNumber} mapped with the input {@link RealNumber}.
     */
    public abstract ComplexNumber toManifoldFromChartCenteredAtIdentity( RealNumber e );
    
    
    /**
     * Returns true if the {@link ComplexNumber} is contained in the domain of the chart centered at the identity {@link ComplexNumber}.
     * 
     * @param z     {@link ComplexNumber} that we want to know if belongs to the chart domain.
     * @return  true if the {@link ComplexNumber} is contained in the domain of the chart centered at the identity {@link ComplexNumber}; false otherwise.
     */
    public abstract boolean isContainedInChartCenteredAtIdentityDomain( ComplexNumber z );
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Note that the chart image is the same for every chart of a {@link UnitComplexNumberAtlas} (regardless of where it is centered).
     */
    public abstract boolean isContainedInChartImage( RealNumber e );
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@link ComplexNumber} used to select the chart.
     * This {@link ComplexNumber} will be mapped by the chart to the origin of the Euclidean space.
     */
    private ComplexNumber chartSelector;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a {@link UnitComplexNumberAtlas}.
     */
    public UnitComplexNumberAtlas()
    {
        // Default chartSelector.
        this.setChartSelector( ComplexNumber.one() );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Such {@link ComplexNumber} will be mapped by the chart to the origin of the Euclidean space.
     * 
     * @see #chartSelector
     */
    public void setChartSelector( ComplexNumber theChartSelector )
    {
        this.chartSelector = theChartSelector;
    }
    
    
    /**
     * Transforms a {@link ComplexNumber} to the perspective of the {@link #chartSelector}.
     * <p>
     * This method is used to generalize the definition of the chart using {@link #toChartCenteredAtIdentity(ComplexNumber)} and {@link #toManifoldFromChartCenteredAtIdentity(RealNumber)}.
     * The generalization is achieved expressing the {@link ComplexNumber} as a rotation defined by the {@link #chartSelector}:
     * z = z0 * delta_z0_z
     * where  z0  is the {@link #chartSelector}, and  delta_z0_z  is the input {@link ComplexNumber} expressed in the perspective of the {@link #chartSelector}.
     * This leads to solve such {@link ComplexNumber} as
     * delta_z0_z = z0^{-1} * z
     * where  z  is the input {@link ComplexNumber}.
     * 
     * @param z     {@link ComplexNumber} to be transformed into the perspective of the {@link #chartSelector}.
     * @return  input {@link ComplexNumber} expressed from the perspective of the {@link #chartSelector}.
     */
    public ComplexNumber toChartSelectorPerspective( ComplexNumber z )
    {
        // delta = z0^{-1} * z
        return this.chartSelector.inverseMultiplicative().multiply( z );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public RealNumber toChart( ComplexNumber z )
    {
        ComplexNumber delta = this.toChartSelectorPerspective( z );
        return this.toChartCenteredAtIdentity( delta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public ComplexNumber toManifold( RealNumber e )
    {
        // z = z0 * delta_z0_z
        ComplexNumber delta = this.toManifoldFromChartCenteredAtIdentity( e );
        return this.chartSelector.multiply( delta );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public boolean isContainedInChartDomain( ComplexNumber z )
    {
        ComplexNumber delta = this.toChartSelectorPerspective( z );
        return this.isContainedInChartCenteredAtIdentityDomain( delta );
    }
    
}
