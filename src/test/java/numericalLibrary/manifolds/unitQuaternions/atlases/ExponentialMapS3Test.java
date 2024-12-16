package numericalLibrary.manifolds.unitQuaternions.atlases;



/**
 * Implements tests methods for {@link ExponentialMapS3}.
 */
class ExponentialMapS3Test
    extends UnitQuaternionDifferentiableAtlasTester
{
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    /**
     * {@inheritDoc}
     */
    public UnitQuaternionDifferentiableAtlas getAtlas()
    {
        return new ExponentialMapS3();
    }
    
}
