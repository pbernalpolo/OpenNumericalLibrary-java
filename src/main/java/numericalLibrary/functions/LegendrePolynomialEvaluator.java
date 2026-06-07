package numericalLibrary.functions;



public class LegendrePolynomialEvaluator
{
	/**
	 * Result of evaluation of P_n(x).
	 */
	private final double[] pn;
	
	
	public LegendrePolynomialEvaluator( int nMaximum )
	{
		if( nMaximum < 0 ) {
			throw new IllegalArgumentException( "Found negative polynomial degree." );
		}
		if( nMaximum < 2 ) {
			nMaximum = 2;
		}
		this.pn = new double[ nMaximum + 1 ];
		this.pn[0] = 1.0;
	}
	
	
	public void evaluate( double x )
	{
		this.pn[1] = x;
		// ( n + 1 ) P_{n+1}(x) = ( 2 n + 1 ) x P_n(x) - n P_{n-1}(x)
		for(  int n=1, nPlus1=2;  nPlus1<this.pn.length;  n++, nPlus1++  ) {
			this.pn[nPlus1] = ( ( n + nPlus1 ) * x * this.pn[n] - n * this.pn[n-1] ) / nPlus1;
		}
		
	}
	
	
	public double getPnValue( int n )
	{
		return this.pn[ n ];
	}
	
}
