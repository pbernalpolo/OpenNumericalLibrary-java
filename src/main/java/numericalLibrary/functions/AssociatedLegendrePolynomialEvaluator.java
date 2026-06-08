package numericalLibrary.functions;



public class AssociatedLegendrePolynomialEvaluator
{
	
	private final double[][] alm;
	private final double[][] blm;
	
	/**
	 * Result of evaluation of P_l^m(x).
	 */
	private final double[][] plm;
	
	
	public AssociatedLegendrePolynomialEvaluator( int lMaximum )
	{
		if( lMaximum < 0 ) {
			throw new IllegalArgumentException( "Found negative polynomial degree." );
		}
		if( lMaximum < 2 ) {
			lMaximum = 2;
		}
		this.alm = new double[ lMaximum + 1 ][];
		this.blm = new double[ lMaximum + 1 ][];
		this.plm = new double[ lMaximum + 1 ][];
		for( int l=0; l<=lMaximum; l++ ) {
			// For each l, m ranges from -l to l.
			this.plm[l] = new double[ l+1 ];
			this.alm[l] = new double[ l+1 ];
			this.blm[l] = new double[ l+1 ];
			double lPlus1 = l + 1.0;
			double lPluslPlus1 = l + lPlus1;
			for( int m=0; m<=l; m++ ) {
				double lMinus_mPlus1 = lPlus1 - m;
				this.alm[l][m] = lPluslPlus1 / lMinus_mPlus1;
				this.blm[l][m] = ( l + m ) / lMinus_mPlus1;
			}
		}
		this.plm[0][0] = 1.0;
	}
	
	
	public void evaluate(double x)
	{
		// Cache field references locally.
	    final double[][] plm = this.plm;
	    final double[][] alm = this.alm;
	    final double[][] blm = this.blm;
	    
	    final double sqrt1Minus_x2 = Math.sqrt( 1.0 - x * x );
	    
	    double[] p_1 = plm[1];
	    p_1[0] = x;
	    p_1[1] = - sqrt1Minus_x2;
	    
	    for (int l = 1, lPlus1 = 2, n = plm.length; lPlus1 < n; l++, lPlus1++) {
	    	// Cache current l references to avoid repeated jagged-array lookups.
	        final double[] p_lMinus1 = plm[l-1];
	        final double[] p_l = plm[l];
	        final double[] p_lPlus1 = plm[lPlus1];
	        // Cache current l coefficients as well.
	        final double[] a_l = alm[l];
	        final double[] b_l = blm[l];
	        // Compute polynomial P_{l+1}^m using previous l polynomials.
			// ( l - m + 1 ) P_{l+1}^m(x) = ( 2 l + 1 ) x P_l^m(x) - (l+m) P_{l-1}^m(x)
	        for( int m=0; m < l; m++ ) {
	        	p_lPlus1[m] = a_l[m] * x * p_l[m] - b_l[m] * p_lMinus1[m];
	        }
	        // P_{l+1}^l(x) = ( 2 l + 1 ) x P_l^l(x)
	     	// P_{l+1}^{l+1}(x) = - ( 2 l + 1 ) sqrt( 1 - x^2 ) P_l^l(x)
	        final double alphaPll = ( l + l + 1.0 ) * p_l[l];
	        p_lPlus1[l] = x * alphaPll;
	        p_lPlus1[lPlus1] = - sqrt1Minus_x2 * alphaPll;
	    }
	}
	
	
	public double getPlmValue( int l , int m )
	{
		return this.plm[ l ][ m ];
	}
	
}
