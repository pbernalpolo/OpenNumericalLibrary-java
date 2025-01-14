package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Exponential Map projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The exponential map of the {@link UnitQuaternion} manifold relates each {@link UnitQuaternion} that represents a 3d rotation transformation to the rotation vector that represents such rotation.
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Exponential_map_(Riemannian_geometry)</a>
 */
public class ExponentialMapS3
    extends UnitQuaternionDifferentiableAtlas
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Maximum allowed norm for chart elements to be transformed by {@link #toManifoldFromChartCenteredAtIdentity(Vector3)}.
     * The {@link #MAX_NORM} value comes from the image of the chart being the ball of radius pi.
     * Note that each point of the border of the image represents a different 3d rotation,
     * so there is no need to leave a margin.
     * 
     * @see #toManifoldFromChartCenteredAtIdentity(Vector3)
     */
    private static final double MAX_NORM = Math.PI;
    
    
    
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
        return q.toRotationVector();
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The image of the chart is the ball of radius pi.
     * Any input outside of the chart image will be saturated.
     * Saturating to {@link #MAX_NORM} makes the map defined by the chart bijective:
     * each {@link Vector3} within the chart image is mapped exactly with one 3d rotation transformation, or one {@link UnitQuaternion} in the upper hemisphere of the S3 sphere.
     * 
     * @see #MAX_NORM
     */
    public UnitQuaternion toManifoldFromChartCenteredAtIdentity( Vector3 e )
    {
        double enorm = e.norm();
        if( !this.isContainedInImageFromNorm( enorm ) ) {
            // Clip the norm to be in the image of the chart.
            e = e.scale( ExponentialMapS3.MAX_NORM / enorm );
        }
        return UnitQuaternion.fromRotationVector( e );
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
     * The image of the chart is the ball of radius pi.
     */
    public boolean isContainedInChartImage( Vector3 e )
    {
        return this.isContainedInImageFromNorm( e.norm() );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal jacobianOfChart( UnitQuaternion q )
    {
        Vector3 qv = q.vectorPart();
        double qvnormSquared = qv.normSquared();
        double qvnorm = Math.sqrt( qvnormSquared );
        double asinc_qvnorm = ( qvnorm > 0.0 )? Math.asin( qvnorm )/qvnorm : 1.0;
        MatrixReal submatrix = MatrixReal.one( 3 ).scaleInplace( asinc_qvnorm );
        submatrix.addInplace( qv.outerProduct( qv ).scaleInplace( ( 1.0/Math.abs( q.w() ) - asinc_qvnorm ) / qvnormSquared ) );
        submatrix.scaleInplace( 2.0 * Math.signum( q.w() ) );
        return MatrixReal.zero( 3 , 4 ).setSubmatrix( 0,1 , submatrix );
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal jacobianOfChartInverse( Vector3 e )
    {
        MatrixReal output = MatrixReal.empty( 4 , 3 );
        double enormSquared = e.normSquared();
        double enorm = Math.sqrt( enormSquared );
        double enorm_half = 0.5 * enorm;
        double sin_enorm_half = Math.sin( enorm_half );
        double cos_enorm_half = Math.cos( enorm_half );
        double sinc_enorm_half = ( enorm_half > 0.0 )? sin_enorm_half/enorm_half : 1.0;
        Vector3 eNormalized = ( enorm > 0.0 )? e.scale( 1.0/enorm ) : Vector3.zero();
        output.setSubmatrix( 0,0 , eNormalized.scale( -sin_enorm_half ).toMatrixAsRow() );
        MatrixReal submatrix = MatrixReal.one( 3 ).scaleInplace( sinc_enorm_half );
        submatrix.addInplace( eNormalized.outerProduct( eNormalized ).scaleInplace( cos_enorm_half - sinc_enorm_half ) );
        output.setSubmatrix( 1,0 , submatrix );
        output.scaleInplace( 0.5 );
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public MatrixReal jacobianOfTransitionMap( UnitQuaternion delta )
    {
        delta = delta.positiveScalarPartForm();
        Vector3 deltav = delta.vectorPart();
        MatrixReal ddT = deltav.outerProduct( deltav );
        double deltavnorm = deltav.norm();
        if( deltavnorm > 0.0 ) {
            return MatrixReal.one( 3 ).subtractInplace( ddT ).scaleInplace( delta.w() )
                    .subtractInplace( deltav.crossProductMatrix() ).scaleInplace( deltavnorm / Math.asin( deltavnorm ) )
                    .addInplace( ddT );
        } else {
            return MatrixReal.one( 3 );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns true if the {@link Vector3} is contained in the chart image.
     * <p>
     * The image is the ball of radius pi, so only the norm is necessary to check if it is in the chart image.
     * 
     * @param enorm     norm of the {@link Vector3} to be checked.
     * @return  true if the {@link Vector3} is contained in the chart image; false otherwise.
     */
    private boolean isContainedInImageFromNorm( double enorm )
    {
        return( enorm < ExponentialMapS3.MAX_NORM );
    }
    
}
