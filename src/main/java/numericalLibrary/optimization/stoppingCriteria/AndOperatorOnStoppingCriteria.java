package numericalLibrary.optimization.stoppingCriteria;


import numericalLibrary.optimization.LevenbergMarquardtAlgorithm;



public class AndOperatorOnStoppingCriteria
    implements StoppingCriterion
{
    ////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////
    private final StoppingCriterion firstStoppingCriterion;
    private final StoppingCriterion secondStoppinCriterion;
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC CONSTRUCTORS
    ////////////////////////////////////////////////////////////////
    
    public AndOperatorOnStoppingCriteria( StoppingCriterion first , StoppingCriterion second )
    {
        this.firstStoppingCriterion = first;
        this.secondStoppinCriterion = second;
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////
    
    public boolean isFinished( LevenbergMarquardtAlgorithm<?> iterativeAlgorithm )
    {
        boolean isFirstFinished = this.firstStoppingCriterion.isFinished( iterativeAlgorithm );
        boolean isSecondFinished = this.secondStoppinCriterion.isFinished( iterativeAlgorithm );
        return ( isFirstFinished && isSecondFinished );
    }
    
}
