package numericalLibrary.manifolds.unitQuaternions.atlases;



/**
 * Implements tests methods for {@link OrthographicS3}.
 */
class OrthographicS3Test
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
        return new OrthographicS3();
    }
    
}
