package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Rodrigues Parameters projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The Rodrigues Parameters projection is a particular case of a stereographic projection in which the center of projection is at infinity in the center of the sphere.
 * <p>
 * The chart is defined as:
 * <br>
 * phi( q )  =  2 q_v / q_0
 * <p>
 * The inverse chart is given by:
 * <br>
 * phi^{-1}( e )  =  1 / sqrt( 4 + ||e||^2 ) (  2  )
 *                                           (  e  )
 * <p>
 * The domain of the chart is:
 * <br>
 * { q in S^3 : q_0 > 0 }
 * <p>
 * The image of the chart is:
 * <br>
 * R^3
 * 
 * @see "Kalman Filtering for Attitude Estimation with Quaternions and Concepts from Manifold Theory" (<a href="https://www.mdpi.com/1424-8220/19/1/149">https://www.mdpi.com/1424-8220/19/1/149</a>)
 * @see <a href>https://en.wikipedia.org/wiki/Stereographic_projection</a>
 */
public class RodriguesParametersS3
    extends UnitQuaternionDifferentiableAtlas
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     * <p>
     * The chart is defined as:
     * <br>
     * phi( q ) = 2 q_v / q_0
     * <p>
     * Any {@link UnitQuaternion} can be mapped to an element in the chart.
     * This is possible because both q and -q represent the same rotation transformation.
     */
    public Vector3 toChartCenteredAtIdentity( UnitQuaternion q )
    {
        return new Vector3( q.x() , q.y() , q.z() ).scaleInplace( 2.0/q.w() );
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The inverse chart is given by:
     * <br>
     * phi^{-1}( e )  =  1 / sqrt( 4 + ||e||^2 ) (  2  )
     *                                           (  e  )
     * <p>
     * The image is R^3, so there is no need to saturate the input {@link Vector3}.
     */
    public UnitQuaternion toManifoldFromChartCenteredAtIdentity( Vector3 e )
    {
        double alpha = 1.0/Math.sqrt( 4.0 + e.normSquared() );
        Quaternion q = Quaternion.fromScalarAndVectorPart(
                alpha + alpha ,
                e.scale( alpha ) );
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
     * The image of the chart is all R^3.
     */
    public boolean isContainedInChartImage( Vector3 e )
    {
        return true;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of the chart is given by:
     * <br>
     * d phi / d q  =  2 / q_0  (  - q_v / q_0  |  I  )
     */
    public MatrixReal jacobianOfChart( UnitQuaternion q )
    {
        MatrixReal output = MatrixReal.empty( 3 , 4 );
        output.setSubmatrix( 0,0 , q.vectorPart().scaleInplace( -2.0/( q.w() * q.w() ) ).toMatrixAsColumn() );
        output.setSubmatrix( 0,1 , MatrixReal.one( 3 ).scaleInplace( 2.0/q.w() ) );
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of the inverse chart is given by:
     * <br>
     * d phi^{-1} / d e =  1 / ( 4 + ||e||^2 )^(3/2) ( - 2 e                        )
     *                                               (  I ( 4 + ||e||^2 ) - e e^T   )
     */
    public MatrixReal jacobianOfChartInverse( Vector3 e )
    {
        MatrixReal output = MatrixReal.empty( 4 , 3 );
        output.setSubmatrix( 0,0 , e.scale( -2.0 ).toMatrixAsRow() );
        double alpha = 4.0 + e.normSquared();
        output.setSubmatrix( 1,0 , MatrixReal.one( 3 ).scaleInplace( alpha ).subtractInplace( e.outerProduct( e ) ) );
        double divisorValue = Math.sqrt( alpha );
        output.scaleInplace( 1.0/( divisorValue * divisorValue * divisorValue ) );
        return output;
    }
    
    
    /**
     * {@inheritDoc}
     * <p>
     * The Jacobian of the transition map is given by:
     * <br>
     * T( delta )  =  delta_0 ( delta_0 I - [ delta_v ]_x )
     * with delta_0 != 0.
     */
    public MatrixReal jacobianOfTransitionMap( UnitQuaternion delta )
    {
        Vector3 deltav = delta.vectorPart();
        return MatrixReal.one(3)
                         .scaleInplace( delta.w() )
                         .subtractInplace( deltav.crossProductMatrix() )
                         .scaleInplace( delta.w() );
    }
    
}
