package numericalLibrary.functions;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;



/**
 * Implements test methods for {@link LegendrePolynomialEvaluator}.
 */
public class LegendrePolynomialEvaluatorTest
{
	////////////////////////////////////////////////////////////////
	/// TEST METHODS
	////////////////////////////////////////////////////////////////
	
	/**
     * Tests {@link LegendrePolynomialEvaluator} by comparing evaluations with analytical forms.
     */
    @Test
    void evaluationMatchesFirst10AnalyticalPolynomials()
    {
    	LegendrePolynomialEvaluator legendrePolynomialEvaluator = new LegendrePolynomialEvaluator( 10 );
    	Random randomNumberGenerator = new Random(42);
		double[] pnAnalytical = new double[11];
    	for( int i=0; i<100; i++ ) {
    		// Get a random evaluation point.
    		double x = 2.0 * randomNumberGenerator.nextDouble() - 1.0;
    		// Evaluate the analytical form of Legendre Polynomials.
    		pnAnalytical[0] = 1.0;
    		pnAnalytical[1] = x;
    		double x2 = x * x;
    		pnAnalytical[2] = ( 3.0 * x2 - 1.0 ) / 2.0;
    		double x3 = x2 * x;
    		pnAnalytical[3] = ( 5.0 * x3 - 3.0 * x ) / 2.0;
    		double x4 = x3 * x;
    		pnAnalytical[4] = ( 35.0 * x4 - 30.0 * x2 + 3.0 ) / 8.0;
    		double x5 = x4 * x;
    		pnAnalytical[5] = ( 63.0 * x5 - 70.0 * x3 + 15.0 * x ) / 8.0;
    		double x6 = x5 * x;
    		pnAnalytical[6] = ( 231.0 * x6 - 315.0 * x4 + 105.0 * x2 - 5.0 ) / 16.0;
    		double x7 = x6 * x;
    		pnAnalytical[7] = ( 429.0 * x7 - 693.0 * x5 + 315.0 * x3 - 35.0 * x ) / 16.0;
    		double x8 = x7 * x;
    		pnAnalytical[8] = ( 6435.0 * x8 - 12012.0 * x6 + 6930.0 * x4 - 1260.0 * x2 + 35.0 ) / 128.0;
    		double x9 = x8 * x;
    		pnAnalytical[9] = ( 12155.0 * x9 - 25740.0 * x7 + 18018.0 * x5 - 4620 * x3 + 315.0 * x ) / 128.0;
    		double x10 = x9 * x;
    		pnAnalytical[10] = ( 46189.0 * x10 - 109395.0 * x8 + 90090.0 * x6 - 30030.0 * x4 + 3465 * x2 - 63.0 ) / 256.0;
    		// Evaluate the implemented version.
    		legendrePolynomialEvaluator.evaluate( x );
    		// Compare with the implemented evaluation.
    		for( int n=0; n<=10; n++ ) {
    			assertEquals( pnAnalytical[n] , legendrePolynomialEvaluator.getPnValue( n ) , 1.0e-13 );
    		}
    	}
    }
    
}
