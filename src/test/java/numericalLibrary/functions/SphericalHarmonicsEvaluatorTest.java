package numericalLibrary.functions;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.types.ComplexNumber;



/**
 * Implements test methods for {@link SphericalHarmonicsEvaluator}.
 */
public class SphericalHarmonicsEvaluatorTest
{
	////////////////////////////////////////////////////////////////
	/// TEST METHODS
	////////////////////////////////////////////////////////////////
	
	/**
     * Tests {@link SphericalHarmonicsEvaluator} by comparing evaluations with analytical forms.
     */
    @Test
    void evaluationMatchesFirstAnalyticalSphericalHarmonics()
    {
    	SphericalHarmonicsEvaluator sphericalHarmonicsEvaluator = new SphericalHarmonicsEvaluator( 4 );
    	Random randomNumberGenerator = new Random(42);
    	ComplexNumber[][] sphericalHarmonicsAnalytical = new ComplexNumber[5][];
		for( int l=0; l<5; l++ ) {
			sphericalHarmonicsAnalytical[l] = new ComplexNumber[l+1];
		}
    	for( int i=0; i<100; i++ ) {
    		// Get a random evaluation point.
    		double theta = Math.PI * randomNumberGenerator.nextDouble();
    		double phi = 2.0 * Math.PI * randomNumberGenerator.nextDouble();
    		// Evaluate the analytical form of Associated Legendre Polynomials.
    		double one_over_sqrt_pi = 1.0 / Math.sqrt( Math.PI );
    		double cosTheta = Math.cos( theta );
    		double sinTheta = Math.sin( theta );
    		sphericalHarmonicsAnalytical[0][0] = ComplexNumber.one().scaleInplace( one_over_sqrt_pi / 2.0 );
    		sphericalHarmonicsAnalytical[1][0] = ComplexNumber.one().scaleInplace( Math.sqrt(3.0)/2.0 * one_over_sqrt_pi * cosTheta );
    		sphericalHarmonicsAnalytical[1][1] = ComplexNumber.i().scaleInplace( phi ).exp().scaleInplace( - Math.sqrt( 3.0 / 2.0 ) / 2.0 * one_over_sqrt_pi * sinTheta );
    		double cos2theta = cosTheta * cosTheta;
    		double sin2theta = sinTheta * sinTheta;
    		sphericalHarmonicsAnalytical[2][0] = ComplexNumber.one().scaleInplace( Math.sqrt(5.0)/4.0 * one_over_sqrt_pi * ( 3.0 * cos2theta - 1.0 ) );
    		sphericalHarmonicsAnalytical[2][1] = ComplexNumber.i().scaleInplace( phi ).exp().scaleInplace( - Math.sqrt( 15.0 / 2.0 ) / 2.0 * one_over_sqrt_pi * sinTheta * cosTheta );
    		sphericalHarmonicsAnalytical[2][2] = ComplexNumber.i().scaleInplace( 2.0 * phi ).exp().scaleInplace( Math.sqrt( 15.0 / 2.0 ) / 4.0 * one_over_sqrt_pi * sin2theta );
    		double cos3theta = cos2theta * cosTheta;
    		double sin3theta = sin2theta * sinTheta;
    		sphericalHarmonicsAnalytical[3][0] = ComplexNumber.one().scaleInplace( Math.sqrt( 7.0 ) / 4.0 * one_over_sqrt_pi * ( 5.0 * cos3theta - 3.0 * cosTheta ) );
    		sphericalHarmonicsAnalytical[3][1] = ComplexNumber.i().scaleInplace( phi ).exp().scaleInplace( - Math.sqrt( 21.0 ) / 8.0 * one_over_sqrt_pi * sinTheta * ( 5.0 * cos2theta - 1.0 ) );
    		sphericalHarmonicsAnalytical[3][2] = ComplexNumber.i().scaleInplace( 2.0 * phi ).exp().scaleInplace( Math.sqrt( 105.0 / 2.0 ) / 4.0 * one_over_sqrt_pi * sin2theta * cosTheta );
    		sphericalHarmonicsAnalytical[3][3] = ComplexNumber.i().scaleInplace( 3.0 * phi ).exp().scaleInplace( - Math.sqrt( 35.0 ) / 8.0 * one_over_sqrt_pi * sin3theta );
    		double cos4theta = cos3theta * cosTheta;
    		double sin4theta = sin3theta * sinTheta;
    		sphericalHarmonicsAnalytical[4][0] = ComplexNumber.one().scaleInplace( 3.0 / 16.0 * one_over_sqrt_pi * ( 35.0 * cos4theta - 30.0 * cos2theta + 3.0 ) );
    		sphericalHarmonicsAnalytical[4][1] = ComplexNumber.i().scaleInplace( phi ).exp().scaleInplace( - 3.0 / 8.0 * Math.sqrt( 5.0 ) * one_over_sqrt_pi * sinTheta * ( 7.0 * cos3theta - 3.0 * cosTheta ) );
    		sphericalHarmonicsAnalytical[4][2] = ComplexNumber.i().scaleInplace( 2.0 * phi ).exp().scaleInplace( 3.0 / 8.0 * Math.sqrt( 5.0 / 2.0 ) * one_over_sqrt_pi * sin2theta * ( 7.0 * cos2theta - 1.0 ) );
    		sphericalHarmonicsAnalytical[4][3] = ComplexNumber.i().scaleInplace( 3.0 * phi ).exp().scaleInplace( - 3.0 / 8.0 * Math.sqrt( 35.0 ) * one_over_sqrt_pi * sin3theta * cosTheta );
    		sphericalHarmonicsAnalytical[4][4] = ComplexNumber.i().scaleInplace( 4.0 * phi ).exp().scaleInplace( 3.0 / 16.0 * Math.sqrt( 35.0 / 2.0 ) * one_over_sqrt_pi * sin4theta );
    		// Evaluate the implemented version.
    		sphericalHarmonicsEvaluator.evaluate( theta , phi );
    		// Compare with the implemented evaluation.
    		for( int l=0; l<5; l++ ) {
    			for( int m=0; m<=l; m++ ) {
    				ComplexNumber analytical = sphericalHarmonicsAnalytical[l][m];
        			assertEquals( analytical.re() , sphericalHarmonicsEvaluator.getSphericalHarmonicsRealPart( l , m ) , 1.0e-14 );
        			assertEquals( analytical.im() , sphericalHarmonicsEvaluator.getSphericalHarmonicsImaginaryPart( l , m ) , 1.0e-14 );
    			}
    		}
    	}
    }
    
}
