package numericalLibrary.functions;



public class LegendrePolynomialEvaluator
{
	private final double[] an;
	private final double[] bn;
	
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
	
	
	public void evaluate( double x )
	{
		this.pn[1] = x;
		// ( n + 1 ) P_{n+1}(x) = ( 2 n + 1 ) x P_n(x) - n P_{n-1}(x)
		for(  int n=1, nPlus1=2;  nPlus1<this.pn.length;  n++, nPlus1++  ) {
			this.pn[nPlus1] = this.an[n] * x * this.pn[n] - this.bn[n] * this.pn[n-1];
		}
	}
	
	
	public double getPnValue( int n )
	{
		return this.pn[ n ];
	}
	
}
