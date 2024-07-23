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
 * Implements test methods for {@link Matrix}.
 */
class MatrixTest
    implements
        AdditiveAbelianGroupElementTester<Matrix>,
        VectorSpaceElementTester<Matrix>,
        MetricSpaceElementTester<Matrix>
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
    public List<Matrix> getElementList()
    {
        List<Matrix> output = new ArrayList<Matrix>();
        output.add( Matrix.zero( 4 , 4 ) );
        output.add( Matrix.diagonal( new double[] { 1.0 , 2.0 , 3.0 , 4.0 } ) );
        output.add( Matrix.one( 4 ) );
        Random randomNumberGenerator = new Random( 42 );
        for( int i=0; i<1000; i++ ) {
            output.add( Matrix.random( 4 , 4 , randomNumberGenerator ) );
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
            Matrix m = Matrix.zero(2,2);
            double value = 123.0;
            m.setEntry( 0,0 , value );
            assertEquals( value , m.entry(0,0) );
        }
    }


    @Test
    void multiplyBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix a = createMatrixA();
            Matrix b = createMatrixB();
            Matrix ab = a.multiply( b );
            Matrix abExpected = Matrix.empty( 2 , 2 );
            abExpected.setEntry( 0,0 , -3.0 );    abExpected.setEntry( 0,1 , 2.0 );
            abExpected.setEntry( 1,0 , -5.0 );    abExpected.setEntry( 1,1 , 0.0 );
            assertTrue( ab.equals( abExpected ) );
        }
    }

    
    /**
     * Tests the behavior of {@link Matrix#choleskyDecomposition()}.
     */
    @Test
    void choleskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // Obtain the Cholesky decomposition.
            Matrix L = A.choleskyDecomposition();
            // Rebuild the matrix doing L * L^T
            Matrix LLT = L.multiply( L.transpose() );
            // We should obtain the same matrix we started with.
            assertTrue( LLT.equalsApproximately( A , 1.0e-14 ) );
        }
    }

    
    /**
     * Tests the behavior of {@link Matrix#choleskyDecompositionInplace()}.
     */
    @Test
    void choleskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // Obtain the Cholesky decomposition.
            Matrix L = A.copy().choleskyDecompositionInplace();
            // Rebuild the matrix doing L * L^T
            Matrix LLT = L.multiply( L.transpose() );
            // We should obtain the same matrix we started with.
            assertTrue( LLT.equalsApproximately( A , 1.0e-14 ) );
        }
    }


    @Test
    void LDLTBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // we obtain the LDLT decomposition
            Matrix LD = A.LDLTDecomposition();
            // we obtain L and D from LD
            Matrix L = Matrix.LfromLDLTDecomposition( LD );
            Matrix D = Matrix.DfromLDLTDecomposition( LD );
            // we rebuild the matrix doing L * D * L^T
            Matrix LDLT = L.multiply( D.multiply( L.transpose() ) );
            assertTrue( LDLT.equalsApproximately( A , 1.0e-13 ) );
        }
    }


    @Test
    void LDLTAssignBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // we obtain the LDLT decomposition
            Matrix LD = A.copy().LDLTDecompositionInplace();
            // we obtain L and D from LD
            Matrix L = Matrix.LfromLDLTDecomposition( LD );
            Matrix D = Matrix.DfromLDLTDecomposition( LD );
            // we rebuild the matrix doing L * D * L^T
            Matrix LDLT = L.multiply( D.multiply( L.transpose() ) );
            assertTrue( LDLT.equalsApproximately( A , 1.0e-13 ) );
        }
    }


    /**
     * Tests the behavior of {@link Matrix#divideRightByPositiveDefiniteUsingItsCholeskyDecomposition(Matrix)}.
     * <p>
     * Having a matrix  XA = X * A, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideRightByPositiveDefiniteUsingItsCholeskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 5 x 10.
            Matrix X = Matrix.random( 5 , 10 , this.randomNumberGenerator );
            // Multiply  X * A  to obtain the matrix we want to divide.
            Matrix XA = X.multiply( A );
            // Divide using the Cholesky decomposition: XA * A^{-1} = X
            Matrix Xrecomputed = XA.divideRightByPositiveDefiniteUsingItsCholeskyDecomposition( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which XA was generated.
            assertTrue( Xrecomputed.equalsApproximately( X , 1.0e-4 ) );
        }
    }


    /**
     * Tests the behavior of {@link Matrix#divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace(Matrix)}.
     * <p>
     * Having a matrix  XA = X * A, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 5 x 10.
            Matrix X = Matrix.random( 5 , 10 , this.randomNumberGenerator );
            // Multiply  X * A  to obtain the matrix we want to divide.
            Matrix XA = X.multiply( A );
            // Divide using the Cholesky decomposition: XA * A^{-1} = X
            XA.divideRightByPositiveDefiniteUsingItsCholeskyDecompositionInplace( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which XA was generated.
            assertTrue( XA.equalsApproximately( X , 1.0e-4 ) );
        }
    }
    
    
    /**
     * Tests the behavior of {@link Matrix#divideLeftByPositiveDefiniteUsingItsCholeskyDecomposition(Matrix)}.
     * <p>
     * Having a matrix  AX = A * X, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 10 x 5.
            Matrix X = Matrix.random( 10 , 5 , this.randomNumberGenerator );
            // Multiply  A * X  to obtain the matrix we want to divide.
            Matrix AX = A.multiply( X );
            // Divide using the Cholesky decomposition: A^{-1} * AX = X
            Matrix Xrecomputed = AX.divideLeftByPositiveDefiniteUsingItsCholeskyDecomposition( A.choleskyDecompositionInplace() );
            // Check that the resulting matrix is approximately equal to the matrix X from which AX was generated.
            assertTrue( Xrecomputed.equalsApproximately( X , 1.0e-4 ) );
        }
    }


    /**
     * Tests the behavior of {@link Matrix#divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplace(Matrix)}.
     * <p>
     * Having a matrix  AX = A * X, and being  A  a known positive definite matrix,
     * we are looking for the matrix X.
     */
    @Test
    void divideLeftByPositiveDefiniteUsingItsCholeskyDecompositionInplaceBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            // Get random positive definite matrix of dimension 10 x 10.
            Matrix X = this.randomPositiveDefiniteMatrix( 10 );
            // Generate random matrix of dimension 10 x 5.
            Matrix A = Matrix.random( 10 , 5 , this.randomNumberGenerator );
            // Multiply  A * X  to obtain the matrix we want to divide.
            Matrix XA = X.multiply( A );
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
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // we generate a random matrix
            Matrix M = Matrix.random( 5 , 10 , this.randomNumberGenerator );
            // we obtain the product
            Matrix MA = M.multiply( A );
            // and we divide using the LDLT decomposition
            Matrix Mrecomputed = MA.divideRightByPositiveDefiniteUsingItsLDLTDecomposition( A.LDLTDecompositionInplace() );
            assertTrue( Mrecomputed.equalsApproximately( M , 1.0e-4 ) );
        }
    }

    
    @Test
    void divideRightByPositiveDefiniteUsingItsLDLTDecompositionAssignBehavior()
    {
        for(int n=0; n<MatrixTest.N_SAMPLES_TO_TEST; n++) {
            Matrix A = this.randomPositiveDefiniteMatrix( 10 );
            // we generate a random matrix
            Matrix M = Matrix.random( 5 , 10 , this.randomNumberGenerator );
            // we obtain the product
            Matrix MA = M.multiply( A );
            // and we divide using the LDLT decomposition
            MA.divideRightByPositiveDefiniteUsingItsLDLTDecompositionInplace( A.LDLTDecompositionInplace() );
            assertTrue( MA.equalsApproximately( M , 1.0e-4 ) );
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * Generates a random positive definite {@link Matrix}.
     * 
     * @param dimension     dimension of the positive definite {@link Matrix} to be generated.
     * @return  random positive definite {@link Matrix}.
     */
    private Matrix randomPositiveDefiniteMatrix( int dimension )
    {
        // Generate a random matrix.
        Matrix M = Matrix.random( dimension , dimension , this.randomNumberGenerator );
        // Obtain a random positive definite matrix through M * M^T
        return M.multiply( M.transpose() );
    }



    ////////////////////////////////////////////////////////////////
    // PRIVATE STATIC METHODS
    ////////////////////////////////////////////////////////////////

    private static Matrix createMatrixA()
    {
        Matrix m = Matrix.empty( 2 , 2 );
        m.setEntry( 0,0 , 1.0 );    m.setEntry( 0,1 , 2.0 );
        m.setEntry( 1,0 , 3.0 );    m.setEntry( 1,1 , 4.0 );
        return m;
    }


    private static Matrix createMatrixB()
    {
        Matrix m = Matrix.empty( 2 , 2 );
        m.setEntry( 0,0 , 1.0 );     m.setEntry( 0,1 , -4.0 );
        m.setEntry( 1,0 , -2.0 );    m.setEntry( 1,1 , 3.0 );
        return m;
    }


    private static Matrix createMatrixC()
    {
        Matrix m = Matrix.empty( 3 , 2 );
        m.setEntry( 0,0 , 1.0 );    m.setEntry( 0,1 , 2.0 );
        m.setEntry( 1,0 , 3.0 );    m.setEntry( 1,1 , 4.0 );
        m.setEntry( 2,0 , 5.0 );    m.setEntry( 2,1 , 6.0 );
        return m;
    }


    private static Matrix createMatrixD()
    {
        Matrix m = Matrix.empty( 3 , 3 );
        m.setEntry( 0,0 , 0.0 );    m.setEntry( 0,1 , 1.0 );     m.setEntry( 0,2 , 1.0 );
        m.setEntry( 1,0 , 2.0 );    m.setEntry( 1,1 , 3.0 );     m.setEntry( 1,2 , 5.0 );
        m.setEntry( 2,0 , 8.0 );    m.setEntry( 2,1 , 13.0 );    m.setEntry( 2,2 , 21.0 );
        return m;
    }
    
}
