package numericalLibrary.manifolds.unitComplexNumbers.atlases;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.ComplexNumber;
import numericalLibrary.types.RealNumber;
import numericalLibrary.types.UnitQuaternion;



/**
 * Implements test methods for {@link UnitQuaternionDifferentiableAtlas}.
 */
public abstract class UnitComplexNumberDifferentiableAtlasTester
    extends UnitComplexNumberAtlasTester
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public abstract UnitComplexNumberDifferentiableAtlas getAtlas();
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Tests that {@link UnitQuaternionDifferentiableAtlas#jacobianOfTransitionMap(UnitQuaternion)} transforms elements in the neighborhood of the element that defines the new chart in a consistent way.
     */
    @Test
    public void transitionMapMatrixIsConsistentWithTransitionMap()
    {
        UnitComplexNumberDifferentiableAtlas atlas = this.getAtlas();
        List<ComplexNumber> manifoldElementList = this.getManifoldElementList();
        List<RealNumber> chartElementList = this.getChartElementList();
        Random rng = new Random( 42 );
        for( int i=0; i<manifoldElementList.size(); i++ ) {
            // Set the complex that defines the current chart.
            ComplexNumber zInitial = manifoldElementList.get( i );
            atlas.setChartSelector( zInitial );
            // Select the center of the new chart (centered at zFinal) in the current chart (centered at zInitial).
            RealNumber e_zInitial_zFinal = chartElementList.get( i ).scaleInplace( 0.9 );  // We move a little from the border.
            // Select a deviated point from the center of the final chart in the initial chart.
            double alpha = 1.0e-3;
            RealNumber dez = RealNumber.random( rng ).scaleInplace( alpha );
            RealNumber e_zInitial_zDeviated = e_zInitial_zFinal.add( dez );
            // Check that it is still in the image of the chart.
            if( !atlas.isContainedInChartImage( e_zInitial_zDeviated ) ) {
                continue;
            }
            // Transform the deviated point to the manifold.
            ComplexNumber zDeviated = atlas.toManifold( e_zInitial_zDeviated );
            // Transform the point to the final chart.
            ComplexNumber zFinal = atlas.toManifold( e_zInitial_zFinal );
            atlas.setChartSelector( zFinal );
            RealNumber e_zFinal_zDeviated = atlas.toChart( zDeviated );
            // Check that it is in the image of the new chart.
            if( !atlas.isContainedInChartImage( e_zFinal_zDeviated ) ) {
                continue;
            }
            // Use the transition map matrix to approximate the point in the new chart.
            RealNumber Tdez = dez.scale( atlas.jacobianOfTransitionMap( zInitial , zFinal ).entry( 0 , 0 ) );
            // Check that the result is close to the real value.
            double distance = e_zFinal_zDeviated.distanceFrom( Tdez );
            // Since charts are first-order approximations,
            // the final error should be of the order of the squared norm of the deviation
            // and for sure smaller than the norm of the deviation (not squared).
            assertTrue( distance < dez.norm() );
        }
    }

}
