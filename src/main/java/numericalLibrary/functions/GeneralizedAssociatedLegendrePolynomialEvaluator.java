package numericalLibrary.functions;



/**
 * Computes associated Legendre polynomials through generalized recurrence relations.
 * <p>
 * The generalized recurrence relations are:
 * <ul>
 * <li> P_l^l(x) = - mu_{l-1} sqrt( 1 - x^2 ) P_{l-1}^{l-1}(x)
 * <li> P_l^{l-1}(x) = nu_{l-1} x P_{l-1}^{l-1}(x)
 * <li> P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x)
 * </ul>
 * starting from a fixed constant  P_0^0 .
 * <p>
 * These relations have been found in a great text written by Justin Willmert:
 * https://justinwillmert.com/articles/2020/pre-normalizing-legendre-polynomials/
 * 
 * In particular, it computes 1-x^2 so that there is no catastrophic cancelation.
 * It also exploits the fact that given the recurrence relation
 * P_l^l(x) = - mu_{l-1} sqrt( 1 - x^2 ) P_{l-1}^{l-1}(x)
 * we have
 * P_l^l(x) = mu_{l-1} mu_{l-2} ( 1 - x^2 ) P_{l-2}^{l-2}(x)
 * so we can compute terms without introducing sqrt computation errors for even l terms.
 */
abstract class GeneralizedAssociatedLegendrePolynomialEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PROTECTED VARIABLES
	////////////////////////////////////////////////////////////////
	
	/**
	 * If  l  is odd, then {@link #mu} contains at  l-1  the value of  mu_{l-1}  defining the recurrence relation  P_l^l(x) = - mu_{l-1} sqrt( 1 - x^2 ) P_{l-1}^{l-1}(x) .
	 * If  l  is even, then {@link #mu} contains at  l-1  the result of the product  mu_{l-1} mu_{l-2}  defining the recurrence relation  P_l^l(x) = mu_{l-1} mu_{l-2} ( 1 - x^2 ) P_{l-2}^{l-2}(x) .
	 */
	protected final double[] mu;
	
	/**
	 * Coefficients  nu_l  that define the recurrence relation  P_l^{l-1}(x) = nu_{l-1} x P_{l-1}^{l-1}(x) .
	 */
	protected final double[] nu;
	
	/**
	 * Coefficients  alpha_l^m  that together with {@link #beta} define the recurrence relation  P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x) .
	 * First index is the polynomial degree l.
	 * Second index is the polynomial order m.
	 */
	protected final double[][] alpha;
	
	/**
	 * Coefficients  beta_l^m  that together with {@link #alpha} define the recurrence relation  P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x) .
	 * First index is the polynomial degree l.
	 * Second index is the polynomial order m.
	 */
	protected final double[][] beta;
	
	/**
	 * Result of evaluation of  P_l^m(x) .
	 * First index is the polynomial degree l.
	 * Second index is the polynomial order m.
	 */
	protected final double[][] p;

	/**
	 * Coefficients  gamma_l^m  that define the derivative recurrence  ( x^2 - 1 ) dP_l^m/dx = l x P_l^m - gamma_{l-1}^m P_{l-1}^m .
	 * The coefficient in front of  x P_l^m  is always  l  regardless of the normalization (it keeps the normalization of  P_l^m );
	 * only  gamma , the coefficient in front of  P_{l-1}^m , is normalization-dependent and is filled by the concrete subclass.
	 * Stored shifted by one degree (as {@link #mu} and {@link #nu} are): {@code gamma[l-1][m]} is used by the degree-l recurrence,
	 * so the first index ranges in  l-1 = 0 , 1 , ... , lMaximum-1  and the second index is the order m.
	 */
	protected final double[][] gamma;

	/**
	 * Result of evaluation of  ( x^2 - 1 ) dP_l^m/dx(x) , set by {@link #evaluateDerivatives()}.
	 * First index is the polynomial degree l.
	 * Second index is the polynomial order m.
	 */
	protected final double[][] dp;

	/**
	 * Evaluation point set by the last {@link #evaluate(double)} call, needed by {@link #evaluateDerivatives()}.
	 */
	protected double x;



	////////////////////////////////////////////////////////////////
	/// PROTECTED CONSTRUCTORS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link GeneralizedAssociatedLegendrePolynomialEvaluator}.
	 * 
	 * @param lMaximum	maximum degree  l  to be evaluated. The degree  l  will range in  l = 0 , 1 , ... , lMaximum
	 */
	protected GeneralizedAssociatedLegendrePolynomialEvaluator( int lMaximum )
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
		this.gamma = new double[ lMaximum ][];
		for( int lMinus1=0; lMinus1<this.gamma.length; lMinus1++ ) {
			this.gamma[lMinus1] = new double[ lMinus1+1 ];
		}
		this.dp = new double[ lMaximum + 1 ][];
		for( int l=0; l<this.dp.length; l++ ) {
			this.dp[l] = new double[ l+1 ];
		}
		// P_0^0 is constant, so ( x^2 - 1 ) dP_0^0/dx is zero for every x; set it once here and never recompute it.
		this.dp[0][0] = 0.0;
	}
	
	
	
	////////////////////////////////////////////////////////////////
	/// PUBLIC METHODS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the Legendre polynomials at x.
	 * <p>
	 * The input x must be in the interval [-1,1].
	 * Otherwise, an {@link IllegalArgumentException} is thrown.
	 * <p>
	 * The Legendre polynomials are evaluated using the recurrence relations:
	 * <ul>
	 * <li> P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x)
	 * <li> P_l^{l-1}(x) = nu_{l-1} x P_{l-1}^{l-1}(x) .
	 * <li> P_l^l(x) = - mu_{l-1} sqrt( 1 - x^2 ) P_{l-1}^{l-1}(x)
	 * </ul>
	 * starting from a fixed constant  P_0^0 .
	 * 
	 * @param x		evaluation point. It must be in the interval [-1,1].
	 * @throws IllegalArgumentException if x is not in the interval [-1,1].
	 */
	public void evaluate( double x )
	{
		if(  x < -1.0  ||  1.0 < x  ) {
			throw new IllegalArgumentException( "x must be in [-1, 1]" );
		}
		this.x = x;

		// Cache field references locally.
		final double[][] alpha = this.alpha;
	    final double[][] beta = this.beta;
	    final double[][] p = this.p;
	    final double[] mu = this.mu;
	    final double[] nu = this.nu;
	    
		// Compute  1 - x^2  =  (1+x) * (1-x) .
	    final double one_minus_x2 = ( 1.0 + x ) * ( 1.0 - x );
	    
	    // Apply  P_l^l(x) = mu_{l-1} mu_{l-2} ( 1 - x^2 ) P_{l-2}^{l-2}(x) .
	    for( int l=2, lMinus2=0; l<p.length; l+=2, lMinus2+=2 ) {
	    	p[l][l] = mu[l-1] * one_minus_x2 * p[lMinus2][lMinus2];
	    }
	    
	    // Compute  sqrt( 1 - x^2 ) .
	    final double sqrt_one_minus_x2 = Math.sqrt( one_minus_x2 );
	    
    	// Apply  P_l^l(x) = - mu_{l-1} sqrt( 1 - x^2 ) P_{l-1}^{l-1}(x) .
	    for( int l=1, lMinus1=0; l<p.length; l+=2, lMinus1+=2 ) {
	    	p[l][l] = - mu[lMinus1] * sqrt_one_minus_x2 * p[lMinus1][lMinus1];
	    }
	    
	    // Apply  P_l^{l-1}(x) = nu_{l-1} x P_{l-1}^{l-1}(x) .
	    for( int l=1, lMinus1=0; l<p.length; l++, lMinus1++ ) {
	    	 p[l][lMinus1] = nu[lMinus1] * x * p[lMinus1][lMinus1];
	    }
	    
	    // Apply  P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x) .
	    for( int l=2, lMinus1=1, lMinus2=0; l<this.p.length;  l++, lMinus1++, lMinus2++ ) {
	    	// Cache current l coefficients.
	        final double[] alpha_lMinus2 = alpha[lMinus2];
	        final double[] beta_lMinus2 = beta[lMinus2];
	        
	        // Cache current l references to avoid repeated jagged-array lookups.
	        final double[] p_lMinus2 = p[lMinus2];
	        final double[] p_lMinus1 = p[lMinus1];
	        final double[] p_l = p[l];
	        
	        // Compute polynomial P_{l+1}^m using previous l polynomials for m=0,...,l-1 .
	        // P_l^m(x) = alpha_{l-2}^m x P_{l-1}^m(x) - beta_{l-2}^m P_{l-2}^m(x)
	        for( int m = 0;  m < l-1;  m++ ) {
	        	p_l[m] = alpha_lMinus2[m] * x * p_lMinus1[m] - beta_lMinus2[m] * p_lMinus2[m];
	        }
	    }
	}
	
	
	/**
	 * Returns the value of the Legendre polynomial P_l^m(x).
	 * <p>
	 * The evaluation point is set from the last {@link #evaluate(double)} call.
	 * 
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	value of the Legendre polynomial P_l^m(x).
	 */
	public double getPolynomialValue( int l , int m )
	{
		return this.p[ l ][ m ];
	}


	/**
	 * Evaluates  ( x^2 - 1 ) dP_l^m/dx  at the point set by the last {@link #evaluate(double)} call.
	 * <p>
	 * It applies the derivative recurrence  ( x^2 - 1 ) dP_l^m/dx = l x P_l^m - gamma_{l-1}^m P_{l-1}^m ,
	 * reusing the polynomial values computed by {@link #evaluate(double)}. {@link #evaluate(double)} must be called first.
	 * <p>
	 * The degree-0 term is left untouched: P_0^0 is constant, so  dp[0][0]  keeps the zero set at construction.
	 */
	public void evaluateDerivatives()
	{
		final double[][] p = this.p;
		final double[][] dp = this.dp;
		final double[][] gamma = this.gamma;
		final double x = this.x;

		// Apply  ( x^2 - 1 ) dP_l^m/dx = l x P_l^m - gamma_{l-1}^m P_{l-1}^m .
		for( int l=1, lMinus1=0; l<dp.length; l++, lMinus1++ ) {
			final double[] p_l = p[l];
			final double[] p_lMinus1 = p[lMinus1];
			final double[] dp_l = dp[l];
			final double[] gamma_lMinus1 = gamma[lMinus1];
			final double lx = l * x;
			// Tesseral terms ( m < l ) use the previous degree.
			for( int m=0; m<l; m++ ) {
				dp_l[m] = lx * p_l[m] - gamma_lMinus1[m] * p_lMinus1[m];
			}
			// Sectoral term ( m = l ): P_{l-1}^l = 0 .
			dp_l[l] = lx * p_l[l];
		}
	}


	/**
	 * Returns the value of  ( x^2 - 1 ) dP_l^m/dx .
	 * <p>
	 * The evaluation point is set from the last {@link #evaluate(double)} call, and the derivative must have been
	 * computed by a subsequent {@link #evaluateDerivatives()} call.
	 * <p>
	 * Note that  ( x^2 - 1 ) dP_l^m/dx(cos theta) = sin( theta ) dP_l^m/dtheta(cos theta) , which is finite everywhere
	 * (in particular at the poles, where the bare  dP_l^m/dtheta  would be a  0/0  limit).
	 *
	 * @param l		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @param m		polynomial order in the range m = 0 , 1 , ... , l
	 * @return	value of  ( x^2 - 1 ) dP_l^m/dx .
	 */
	public double getPolynomialDerivativeTimesX2Minus1( int l , int m )
	{
		return this.dp[ l ][ m ];
	}

}
