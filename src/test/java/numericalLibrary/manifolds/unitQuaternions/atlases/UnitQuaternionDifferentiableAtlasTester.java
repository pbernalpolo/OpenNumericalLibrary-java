package numericalLibrary.manifolds.unitQuaternions.atlases;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.MatrixReal;
import numericalLibrary.types.UnitQuaternion;
import numericalLibrary.types.Vector3;



/**
 * Implements test methods for {@link UnitQuaternionDifferentiableAtlas}.
 */
abstract class UnitQuaternionDifferentiableAtlasTester
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
            MatrixReal T = atlas.jacobianOfTransitionMap( q );
            MatrixReal Tminus = atlas.jacobianOfTransitionMap( qOpposite );
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
    
    
    /**
     * Tests that {@link UnitQuaternionDifferentiableAtlas#jacobianOfTransitionMap(UnitQuaternion)} is consistent with {@link UnitQuaternionDifferentiableAtlas#jacobianOfChart(UnitQuaternion)} and {@link UnitQuaternionDifferentiableAtlas#jacobianOfChartInverse(Vector3)}.
     * <p>
     * We compare the result of {@link UnitQuaternionDifferentiableAtlas#jacobianOfTransitionMap(UnitQuaternion)} to the result of applying the chain rule:
     * e_p_r = phi( delta_p_q^* * phi^{-1}( e_q_r ) )
     * d (e_p_r)_i / d (e_q_r)_alpha |_emean = \sum_{j k} d phi_i / d q_j ( delta_p_q^* * phi^{-1}( emean ) ) * [ delta_p_q^* ]*_{j k} * d phi^{-1}_k / d e ( emean ) =
     *  = \sum_{j k} d phi_i / d q_j ( 1 ) * [ delta_p_q^* ]*_{j k} * d phi^{-1}_k / d e ( emean )
     */
    @Test
    public void transitionMapMatrixIsConsistentWithJacobiansOfChart()
    {
        // Get the atlas and the chart elements that define the delta quaternion to be introduced in jacobianOfTransitionMap.
        UnitQuaternionDifferentiableAtlas atlas = this.getAtlas();
        List<Vector3> chartElementList = this.getChartElementList();
        // For each chart element,
        for( Vector3 e_qInitial_qFinal : chartElementList ) {
            // Get the delta quaternion that results from the chart update vector.
            UnitQuaternion delta_qInitial_qFinal = atlas.toManifold( e_qInitial_qFinal );
            // Compute the Jacobian of the transition map using the jacobianOfTransitionMap method.
            MatrixReal transitionMapMatrix = atlas.jacobianOfTransitionMap( delta_qInitial_qFinal );
            // Compute the Jacobian of the transition map using the jacobianOfChart and jacobianOfChartInverse methods, and the chain rule.
            MatrixReal jacobianChart = atlas.jacobianOfChart( UnitQuaternion.one() );
            MatrixReal jacobianChartInverse = atlas.jacobianOfChartInverse( e_qInitial_qFinal );
            MatrixReal delta_qInitial_qFinal_conjugate_productMatrix = MatrixReal.empty( 4 , 4 );
            delta_qInitial_qFinal_conjugate_productMatrix.setEntry( 0,0 ,
                    delta_qInitial_qFinal.w() );
            delta_qInitial_qFinal_conjugate_productMatrix.setSubmatrix( 0,1 ,
                    delta_qInitial_qFinal.vectorPart().toMatrixAsRow() );
            delta_qInitial_qFinal_conjugate_productMatrix.setSubmatrix( 1,0 ,
                    delta_qInitial_qFinal.vectorPart().inverseAdditiveInplace().toMatrixAsColumn() );
            delta_qInitial_qFinal_conjugate_productMatrix.setSubmatrix( 1,1 ,
                    MatrixReal.one( 3 ).scaleInplace( delta_qInitial_qFinal.w() )
                    .subtractInplace( delta_qInitial_qFinal.vectorPart().crossProductMatrix() ) );
            MatrixReal transitionMapMatrixFromJacobians = jacobianChart.multiply( delta_qInitial_qFinal_conjugate_productMatrix.multiply( jacobianChartInverse ) );
            // Check that both ways of computing the Jacobian of the transition map yield the same result.
            //transitionMapMatrix.subtract( transitionMapMatrixFromJacobians ).print();
            assertTrue( transitionMapMatrix.equalsApproximately( transitionMapMatrixFromJacobians , 1.0e-14 ) );
        }
    }
    
    
    /**
     * Tests that {@link UnitQuaternionDifferentiableAtlas#jacobianOfChart(UnitQuaternion)} returns the opposite for q than for -q.
     * <p>
     * This steams from the chart property for unit quaternions:
     * phi( q )  =  phi( -q )
     * which leads to
     * d phi / d q ( q )  =  - d phi / d q ( -q )
     */
    @Test
    public void jacobianOfChartAtQIsMinusJacobianOfChartAtMinusQ()
    {
        UnitQuaternionDifferentiableAtlas atlas = this.getAtlas();
        List<UnitQuaternion> manifoldElementList = this.getManifoldElementList();
        for( UnitQuaternion manifoldElement : manifoldElementList ) {
            // Get Jacobian of chart evaluated at q.
            MatrixReal jacobianChartPlus = atlas.jacobianOfChart( manifoldElement );
            // Get Jacobian of chart evaluated at -q.
            MatrixReal jacobianChartMinus = atlas.jacobianOfChart( manifoldElement.opposite() );
            // Check that first is the opposite of second.
            //jacobianChartPlus.print();
            //jacobianChartMinus.print();
            assertTrue( jacobianChartMinus.equalsApproximately( jacobianChartPlus.inverseAdditive() , 1.0e-17 ) );
        }
    }
    
}
