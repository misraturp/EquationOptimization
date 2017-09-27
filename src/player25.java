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
		System.out.print("Done!");
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
	
	public double[] getFitnessArray(double [][] individuals) {

		int size = individuals.length;
		int dim =  individuals[0].length;
		double [] fitness_matrix = new double [size];

		double[] indv = new double[dim];

		for(int i=0;i<size; i++) {
				indv = individuals[i];
				fitness_matrix[i] = (double) evaluation_.evaluate(indv);
		}
		
		return fitness_matrix;
	}
	
	
	//SELECTION ALGORITHMS//
	
	//returns best n individuals
	public double[][] selection(double [][] population, int parent_num) 
	{
		int size = population.length;
		int dim =  population[0].length;
		double[] child = new double[dim];
		double [][] parents = new double[size][parent_num];
		
		//calculate fitness of population
		double [] fitness_matrix = getFitnessArray(population);
		
		//get top 'parent_num' parents
		int count = 0;
		double curr = 0;
		double temp_max = 0;
		int index = 0;
		
		//while the number of high fitness valued individuals we found 
		//is less than the wanted num of parents
		while(count<=parent_num) {
			//get the fitness value of first individual
			temp_max = fitness_matrix[0];
			
			//iterate through whole list
			for(int j=0;j<size; j++) {
				
				//get next individual to compare (starting with the first one)
				curr = fitness_matrix[j];
				
				//if the new found fitness value is more than the one we have
				if(temp_max<curr) {
					//update max value
					temp_max = curr;
					
					//update max value found index
					index = j;
				}
			}
			
			//after iteration is done, set the fitness value of found highest to really low
			//so it won't be selected again
			fitness_matrix[index] = -100;
			
			//put the new parent to array
			parents[count] = population[index];
			
			//update necessary variables
			count++;
			curr = 0;
			temp_max = 0;
			index = 0;
		}
		
		return parents;
	}
	
	public double[][] tournamentSelection(double [][] population, int parent_num, int tournament_num, double probability){

		Random rand = new Random();

		int size = population.length;
		int dim =  population[0].length;
		double[][] parents = new double[size][dim];
		
		double[][] tributes = new double[tournament_num][dim];
		int ind = 0;
		
		//determine the tournament attenders
		for(int i=0;i<tournament_num; i++) {
				ind = rand.nextInt(size);
				tributes[i] = population[ind];
		}
		
		//get the fitness of selected individuals
		double [] fitness_array = getFitnessArray(tributes);
		
		//sorting based on fitness values**********************************************//
		double[][] sorted_tributes = new double[tournament_num][dim];
		double [] sorted_fitness_array = new double[tournament_num];
		
		int count = 0;
		double curr = 0;
		double temp_max = 0;
		int index = 0;
		
		//while the number of high fitness valued individuals we found 
		//is less than the wanted num of parents
		while(count<=tournament_num) {
			//get the fitness value of first individual
			temp_max = fitness_array[0];
			
			//iterate through whole list
			for(int j=0;j<size; j++) {
				
				//get next individual to compare (starting with the first one)
				curr = fitness_array[j];
				
				//if the new found fitness value is more than the one we have
				if(temp_max<curr) {
					//update max value
					temp_max = curr;
					
					//update max value found index
					index = j;
				}
			}
			
			//after iteration is done, set the fitness value of found highest to really low
			//so it won't be selected again
			fitness_array[index] = -100;
			
			//put the new max to array
			sorted_tributes[count] = population[index];
			sorted_fitness_array[count] = fitness_array[index];
			
			//update necessary variables
			count++;
			curr = 0;
			temp_max = 0;
			index = 0;
		}
		//**************************************************************************//
		
		int [] probabilities = new int[tournament_num];
		int prob = 0;
		int total = 0;
		
		//assign probabilities
		for(int k = 0; k<tournament_num; k++) {
			prob = (int) (probability*Math.pow((1-probability),k))*100;
			probabilities[k] = prob;
			total = total + prob;
		}
		
		//for each new parent
		for(int t=0; t<parent_num;t++) {
			
			//generate random number from 0 to total(sum of all probs)
			int generated = rand.nextInt(total);
			
			int c = 0;
			for(int p=0; p<tournament_num;p++){
				c = c + probabilities[p];
				if(generated<c) {
					//I MIGHT END UP SELECTING THE 2 OF THE SAME PARENT HERE
					parents[t] = sorted_tributes[p];				
				}
			}
		}
		
		return parents;
	}
	
	public double[][] fitnessProportionateSelection(double [][] population){
		
		int size = population.length;
		int dim = population[0].length;
		
		double [][] parents = new double[size][dim];
		
		return parents;
	}
	
	public double[][] stochasticUniversalSampling(double [][] population){

		int size = population.length;
		int dim = population[0].length;
		
		double [][] parents = new double[size][dim];
		
		return parents;
	}
	
	public double[][] uniformSelection(double [][] population) {

		int size = population.length;
		int dim = population[0].length;
		
		double [][] parents = new double[size][dim];
		
		return parents;
	}
	
	public double[][] rankBasedSelection(double [][] population) {

		int size = population.length;
		int dim = population[0].length;
		
		double [][] parents = new double[size][dim];
		
		return parents;
	}
	
	//END SELECTION ALGORITHMS//
	
	public double[][] cross_over(int point_num, double[][] parents)
	{
		int dim =  parents[0].length;
		double[][] children = new double[2][dim];
		
		int cross_point = dim/2;
		
		for(int i=0; i<dim; i++) {
			if(i<cross_point) {
				children[0][i] = parents[0][i];
				children[1][i] = parents[1][i];
			}
			else {
				children[0][i] = parents[1][i];
				children[1][i] = parents[0][i];	
			}
		}
			
		return children;
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
        	System.out.print("helo");
        	//double [][] parents = selection(population, parent_number);
        	double [][] parents = tournamentSelection(population, parent_number, 5, 0.5);
        	
        	double [][]children = cross_over(1,parents);
            // Apply crossover / mutation operators
        		        	
            //double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        	double child[] = children[0];
            // Check fitness of unknown function
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            
            double [][] population_wChildren = Arrays.copyOf(population, size+2);
            population_wChildren[size] = children[0];
            population_wChildren[size+1] = children[1];
            
            population = selection(population_wChildren, size);
            
            // Select survivors
            	//log fitness values with stats****///
        }

	}
}
