package numericalLibrary.functions;



/**
 * Computes Legendre polynomials using a recurrence relation.
 * <p>
 * In particular, it uses:
 * <br>
 * P_{n+1}(x) = a_n x P_n(x) - b_n P_{n-1}(x)
 * <br>
 * with:
 * <ul>
 * <li> a_n = ( 2 n + 1 ) / ( n + 1 )
 * <li> b_n = n / ( n + 1 )
 * </ul>
 * starting from a fixed constant  P_0^0 = 1 .
 */
public class LegendrePolynomialEvaluator
{
	////////////////////////////////////////////////////////////////
	/// PRIVATE VARIABLES
	////////////////////////////////////////////////////////////////
	
	/**
	 * Coefficients  a_n  that define the recurrence relation  P_{n+1}(x) = a_n x P_n(x) - b_n P_{n-1}(x) .
	 */
	private final double[] an;
	
	/**
	 * Coefficients  b_n  that define the recurrence relation  P_{n+1}(x) = a_n x P_n(x) - b_n P_{n-1}(x) .
	 */
	private final double[] bn;
	
	/**
	 * Result of evaluation of P_n(x).
	 */
	private final double[] pn;
	
	
	
	////////////////////////////////////////////////////////////////
	/// PUBLIC CONSTRUCTORS
	////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a {@link LegendrePolynomialEvaluator}.
	 * 
	 * @param nMaximum	maximum degree  n  to be evaluated. The degree  n  will range in  n = 0 , 1 , ... , nMaximum
	 */
	public LegendrePolynomialEvaluator( int nMaximum )
	{
		if( nMaximum < 0 ) {
			throw new IllegalArgumentException( "Found negative polynomial degree." );
		}
		if( nMaximum < 2 ) {
			nMaximum = 2;
		}
		this.an = new double[ nMaximum + 1 ];
		this.bn = new double[ nMaximum + 1 ];
		this.pn = new double[ nMaximum + 1 ];
		this.pn[0] = 1.0;
		for( int n=1; n<this.pn.length; n++ ) {
			double nPlus1 = n + 1.0;
			this.an[n] = ( n + nPlus1 ) / nPlus1;
			this.bn[n] = n / nPlus1;
		}
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
	 * <br>
	 * P_{n+1}(x) = a_n x P_n(x) - b_n P_{n-1}(x)
	 * <br>
	 * with:
	 * <ul>
	 * <li> a_n = ( 2 n + 1 ) / ( n + 1 )
	 * <li> b_n = n / ( n + 1 )
	 * </ul>
	 * starting from a fixed constant  P_0^0 .
	 * 
	 * @param x		evaluation point. It must be in the interval [-1,1].
	 * @throws IllegalArgumentException if x is not in the interval [-1,1].
	 */
	public void evaluate( double x )
	{
		final double[] p = this.pn;
		final double[] a = this.an;
		final double[] b = this.bn;
		
		p[1] = x;
		// ( n + 1 ) P_{n+1}(x) = ( 2 n + 1 ) x P_n(x) - n P_{n-1}(x)
		for(  int n=1, nPlus1=2;  nPlus1<this.pn.length;  n++, nPlus1++  ) {
			p[nPlus1] = a[n] * x * p[n] - b[n] * p[n-1];
		}
	}
	
	
	/**
	 * Returns the value of the Legendre polynomial P_n(x).
	 * <p>
	 * The evaluation point is set from the last {@link #evaluate(double)} call.
	 * 
	 * @param n		polynomial degree in the range l = 0 , 1 , ... , lMaximum
	 * @return	value of the Legendre polynomial P_n(x).
	 */
	public double getPnValue( int n )
	{
		return this.pn[ n ];
	}
	
}
