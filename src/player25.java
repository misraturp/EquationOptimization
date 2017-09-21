import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Arrays;
import java.util.Properties;

public class player25 implements ContestSubmission

{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player25()
	{
		rnd_ = new Random();
	}
	
	public static void main(String[] args){
		double [] array = {1.4,2,4,7.3,1.6};
		Arrays.sort(array);
		System.out.print("didsjfjf");
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
	
	public double [][] initialization(int size, int dim)
	{
		int min = -5;
		int max = 5;
		Random rand = new Random();
		
		//initialize the population
		double[][] population = new double [size][dim];
		
		//create random numbers from -5 to 5
		for(int i=0;i<size; i++) {
			for(int j=0;j<dim;j++) {
				population[i][j] = min + (max - min) * rand.nextDouble();
			}
		}
		
		return population;
	}
	
	public double[][] selection(double [][] population, int parent_num) 
	{
		int size = population.length;
		int dim =  population[0].length;
		double[] child = new double[dim];
		double [][] parents = new double[size][parent_num];
		
		//calculate fitness of population
		double [] fitness_matrix = new double [size];
	
		for(int i=0;i<size; i++) {
				child = population[i];
				fitness_matrix[i] = (double) evaluation_.evaluate(child);
		}
		
		//get top 'parent_num' parents
		int count = 0;
		double curr = 0;
		double temp_max = 0;
		int index = 0;
		
		while(count<=parent_num) {
			temp_max = fitness_matrix[0];
			
			for(int j=0;j<size; j++) {
				curr = fitness_matrix[j];
				
				if(temp_max<curr) {
					temp_max = curr;
					index = j;
				}
			}
			fitness_matrix[index] = -100;
			parents[count] = population[index];
			count++;
			curr = 0;
			temp_max = 0;
			index = 0;
		}
		
		return parents;
	}
	
	public void cross_over(int point_num, double parent, double [][] children)
	{
			
	}
    
	public void run()
	{
		// Run your algorithm here
        
        int evals = 0;
        int size = 10;
        int dimention = 10;
        int parent_number = 2;
        
        // init population
        double [][] population = initialization(size, dimention);
        		
        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
        	double [][] parents = selection(population, parent_number);
        	
            // Apply crossover / mutation operators
        		
        	
            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown function
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            // Select survivors
            	//log fitness values with stats****///
        }

	}
}
