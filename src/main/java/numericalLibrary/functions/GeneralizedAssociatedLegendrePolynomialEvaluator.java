package numericalLibrary.functions;



/**
 * P_{l+1}^{m}(x) = alpha_{l-1}^m x P_l^m(x) - beta_{l-1}^m P_{l-1}^m(x)
 * P_{l+1}^{l}(x) = nu_l x P_l^l(x)
 * P_{l+1}^{l+1}(x) = - mu_l sqrt( 1 - x^2 ) P_l^l(x)
 * 
 */
abstract class GeneralizedAssociatedLegendrePolynomialEvaluator
{
	
	protected final double[] mu;
	protected final double[] nu;
	protected final double[][] alpha;
	protected final double[][] beta;
	
	/**
	 * Result of evaluation of P_l^m(x).
	 */
	protected final double[][] p;
	
	
	public GeneralizedAssociatedLegendrePolynomialEvaluator( int lMaximum )
	{
		if( lMaximum < 0 ) {
			throw new IllegalArgumentException( "Found negative polynomial degree." );
		}
		this.mu = new double[ lMaximum ];
		this.nu = new double[ lMaximum ];
		this.alpha = new double[ lMaximum - 1 ][];
		this.beta = new double[ lMaximum - 1 ][];
		for( int lp=0; lp<this.alpha.length; lp++ ) {
			this.alpha[lp] = new double[ lp+1 ];
		}
		for( int lp=0; lp<this.beta.length; lp++ ) {
			this.beta[lp] = new double[ lp+1 ];
		}
		this.p = new double[ lMaximum + 1 ][];
		for( int l=0; l<this.p.length; l++ ) {
			this.p[l] = new double[ l+1 ];
		}
	}
	
	
	public void evaluate( double x )
	{
		if(  x < -1.0  ||  1.0 < x  ) {
			throw new IllegalArgumentException( "x must be in [-1, 1]" );
		}
		
		// Cache field references locally.
		final double[][] alpha = this.alpha;
	    final double[][] beta = this.beta;
	    final double[][] p = this.p;
	    final double[] mu = this.mu;
	    final double[] nu = this.nu;
	    
	    final double sqrt1Minus_x2 = Math.sqrt( 1.0 - x * x );
	    
	    // Compute p_1^m.
	    double[] p_1 = p[1];
	    p_1[1] = - mu[0] * sqrt1Minus_x2 * p[0][0];
	    p_1[0] = nu[0] * x * p[0][0];
	    
	    // Compute p_l^m for l>1.
	    for( int l=1, lPlus1=2, lMinus1=0; l<this.mu.length;  l++, lPlus1++, lMinus1++ ) {
	    	// Cache current l coefficients.
	        final double[] alpha_lMinus1 = alpha[lMinus1];
	        final double[] beta_lMinus1 = beta[lMinus1];
	        
	        // Cache current l references to avoid repeated jagged-array lookups.
	        final double[] p_lMinus1 = p[lMinus1];
	        final double[] p_l = p[l];
	        final double[] p_lPlus1 = p[lPlus1];
	        
	        // Compute polynomial P_{l+1}^m using previous l polynomials for m=0,...,l-1 .
	        // P_{l+1}^{m}(x) = alpha_{l-1}^m x P_l^m(x) - beta_{l-1}^m P_{l-1}^m(x)
	        for( int m = 0;  m < l;  m++ ) {
	        	p_lPlus1[m] = alpha_lMinus1[m] * x * p_l[m] - beta_lMinus1[m] * p_lMinus1[m];
	        }
	        
	        // Compute P_{l+1}^{l+1} and P_{l+1}^{l}.
	        // P_{l+1}^{l}(x) = nu_l x P_l^l(x)
	        p_lPlus1[l] = nu[l] * x * p_l[l];
	        // P_{l+1}^{l+1}(x) = - mu_l sqrt( 1 - x^2 ) P_l^l(x)
	        p_lPlus1[lPlus1] = - mu[l] * sqrt1Minus_x2 * p_l[l];
	    }
	}
	
	
	public double getPolynomialValue( int l , int m )
	{
		return this.p[ l ][ m ];
	}
	
}
