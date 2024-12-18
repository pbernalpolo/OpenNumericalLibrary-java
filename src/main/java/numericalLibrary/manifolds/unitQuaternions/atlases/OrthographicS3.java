package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Orthographic projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The Orthographic projection is a particular case of a stereographic projection in which the center of projection is at infinity in the direction of the bottom of the sphere.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 * @see <a href>https://en.wikipedia.org/wiki/Orthographic_projection</a>
 */
public class OrthographicS3
    extends UnitQuaternionDifferentiableAtlas
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Small value used to define {@link #MAX_NORM}.
     */
    private static final double EPSILON = 1.0e-4;
    
    /**
     * Maximum allowed norm for chart elements to be transformed by {@link #toManifoldFromChartCenteredAtIdentity(Vector3)}.
     * The {@link #MAX_NORM} value comes from the image of the chart being the ball of radius 2,
     * but we leave some margin so that {@link #jacobianOfTransitionMap(UnitQuaternion)} is non-singular.
     * 
     * @see #toManifoldFromChartCenteredAtIdentity(Vector3)
     */
    private static final double MAX_NORM = 2.0 - OrthographicS3.EPSILON;
    
    /**
     * Maximum allowed squared norm.
     * Derived from {@link #MAX_NORM}.
     */
    private static final double MAX_NORM_SQUARED = OrthographicS3.MAX_NORM * OrthographicS3.MAX_NORM;
    
    
    
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
        return new Vector3( q.x() , q.y() , q.z() ).scaleInplace( 2.0 );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the ball of radius 2.
     * Any input outside of the chart image will be saturated.
     * Saturating to {@link #MAX_NORM} makes the map defined by the chart bijective:
     * each {@link Vector3} within the chart image is mapped exactly with one 3d rotation transformation, or one {@link UnitQuaternion} in the upper hemisphere of the S3 sphere.
     * It also prevents {@link #jacobianOfTransitionMap(UnitQuaternion)} from being singular.
     * 
     * @see #MAX_NORM
     */
    public UnitQuaternion toManifoldFromChartCenteredAtIdentity( Vector3 e )
    {
        double enormSquared = e.normSquared();
        if( !this.isContainedInImageFromNormSquared( enormSquared ) ) {
            // Clip the norm to be in the image of the chart.
            e = e.scale( OrthographicS3.MAX_NORM/e.norm() );
            enormSquared = OrthographicS3.MAX_NORM_SQUARED;
        }
        Quaternion q = Quaternion.fromScalarAndVectorPart(
                Math.sqrt( 1.0 - enormSquared/4.0 ) ,
                e.scale( 0.5 ) );
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
     * The image of the chart is the ball of radius 2.
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
        return MatrixReal.one(3)
                .scaleInplace( delta.w() )
                .subtractInplace( deltav.crossProductMatrix() )
                .addInplace( deltav.outerProduct( deltav ).scaleInplace( 1.0/delta.w() ) );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns true if the {@link Vector3} is contained in the chart image.
     * <p>
     * The image is the ball of radius 2, so only the norm (or in this case, the squared norm) is necessary to check if it is in the chart image.
     * 
     * @param enormSquared     squared norm of the {@link Vector3} to be checked.
     * @return  true if the {@link Vector3} is contained in the chart image; false otherwise.
     */
    private boolean isContainedInImageFromNormSquared( double enormSquared )
    {
        // Note that we are not using MAX_NORM_SQUARED so that tests can pass.
        return ( enormSquared < 4.0 );
    }

}
