package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Modified Rodrigues Parameters projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The Modified Rodrigues Parameters is a particular case of a stereographic projection in which the center of projection is at the bottom of the sphere.
 * <p>
 * The chart is defined as:
 * <br>
 * phi( q ) = 4 q_v / ( 1 + q_0 )
 * <p>
 * The inverse chart is given by:
 * <br>
 * phi^{-1}( e ) = 1 / ( 16 + ||e||^2 ) (  16 - ||e||^2     )
 *                                      (  8 e              )
 * <p>
 * The domain of the chart is:
 * <br>
 * { q in S^3 : q_0 >= 0 }
 * <p>
 * The image of the chart is:
 * <br>
 * { e in R^3 : ||e|| <= 4 }
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
     * The chart is defined as:
     * <br>
     * phi( q )  =  4 q_v / ( 1 + q_0 )
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
     * The inverse chart is given by:
     * <br>
     * phi^{-1}( e )  =  1 / ( 16 + ||e||^2 ) (  16 - ||e||^2     )
     *                                        (  8 e              )
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
     * <p>
     * The Jacobian of the chart is given by:
     * <br>
     * d phi / d q  =  4 sign( q_0 ) / ( 1 + q_0 ) (  -q_v / ( 1 + q_0 )  |  I  )
     */
    public MatrixReal jacobianOfChart( UnitQuaternion q )
    {
        double q0signum = Math.signum( q.w() );
        q = q.positiveScalarPartForm();
        double q0Plus1 = 1.0 + q.w();
        MatrixReal output = MatrixReal.empty( 3 , 4 );
        output.setSubmatrix( 0,0 , q.vectorPart().scaleInplace( -4.0/( q0Plus1 * q0Plus1 ) * q0signum ).toMatrixAsColumn() );
        output.setSubmatrix( 0,1 , MatrixReal.one( 3 ).scaleInplace( 4.0/q0Plus1 * q0signum ) );
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of the inverse chart is given by:
     * <br>
     * d phi^{-1} / d e  =  8 / ( 16 + ||e||^2 )^2  ( - 8 e                          )
     *                                              ( I ( 16 + ||e||^2 ) - 2 e e^T   )
     */
    public MatrixReal jacobianOfChartInverse( Vector3 e )
    {
        MatrixReal output = MatrixReal.empty( 4 , 3 );
        output.setSubmatrix( 0,0 , e.scale( -8.0 ).toMatrixAsRow() );
        double alpha = 16.0 + e.normSquared();
        output.setSubmatrix( 1,0 , MatrixReal.one( 3 ).scaleInplace( alpha ).subtractInplace( e.outerProduct( e ).scaleInplace( 2.0 ) ) );
        output.scaleInplace( 8.0/( alpha * alpha ) );
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of the transition map is given by:
     * <br>
     * T( delta )  =  1/2 [ ( 1 + delta_0 ) ( delta_0 I - [ delta_v ]_x ) + delta_v delta_v^T ]
     * with delta_0 >= 0.
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
