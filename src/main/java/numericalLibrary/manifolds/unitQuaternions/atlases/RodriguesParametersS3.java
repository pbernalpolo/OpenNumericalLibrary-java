package numericalLibrary.manifolds.unitQuaternions.atlases;


import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.Quaternion;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements the Rodrigues Parameters projection that maps {@link UnitQuaternion}s in the S3 sphere to {@link Vector3}s in the Euclidean space.
 * <p>
 * The Rodrigues Parameters projection is a particular case of a stereographic projection in which the center of projection is at infinity in the center of the sphere.
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
