package numericalLibrary.functions;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;



public class AssociatedLegendrePolynomialEvaluatorTest
{
	/**
     * Tests {@link AssociatedLegendrePolynomialEvaluator} by comparing evaluations with analytical forms.
     */
    @Test
    void evaluationMatchesFirst4AnalyticalPolynomials()
    {
    	AssociatedLegendrePolynomialEvaluator associatedLegendrePolynomialEvaluator = new AssociatedLegendrePolynomialEvaluator( 4 );
    	Random randomNumberGenerator = new Random(42);
		double[][] plmAnalytical = new double[5][];
		for( int l=0; l<5; l++ ) {
			plmAnalytical[l] = new double[l+1];
		}
    	for( int i=0; i<100; i++ ) {
    		// Get a random evaluation point.
    		double x = 2.0 * randomNumberGenerator.nextDouble() - 1.0;
    		// Evaluate the analytical form of Associated Legendre Polynomials.
    		double x2 = x * x;
    		double oneMinus_x2 = 1.0 - x2;
    		double sqrt1Minus_x2 = Math.sqrt( oneMinus_x2 );
    		plmAnalytical[0][0] = 1.0;
    		plmAnalytical[1][0] = x;
    		plmAnalytical[1][1] = -sqrt1Minus_x2;
    		plmAnalytical[2][0] = ( 3.0 * x2 - 1.0 ) /  2.0;
    		plmAnalytical[2][1] = - 3.0 * x * sqrt1Minus_x2;
    		plmAnalytical[2][2] = 3.0 * oneMinus_x2;
    		double x3 = x2 * x;
    		plmAnalytical[3][0] = ( 5.0 * x3 - 3.0 * x ) / 2.0;
    		plmAnalytical[3][1] = 3.0/2.0 * ( 1.0 - 5.0 * x2 ) * sqrt1Minus_x2;
    		plmAnalytical[3][2] = 15.0 * x * oneMinus_x2;
    		plmAnalytical[3][3] = - 15.0 * sqrt1Minus_x2 * sqrt1Minus_x2 * sqrt1Minus_x2;
    		double x4 = x3 * x;
    		plmAnalytical[4][0] = ( 35.0 * x4 - 30.0 * x2 + 3.0 ) / 8.0;
    		plmAnalytical[4][1] = -5.0/2.0 * ( 7.0 * x3 - 3.0 * x ) * sqrt1Minus_x2;
    		plmAnalytical[4][2] = 15.0/2.0 * ( 7.0 * x2 - 1.0 ) * oneMinus_x2;
    		plmAnalytical[4][3] = - 105.0 * x * sqrt1Minus_x2 * sqrt1Minus_x2 * sqrt1Minus_x2;
    		plmAnalytical[4][4] = 105.0 * oneMinus_x2 * oneMinus_x2;
    		// Evaluate the implemented version.
    		associatedLegendrePolynomialEvaluator.evaluate( x );
    		// Compare with the implemented evaluation.
    		for( int l=0; l<5; l++ ) {
    			for( int m=0; m<=l; m++ ) {
    				//System.out.println( l + " " + m );
        			assertEquals( plmAnalytical[l][m] , associatedLegendrePolynomialEvaluator.getPolynomialValue( l , m ) , 1.0e-13 );
    				
    			}
    		}
    	}
    }
    
}
