package numericalLibrary.types;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import numericalLibrary.algebraicStructures.AdditiveAbelianGroupElementTester;
import numericalLibrary.algebraicStructures.MetricSpaceElementTester;
import numericalLibrary.algebraicStructures.VectorSpaceElementTester;



/**
 * Implements test methods for {@link MatrixReal}.
 */
class MatrixTest
    implements
        AdditiveAbelianGroupElementTester<MatrixReal>,
        VectorSpaceElementTester<MatrixReal>,
        MetricSpaceElementTester<MatrixReal>
{
    ////////////////////////////////////////////////////////////////
    // CONSTANTS
    ////////////////////////////////////////////////////////////////
    protected static final int N_SAMPLES_TO_TEST = 10000;

    ////////////////////////////////////////////////////////////////
    // PROTECTED VARIABLES
    ////////////////////////////////////////////////////////////////
    protected Random randomNumberGenerator;

    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public MatrixTest()
    {
        this.randomNumberGenerator = new Random( 42 );
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public List<MatrixReal> getElementList()
    {
        List<MatrixReal> output = new ArrayList<MatrixReal>();
        output.add( MatrixReal.zero( 4 , 4 ) );
        output.add( MatrixReal.diagonal( new double[] { 1.0 , 2.0 , 3.0 , 4.0 } ) );
        output.add( MatrixReal.one( 4 ) );
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            output.add( MatrixReal.random( 4 , 4 , randomNumberGenerator ) );
        }
        return output;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // TEST METHODS
    ////////////////////////////////////////////////////////////////

    @Test
    void setAndGet()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal m = MatrixReal.zero(2,2);
            double value = 123.0;
            m.setEntry( 0,0 , value );
            assertEquals( value , m.entry(0,0) );
        }
    }


    @Test
    void multiplyBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal a = createMatrixA();
            MatrixReal b = createMatrixB();
            MatrixReal ab = a.multiply( b );
            MatrixReal abExpected = MatrixReal.empty( 2 , 2 );
            abExpected.setEntry( 0,0 , -3.0 );    abExpected.setEntry( 0,1 , 2.0 );
            abExpected.setEntry( 1,0 , -5.0 );    abExpected.setEntry( 1,1 , 0.0 );
            assertTrue( ab.equals( abExpected ) );
        }
    }

    
    /**
     * Tests the behavior of {@link MatrixReal#choleskyDecomposition()}.
     */
    @Test
    void choleskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // Obtain the Cholesky decomposition.
            MatrixReal L = A.choleskyDecomposition();
            // Rebuild the matrix doing L * L^T
            MatrixReal LLT = L.multiply( L.transpose() );
            // We should obtain the same matrix we started with.
            assertTrue( LLT.equalsApproximately( A , 1.0e-14 ) );
        }
    }

    
    /**
     * Tests the behavior of {@link MatrixReal#choleskyDecompositionInplace()}.
     */
    @Test
    void choleskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // Obtain the Cholesky decomposition.
            MatrixReal L = A.copy().choleskyDecompositionInplace();
            // Rebuild the matrix doing L * L^T
            MatrixReal LLT = L.multiply( L.transpose() );
            // We should obtain the same matrix we started with.
            assertTrue( LLT.equalsApproximately( A , 1.0e-14 ) );
        }
    }


    @Test
    void LDLTBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // we obtain the LDLT decomposition
            MatrixReal LD = A.LDLTDecomposition();
            // we obtain L and D from LD
            MatrixReal L = MatrixReal.LfromLDLTDecomposition( LD );
            MatrixReal D = MatrixReal.DfromLDLTDecomposition( LD );
            // we rebuild the matrix doing L * D * L^T
            MatrixReal LDLT = L.multiply( D.multiply( L.transpose() ) );
            assertTrue( LDLT.equalsApproximately( A , 1.0e-13 ) );
        }
    }


    @Test
    void LDLTAssignBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // we obtain the LDLT decomposition
            MatrixReal LD = A.copy().LDLTDecompositionInplace();
            // we obtain L and D from LD
            MatrixReal L = MatrixReal.LfromLDLTDecomposition( LD );
            MatrixReal D = MatrixReal.DfromLDLTDecomposition( LD );
            // we rebuild the matrix doing L * D * L^T
            MatrixReal LDLT = L.multiply( D.multiply( L.transpose() ) );
            assertTrue( LDLT.equalsApproximately( A , 1.0e-13 ) );
        }
    }


    /**
     * Tests the behavior of {@link MatrixReal#divideRightByPositiveDefiniteUsingItsCholeskyDecomposition(MatrixReal)}.
     * <p>
     * Having a matrix  XA = X * A, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideRightByPositiveDefiniteUsingItsCholeskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 5 x 10.
            MatrixReal X = MatrixReal.random( 5 , 10 , this.randomNumberGenerator );
            // Multiply  X * A  to obtain the matrix we want to divide.
            MatrixReal XA = X.multiply( A );
            // Divide using the Cholesky decomposition: XA * A^{-1} = X
            MatrixReal Xrecomputed = XA.divideRightByPositiveDefiniteUsingItsCholeskyDecomposition( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which XA was generated.
            assertTrue( Xrecomputed.equalsApproximately( X , 1.0e-4 ) );
        }
    }


    /**
     * Tests the behavior of {@link MatrixReal#divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace(MatrixReal)}.
     * <p>
     * Having a matrix  XA = X * A, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 5 x 10.
            MatrixReal X = MatrixReal.random( 5 , 10 , this.randomNumberGenerator );
            // Multiply  X * A  to obtain the matrix we want to divide.
            MatrixReal XA = X.multiply( A );
            // Divide using the Cholesky decomposition: XA * A^{-1} = X
            XA.divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which XA was generated.
            assertTrue( XA.equalsApproximately( X , 1.0e-4 ) );
        }
    }
    
    
    /**
     * Tests the behavior of {@link MatrixReal#divideLeftByPositiveDefiniteUsingItsCholeskyDecomposition(MatrixReal)}.
     * <p>
     * Having a matrix  AX = A * X, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 10 x 5.
            MatrixReal X = MatrixReal.random( 10 , 5 , this.randomNumberGenerator );
            // Multiply  A * X  to obtain the matrix we want to divide.
            MatrixReal AX = A.multiply( X );
            // Divide using the Cholesky decomposition: A^{-1} * AX = X
            MatrixReal Xrecomputed = AX.divideLeftByPositiveDefiniteUsingItsCholeskyDecomposition( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which AX was generated.
            assertTrue( Xrecomputed.equalsApproximately( X , 1.0e-4 ) );
        }
    }


    /**
     * Tests the behavior of {@link MatrixReal#divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace(MatrixReal)}.
     * <p>
     * Having a matrix  AX = A * X, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            MatrixReal X = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 10 x 5.
            MatrixReal A = MatrixReal.random( 10 , 5 , this.randomNumberGenerator );
            // Multiply  A * X  to obtain the matrix we want to divide.
            MatrixReal XA = X.multiply( A );
            // Divide using the Cholesky decomposition: A^{-1} * AX = X
            XA.divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace( X.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which AX was generated.
            assertTrue( XA.equalsApproximately( A , 1.0e-4 ) );
        }
    }


    @Test
    void divideRightByPositiveDefiniteUsingItsLDLTDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // we generate a random matrix
            MatrixReal M = MatrixReal.random( 5 , 10 , this.randomNumberGenerator );
            // we obtain the product
            MatrixReal MA = M.multiply( A );
            // and we divide using the LDLT decomposition
            MatrixReal Mrecomputed = MA.divideRightByPositiveDefiniteUsingItsLDLTDecomposition( A.LDLTDecompositionInplace() );
            assertTrue( Mrecomputed.equalsApproximately( M , 1.0e-4 ) );
        }
    }

    
    @Test
    void divideRightByPositiveDefiniteUsingItsLDLTDecompositionAssignBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            MatrixReal A = this.randomPositiveDefiniteMatrix( 10 );
            // we generate a random matrix
            MatrixReal M = MatrixReal.random( 5 , 10 , this.randomNumberGenerator );
            // we obtain the product
            MatrixReal MA = M.multiply( A );
            // and we divide using the LDLT decomposition
            MA.divideRightByPositiveDefiniteUsingItsLDLTDecompositionInplace( A.LDLTDecompositionInplace() );
            assertTrue( MA.equalsApproximately( M , 1.0e-4 ) );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Generates a random positive definite {@link MatrixReal}.
     * 
     * @param dimension     dimension of the positive definite {@link MatrixReal} to be generated.
     * @return  random positive definite {@link MatrixReal}.
     */
    private MatrixReal randomPositiveDefiniteMatrix( int dimension )
    {
        // Generate a random matrix.
        MatrixReal M = MatrixReal.random( dimension , dimension , this.randomNumberGenerator );
        // Obtain a random positive definite matrix through M * M^T
        return M.multiply( M.transpose() );
    }



    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC METHODS
    ////////////////////////////////////////////////////////////////

    private static MatrixReal createMatrixA()
    {
        MatrixReal m = MatrixReal.empty( 2 , 2 );
        m.setEntry( 0,0 , 1.0 );    m.setEntry( 0,1 , 2.0 );
        m.setEntry( 1,0 , 3.0 );    m.setEntry( 1,1 , 4.0 );
        return m;
    }


    private static MatrixReal createMatrixB()
    {
        MatrixReal m = MatrixReal.empty( 2 , 2 );
        m.setEntry( 0,0 , 1.0 );     m.setEntry( 0,1 , -4.0 );
        m.setEntry( 1,0 , -2.0 );    m.setEntry( 1,1 , 3.0 );
        return m;
    }


    private static MatrixReal createMatrixC()
    {
        MatrixReal m = MatrixReal.empty( 3 , 2 );
        m.setEntry( 0,0 , 1.0 );    m.setEntry( 0,1 , 2.0 );
        m.setEntry( 1,0 , 3.0 );    m.setEntry( 1,1 , 4.0 );
        m.setEntry( 2,0 , 5.0 );    m.setEntry( 2,1 , 6.0 );
        return m;
    }


    private static MatrixReal createMatrixD()
    {
        MatrixReal m = MatrixReal.empty( 3 , 3 );
        m.setEntry( 0,0 , 0.0 );    m.setEntry( 0,1 , 1.0 );     m.setEntry( 0,2 , 1.0 );
        m.setEntry( 1,0 , 2.0 );    m.setEntry( 1,1 , 3.0 );     m.setEntry( 1,2 , 5.0 );
        m.setEntry( 2,0 , 8.0 );    m.setEntry( 2,1 , 13.0 );    m.setEntry( 2,2 , 21.0 );
        return m;
    }
    
}
