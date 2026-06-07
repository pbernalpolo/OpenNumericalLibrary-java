package numericalLibrary.functions;



public class AssociatedLegendrePolynomialEvaluator
{
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
		this.plm = new double[ lMaximum + 1 ][];
		for( int l=0; l<=lMaximum; l++ ) {
			// For each l, m ranges from -l to l.
			this.plm[l] = new double[ l+1 ];
		}
		this.plm[0][0] = 1.0;
	}
	
	
	public void evaluate( double x )
	{
		double sqrt1Minus_x2 = Math.sqrt( 1.0 - x * x );
		this.plm[1][0] = x;
		this.plm[1][1] = - sqrt1Minus_x2;
		// ( l - m + 1 ) P_{l+1}^m(x) = ( 2 l + 1 ) x P_l^m(x) - (l+m) P_{l-1}^m(x)
		for(  int l=1, lPlus1=2;  lPlus1<this.plm.length;  l++, lPlus1++  ) {
			for( int m=0; m<l; m++ ) {
				this.plm[lPlus1][m] = ( ( l + l + 1 ) * x * this.plm[l][m] - ( l + m ) * this.plm[l-1][m] ) / ( l - m + 1 );
			}
			// P_{l+1}^l(x) = ( 2 l + 1 ) x P_l^l(x)
			// P_{l+1}^{l+1}(x) = - ( 2 l + 1 ) sqrt( 1 - x^2 ) P_l^l(x)
			double alphaPll = ( l + l + 1 ) * this.plm[l][l];
			this.plm[lPlus1][l] = x * alphaPll;
			this.plm[lPlus1][lPlus1] = - sqrt1Minus_x2 * alphaPll;
		}
	}
	
	
	public double getPlmValue( int l , int m )
	{
		return this.plm[ l ][ m ];
	}
	
}
