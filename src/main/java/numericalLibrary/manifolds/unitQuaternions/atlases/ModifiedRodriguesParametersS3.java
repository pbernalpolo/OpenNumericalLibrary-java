package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Modified Rodrigues Parameters projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The Modified Rodrigues Parameters is a particular case of a stereographic projection in which the center of projection is at the bottom of the sphere.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 */
public class ModifiedRodriguesParametersS3
    extends UnitQuaternionDifferentiableAtlas
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Maximum allowed norm for chart elements to be transformed by {@link #toManifoldFromChartCenteredAtIdentity(Vector3)}.
     * The {@link #MAX_NORM} value comes from the image of the chart being the ball of radius 4.
     * Note that each point of the border of the image represents a different 3d rotation,
     * so there is no need to leave a margin.
     * 
     * @see #toManifoldFromChartCenteredAtIdentity(Vector3)
     */
    private static final double MAX_NORM = 4.0;
    
    /**
     * Maximum allowed squared norm.
     * Derived from {@link #MAX_NORM}.
     */
    private static final double MAX_NORM_SQUARED = ModifiedRodriguesParametersS3.MAX_NORM * ModifiedRodriguesParametersS3.MAX_NORM;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link UnitQuaternion} can be mapped to an element in the chart.
     * This is possible because both q and -q represent the same rotation transformation.
     */
    public Vector3 toChartCenteredAtIdentity( UnitQuaternion q )
    {
        q = q.positiveScalarPartForm();
        return new Vector3( q.x() , q.y() , q.z() ).scaleInplace( 4.0/( 1.0 + q.w() ) );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the ball of radius 4.
     * Any input outside of the chart image will be saturated.
     * Saturating to {@link #MAX_NORM} makes the map defined by the chart bijective:
     * each {@link Vector3} within the chart image is mapped exactly with one 3d rotation transformation, or one {@link UnitQuaternion} in the upper hemisphere of the S3 sphere.
     * 
     * @see #MAX_NORM
     */
    public UnitQuaternion toManifoldFromChartCenteredAtIdentity( Vector3 e )
    {
        double enormSquared = e.normSquared();
        if( !this.isContainedInImageFromNormSquared( enormSquared ) ) {
            // Clip the norm to be in the image of the chart.
            e = e.scale( ModifiedRodriguesParametersS3.MAX_NORM/e.norm() );
            enormSquared = ModifiedRodriguesParametersS3.MAX_NORM_SQUARED;
        }
        double alpha = 1.0/( 16.0 + enormSquared );
        double alphav = alpha * 8.0;
        Quaternion q = Quaternion.fromScalarAndVectorPart(
                ( 16.0 - enormSquared ) * alpha ,
                e.scale( alphav ) );
        return UnitQuaternion.fromNormalizedQuaternion( q );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * Any {@link UnitQuaternion} can be mapped to an element in the chart.
     * This is possible because both q and -q represent the same rotation transformation.
     */
    public boolean isContainedInChartCenteredAtIdentityDomain( UnitQuaternion q )
    {
        return true;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the ball of radius 4.
     */
    public boolean isContainedInChartImage( Vector3 e )
    {
        return this.isContainedInImageFromNormSquared( e.normSquared() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal jacobianOfTransitionMap( UnitQuaternion delta )
    {
        delta = delta.positiveScalarPartForm();
        Vector3 deltav = delta.vectorPart();
        return ( ( MatrixReal.one(3)
                         .scaleInplace( delta.w() )
                         .subtractInplace( deltav.crossProductMatrix() ) )
                 .scaleInplace( 1.0 + delta.w() )
                 .addInplace( deltav.outerProduct( deltav ) ) )
               .scaleInplace( 0.5 );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns true if the {@link Vector3} is contained in the chart image.
     * <p>
     * The image is the ball of radius 4, so only the norm (or in this case, the squared norm) is necessary to check if it is in the chart image.
     * 
     * @param enormSquared     squared norm of the {@link Vector3} to be checked.
     * @return  true if the {@link Vector3} is contained in the chart image; false otherwise.
     */
    private boolean isContainedInImageFromNormSquared( double enormSquared )
    {
        return ( enormSquared < ModifiedRodriguesParametersS3.MAX_NORM_SQUARED );
    }

}
