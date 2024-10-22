package numericalLibrary.manifolds.unitQuaternions.atlases;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.Matrix;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements test methods for {@link UnitQuaternionDifferentiableAtlas}.
 */
public abstract class UnitQuaternionDifferentiableAtlasTester
    extends UnitQuaternionAtlasTester
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public abstract UnitQuaternionDifferentiableAtlas getAtlas();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link UnitQuaternionDifferentiableAtlas#jacobianOfTransitionMap(UnitQuaternion)} returns the same for both q and -q.
     * <p>
     * This test makes sense because both q and -q represent the same rotation transformation.
     */
    @Test
    public void jacobianOfTransitionMapMapsQAndMinusQToSameTransitionMapMatrix()
    {
        UnitQuaternionDifferentiableAtlas atlas = this.getAtlas();
        List<UnitQuaternion> manifoldElementList = this.getManifoldElementList();
        for( UnitQuaternion q : manifoldElementList ) {
            // Take a random element in the domain of the chart.
            // Also build the same rotation with the opposite quaternion from the first one.
            UnitQuaternion qOpposite = q.opposite();
            // Obtain the transition map matrix T.
            Matrix T = atlas.jacobianOfTransitionMap( q );
            Matrix Tminus = atlas.jacobianOfTransitionMap( qOpposite );
            // Check that both q and -q are mapped to the same transition map matrix.
            double distance = T.distanceFrom( Tminus );
            assertTrue( distance < 1.0e-100 );
        }
    }
    
    
    /**
     * Tests that {@link UnitQuaternionDifferentiableAtlas#jacobianOfTransitionMap(UnitQuaternion)} transforms elements in the neighborhood of the element that defines the new chart in a consistent way.
     */
    @Test
    public void transitionMapMatrixIsConsistentWithTransitionMap()
    {
        UnitQuaternionDifferentiableAtlas atlas = this.getAtlas();
        List<UnitQuaternion> manifoldElementList = this.getManifoldElementList();
        List<Vector3> chartElementList = this.getChartElementList();
        Random rng = new Random( 42 );
        for( int i=0; i<manifoldElementList.size(); i++ ) {
            // Set the quaternion that defines the current chart.
            UnitQuaternion qInitial = manifoldElementList.get( i );
            atlas.setChartSelector( qInitial );
            // Select the center of the new chart (centered at qFinal) in the current chart (centered at qInitial).
            Vector3 e_qInitial_qFinal = chartElementList.get( i ).scaleInplace( 0.9 );  // We move a little from the border.
            // Select a deviated point from the center of the final chart in the initial chart.
            double alpha = 1.0e-3;
            Vector3 deq = Vector3.random( rng ).scaleInplace( alpha );
            Vector3 e_qInitial_qDeviated = e_qInitial_qFinal.add( deq );
            // Check that it is still in the image of the chart.
            if( !atlas.isContainedInChartImage( e_qInitial_qDeviated ) ) {
                continue;
            }
            // Transform the deviated point to the manifold.
            UnitQuaternion qDeviated = atlas.toManifold( e_qInitial_qDeviated );
            // Transform the point to the final chart.
            UnitQuaternion qFinal = atlas.toManifold( e_qInitial_qFinal );
            atlas.setChartSelector( qFinal );
            Vector3 e_qFinal_qDeviated = atlas.toChart( qDeviated );
            // Check that it is in the image of the new chart.
            if( !atlas.isContainedInChartImage( e_qFinal_qDeviated ) ) {
                continue;
            }
            // Use the transition map matrix to approximate the point in the new chart.
            Vector3 Tdeq = atlas.jacobianOfTransitionMap( qInitial , qFinal ).applyToVector3( deq );
            // Check that the result is close to the real value.
            double distance = e_qFinal_qDeviated.distanceFrom( Tdeq );
            // Since charts are first-order approximations,
            // the final error should be of the order of the squared norm of the deviation
            // and for sure smaller than the norm of the deviation (not squared).
            assertTrue( distance < deq.norm() );
        }
    }

}
