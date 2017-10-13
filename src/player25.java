import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class player25 implements ContestSubmission

{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
    public int evals;
    public individual[] original_population;
    public int trial = 0;
    boolean bentCig = false;
    boolean schaffers = false;
    boolean katsuura = false;
    //public double[] fitness_array;
    //public double[] fitness_temp;
	
	public player25()
	{
		rnd_ = new Random();
	}
	
	public static void main(String[] args){
		
		//ContestEvaluation con = System.getProperty("evaluation");
		
		BentCigarFunction eval = new BentCigarFunction();

		player25 player = new player25();
		player.setSeed(1);
		player.setEvaluation(eval);
		player.run();
			
		SchaffersEvaluation eval_sch = new SchaffersEvaluation();
		
		player25 player_sch = new player25();
		player_sch.setSeed(1);
		player_sch.setEvaluation(eval_sch);
		player_sch.run();
		
		KatsuuraEvaluation eval_kat = new KatsuuraEvaluation();
		
		player25 player_kat = new player25();
		player_kat.setSeed(1);
		player_kat.setEvaluation(eval_kat);
		player_kat.run();
		
		//System.out.println("Done!");
			
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algorithms random process
		rnd_.setSeed(seed);
	}
	
	public void setLimit(int limit) {
		evaluations_limit_ = limit;
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
        	if(hasStructure) {
        		//schaffers
        		schaffers = true;
        		//schaffersOptimization();
        		
        	}
        	else {
        		//katsuura
        		katsuura = true;
        		//katsuuraOptimization();
        	}
        }else{
            // Do sth else
        	//bent cigar
        	bentCig = true;
        	//bentCigarOptimization();
        }
    }
	
	//DONE
	public void initialization(int size, int dim)
	{
		int min = -5;
		int max = 5;
		Random rand = new Random();
		
		//initialize the population
		double[][] population = new double [size][dim];
		double fit = 0.0;
		
		//create random numbers from -5 to 5
		for(int i=0;i<size; i++) {
			for(int j=0;j<dim;j++) {
				population[i][j] = min + (max - min) * rand.nextDouble();
			}
			fit = (double) evaluation_.evaluate(population[i]);
			individual random_guy = new individual(population[i],fit);
			original_population[i] = random_guy;
			evals++;
		}
	}
	
	//not necessary anymore
	public void calculateFitness() {
		
		int size = original_population.length;
		//int dim =  original_population[0].dim;
		//double [] fitness_matrix = new double [size];
		
		for(int i=0;i<size; i++) {
				original_population[i].fitness_value = (double) evaluation_.evaluate(original_population[i]);
				evals++;
		}
	}
	
	public individual[] fixComp(individual[] indvs){
		
		for(int i=0;i<indvs.length;i++) {
			indvs[i].comp_fit = indvs[i].fitness_value;
		}
		
		return indvs;
	}
	
	//DONE
	public individual[] fitnessSort(individual[] indvs){
		
		int size = indvs.length;
		//int dim =  indvs[0].dim;
		
		individual[] sorted_population = new individual[size];
		
		int count = 0;
		double curr = 0;
		double temp_max = 0;
		int index = 0;
		
		//while the number of high fitness valued individuals we found 
		//is less than the wanted num of parents
		while(count<size) {
			//get the fitness value of first individual
			temp_max = indvs[0].comp_fit;
			
			//iterate through whole list
			for(int j=0;j<size; j++) {
				
				//get next individual to compare (starting with the first one)
				curr = indvs[j].comp_fit;
				
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
			//sorted_fitness_array[count] = fitness_matrix[index];
			indvs[index].comp_fit = -100;
			
			//put the new max to array
			sorted_population[count] = indvs[index];
			
			//update necessary variables
			count++;
			curr = 0;
			temp_max = 0;
			index = 0;
		}
		
		//fitness_temp = sorted_fitness_array;
		//return sorted_population
		
		fixComp(sorted_population);
		fixComp(indvs);
		
		return sorted_population;
	}
	
	
	//SELECTION ALGORITHMS//
	
	//DONE
	//returns best n individuals
	public individual[] selection(individual [] some_individuals, int parent_num) 
	{
		int size = some_individuals.length;
		//int dim =  some_individuals[0].dim;

		individual[] parents = new individual[parent_num];
		//double[] fitness_new = new double[fitness_array.length];
		
		//calculate fitness of population
		//double [] fitness_matrix = getFitnessArray(population);
		//fitness_array = getFitnessArray(population);
		
		//get top 'parent_num' parents
		int count = 0;
		double curr = 0;
		double temp_max = 0;
		int index = 0;
		
		//while the number of high fitness valued individuals we found 
		//is less than the wanted num of parents
		while(count<parent_num) {
			//get the fitness value of first individual
			temp_max = some_individuals[0].comp_fit;
			
			//iterate through whole list
			for(int j=0;j<size; j++) {
				//get next individual to compare (starting with the first one)
				curr = some_individuals[j].comp_fit;
				
				//if the new found fitness value is more than the one we have
				if(temp_max<curr) {
					//update max value
					temp_max = curr;
					
					//update max value found index
					index = j;
				}
			}
			
			//this is only relevant if done after merging children and population
			//fitness_new[count] = fitness_m[index];
			
			//after iteration is done, set the fitness value of found highest to really low
			//so it won't be selected again
			some_individuals[index].comp_fit = -100;
			
			//put the new parent to array
			parents[count] = some_individuals[index];
			index=0;			
			
			//update necessary variables
			count++;
			curr = 0;
			temp_max = 0;
			index = 0;
		}

		//if(merge){
		//	fitness_array = fitness_new;				
		//}
		
		fixComp(parents);
		fixComp(some_individuals);
		
		return parents;
	}
	
	//DONE
	public individual[] tournamentSelection(int parent_num, int tournament_num, double probability){

		Random rand = new Random();

		int size = original_population.length;
		//int dim =  population[0].length;
		individual[] parents = new individual[parent_num];
		
		individual[] tributes = new individual[tournament_num];
		//double [] tributes_fitness = new double[tournament_num];
		int ind = 0;
		int count = 0;
		List<Integer> used_1 = new ArrayList<Integer>();
		
		//determine the tournament attenders
		while(count<tournament_num) {
			
			ind = rand.nextInt(size);
			
			if(!used_1.contains(ind) && !original_population[ind].used) {
				tributes[count] = original_population[ind];
				used_1.add(ind);
				count++;
			}
		}
		
		//get the fitness of selected individuals
		//double [] fitness_array = getFitnessArray(tributes);
		
		individual[] sorted_tributes = fitnessSort(tributes);

		//double [] probabilities = new double[tournament_num];
		double prob = 0;
		double total = 0;
		
		//assign probabilities
		for(int k = 0; k<tournament_num; k++) {
			prob = (probability*Math.pow((1-probability),k));
			sorted_tributes[k].probability = prob;
			total = total + prob;
		}
		
		parents = selectBasedOnProbability(sorted_tributes, parent_num, total);
		
		
		return parents;
	}
	
	public individual[] fitnessProportionateSelection(int parent_num){
		
		int size = original_population.length;
		//int dim = original_population[0].length;
		
		individual[] parents = new individual[size];
		double total_fitness = 0;
		for(int i=0;i<size;i++) {
			total_fitness+=original_population[i].fitness_value;
		}
		
		double prob = 0;
		double total = 0;
		
		//assign probabilities
		for(int k = 0; k<size; k++) {
			prob = (double)original_population[k].fitness_value/total_fitness;
			original_population[k].probability = prob;
			total = total + prob;
		}
		
		parents = selectBasedOnProbability(original_population, parent_num, total);
		
		return parents;
	}
	
	public individual[] stochasticUniversalSampling(int parent_num){

		int size = original_population.length;
		int dim = original_population[0].dim;

		individual[] sorted_population = fitnessSort(original_population);
		individual[] parents = new individual[parent_num];
		
		int interval = (int) Math.floor(size/parent_num);
		int count = 0;
		int index = interval;
		
		while(count<parent_num){
			parents[count] = sorted_population[index-1];
			index = index + interval;
			count++;
		}
		
		return parents;
	}
	
	//DONE
	public individual[] uniformSelection(int parent_num) {

		Random rand = new Random();
		
		int size = original_population.length;
		//int dim = original_population[0].dim;
		
		individual[] parents = new individual[parent_num];

		List<Integer> used = new ArrayList<Integer>();
		int index = 0;
		int i=0;
		
		while(i<parent_num) {
			index = rand.nextInt(size);
			
			if(!used.contains(index)) {
				parents[i] = original_population[index];
				used.add(index);
				i++;
			}
		}
		
		return parents;
	}
	
	//DONE
	public individual[] rankBasedSelection(int parent_num) {

		Random rand = new Random();
		
		int size = original_population.length;
		//int dim = original_population[0].dim;
		
		individual[] parents = new individual[parent_num];
		
		//double [] fitness_array = getFitnessArray(population);		
		individual[] sorted_population = fitnessSort(original_population);
		
		//double [] probabilities = new double[size];
		double prob = 0;
		double total = 0;
		
		//assign probabilities
		for(int k = 0; k<size; k++) {
			prob = (1.0/(k+2.0));
			sorted_population[k].probability = prob;
			total = total + prob;
		}
		
		parents = selectBasedOnProbability(sorted_population, parent_num, total);
		
		/*List<Integer> used = new ArrayList<Integer>();
		int t=0;

		//System.out.println("probs assigned, starting parent selection...");
		//for each new parent
		while(t<parent_num) {
			
			//generate random number from 0 to total(sum of all probs)
			double generated = total * rand.nextDouble();
			
			double c = 0;
			
			//iterates over whole population until finds to which individual the
			//generated number refers
			for(int p=0; p<size;p++){
				
				c = c + sorted_population[p].probability;
				
				if(generated<c) {
					
					if(!used.contains(p)) {
						//System.out.println("parent selected");
						parents[t] = sorted_population[p];	
						used.add(p);
						t++;
						break;
					}
					else {
						break;
					}
				}
			}
		}*/
		
		return parents;
	}
	
	//END SELECTION ALGORITHMS//
	
	//STILL ASSUMES 2 CHILDREN
	
	//CROSS-OVER ALGORITHMS//
	
	//FIX THIS TO INCLUDE MORE POINTS
	public individual[] cross_over(individual[] parents, int point_num)
	{
		Random rand = new Random();
		int size = parents.length;
		int dim =  parents[0].dim;
		individual[] children = new individual[parents.length];
		for(int x=0;x<children.length;x++){
			children[x] = new individual();
		}
		
		int cross_point = dim/2;
		List<Integer> used = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		int count = 0;
		int child_index = 0;
		
		individual parent1 = new individual();
		individual parent2 = new individual();
		
		//randomly assign parents together
		while(used.size()<size) {
			
			//select the first parent
			while(count == 0) {
				num1 = rand.nextInt(size); 
				if(!used.contains(num1)) {
					parent1 = parents[num1];
					count++;
				}
			}
			count = 0;
			used.add(num1);
			
			//select the second parent
			while(count == 0) {
				num2 = rand.nextInt(size);
				if(!used.contains(num2)){
					parent2 = parents[num2];
					count++;
				}
			}
			count = 0;
			used.add(num2);
			
			for(int i=0; i<dim; i++) {
				if(i<cross_point) {
					children[child_index].content[i] = parent1.content[i];
					children[child_index+1].content[i] = parent2.content[i];
				}
				else {
					children[child_index].content[i] = parent2.content[i];
					children[child_index+1].content[i] = parent1.content[i];	
				}
			}
			child_index = child_index + 2;
		}

		
		return children;
	}

	public individual[] fixed_cross_over(int point_num)
	{
		int size = original_population.length;
		int dim =  original_population[0].dim;
		individual[] children = new individual[original_population.length];
		for(int x=0;x<children.length;x++){
			children[x] = new individual();
		}
		
		int cross_point = dim/2;
		List<Integer> used = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		int count = 0;
		int child_index = 0;
		
		individual parent1 = new individual();
		individual parent2 = new individual();
		
		//randomly assign parents together
		while(child_index<size-2) {
						
			individual[] parents = rankBasedSelection(2);
			
			parent1 = parents[0];
			num1 = indexOf(original_population,parent1);
			original_population[num1].used = true;
					
			parent2 = parents[1];
			num2 = indexOf(original_population,parent2);
			original_population[num2].used = true;
			
			for(int i=0; i<dim; i++) {
				if(i<cross_point) {
					children[child_index].content[i] = parent1.content[i];
					children[child_index+1].content[i] = parent2.content[i];
				}
				else {
					children[child_index].content[i] = parent2.content[i];
					children[child_index+1].content[i] = parent1.content[i];	
				}
			}
			child_index = child_index + 2;
		}

		for(int m=0;m<size;m++){
			original_population[m].used=false;
		}
		return children;
	}
	
	//DONE
	public individual[] uniform_cross_over(){
		
		Random rand = new Random();

		int size = original_population.length;
		int dim = original_population[0].dim;
		individual[] children = new individual [size];
		for(int x=0;x<children.length;x++){
			children[x] = new individual();
		}

		List<Integer> used = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		int count = 0;
		int child_index = 0;
		
		individual parent1 = new individual();
		individual parent2 = new individual();
		
		//randomly assign parents together
		while(used.size()<size) {
			
			//select the first parent
			while(count == 0) {
				num1 = rand.nextInt(size); 
				if(!used.contains(num1)) {
					parent1 = original_population[num1];
					count++;
				}
			}
			count = 0;
			used.add(num1);
			
			//select the second parent
			while(count == 0) {
				num2 = rand.nextInt(size);
				if(!used.contains(num2)){
					parent2 = original_population[num2];
					count++;
				}
			}
			count = 0;
			used.add(num2);
			
			for(int j=0; j<dim; j++) {
				if(Math.random()<0.5) {
					children[child_index].content[j] = parent1.content[j];
					children[child_index+1].content[j] = parent2.content[j];
				}
				else {
					children[child_index].content[j] = parent2.content[j];
					children[child_index+1].content[j] = parent1.content[j];
				}
			}
			child_index = child_index + 2;
		}
		
		
		return children;
	}
	
	public individual[] fixed_uniform_cross_over(){
		
		Random rand = new Random();

		int size = original_population.length;
		int dim = original_population[0].dim;
		individual[] children = new individual [size];
		for(int x=0;x<children.length;x++){
			children[x] = new individual();
		}

		List<Integer> used = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		int count = 0;
		int child_index = 0;
		
		individual parent1 = new individual();
		individual parent2 = new individual();
		
		//randomly assign parents together
		while(child_index<size-2) {

			individual[] parents = tournamentSelection(2, 3, 0.3);
			
			parent1 = parents[0];
			num1 = indexOf(original_population,parent1);
			original_population[num1].used = true;
					
			parent2 = parents[1];
			num2 = indexOf(original_population,parent2);
			original_population[num2].used = true;
			
			for(int j=0; j<dim; j++) {
				if(Math.random()<0.5) {
					children[child_index].content[j] = parent1.content[j];
					children[child_index+1].content[j] = parent2.content[j];
				}
				else {
					children[child_index].content[j] = parent2.content[j];
					children[child_index+1].content[j] = parent1.content[j];
				}
			}
			child_index = child_index + 2;
			//System.out.println(child_index);
		}

		for(int m=0;m<size;m++){
			original_population[m].used=false;
		}
		
		return children;
	}
	
	public int getClosestGuy(int in, individual [] all_the_guys){
		
		int dim = all_the_guys[0].dim;
		double min_distance=10.0;
		int index = 0;
		individual the_guy = all_the_guys[in];
		
		min_distance = 10.0;
		double distance=0.0;
		//look at all the other guys
		for(int j=0;j<all_the_guys.length;j++) {

			distance = 0.0;
			double sum = 0.0;
			
			//calculate distance
			if(in!=j) {
				for(int x=0;x<dim;x++) {
						double difference = Math.abs(the_guy.content[x] - all_the_guys[j].content[x]);
						sum += difference;
					}
				distance = sum/10;
				if(distance<min_distance) {
					min_distance = distance;
					index = j;
				}
			}
			/////////////////////////
		}
		//all_the_guys[j]
		return index;
	}
	
	
	public individual[] close_cross_over(){
		
		Random rand = new Random();

		int size = original_population.length;
		int dim = original_population[0].dim;
		individual[] children = new individual [size];
		for(int x=0;x<children.length;x++){
			children[x] = new individual();
		}

		List<Integer> used = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		int count = 0;
		int child_index = 0;
		
		individual parent1 = new individual();
		individual parent2 = new individual();
		
		//randomly assign parents together
		while(used.size()<size) {
			
			//select the first parent
			while(count == 0) {
				num1 = rand.nextInt(size); 
				if(!used.contains(num1)) {
					parent1 = original_population[num1];
					original_population[num1].used = true;
					count++;
				}
			}
			count = 0;
			used.add(num1);

			num2 = getClosestGuy(num1, original_population);
			parent2 = original_population[num2];
			used.add(num2);
			original_population[num2].used = true;
			
			for(int j=0; j<dim; j++) {
				if(Math.random()<0.5) {
					children[child_index].content[j] = parent1.content[j];
					children[child_index+1].content[j] = parent2.content[j];
				}
				else {
					children[child_index].content[j] = parent2.content[j];
					children[child_index+1].content[j] = parent1.content[j];
				}
			}
			child_index = child_index + 2;
		}

		for(int m=0;m<size;m++){
			original_population[m].used=false;
		}

		
		return children;
	}
	//END CROSS-OVER ALGORITHMS//
	
	//DONE
	
	public double getMaxValue(double[] array) {
	    double maxValue = 0;
	    for (int i = 1; i < array.length; i++) {
	        if (array[i] > maxValue) {
	            maxValue = array[i];
	        }
	    }
	    return maxValue;
	}
	
	
	public individual[] selectBasedOnProbability(individual[] indvs, int selection_num, double total) {
		
		Random rand = new Random();
		int size = indvs.length;
		
		individual [] selected = new individual [selection_num];
		
		List<Integer> used = new ArrayList<Integer>();
		int t=0;

		//for each new parent
		while(t<selection_num) {
			
			//generate random number from 0 to total(sum of all probs)
			double generated = total * rand.nextDouble();
			
			double c = 0;
			for(int p=0; p<size;p++){
				
				c = c + indvs[p].probability;
				
				if(generated<c && !used.contains(p) && !indvs[p].used) {
					
					selected[t] = indvs[p];	
					used.add(p);
					t++;
					break;
				}
			}
		}
		return selected;
	}
	
	//MUTATION ALGORITHMS//
	
	//DONE
	public individual[] mutation(individual[] children){

		
		for(int i=0; i<children.length; i++) {
			if(Math.random()>0.6) {
				for(int j=0; j<children[0].dim;j++) {
					if(Math.random()>0.8 && children[i].content[j]>-4.6 && children[i].content[j]<4.6){
						if(Math.random()<0.5) {
							//children[i].content[j]--;
							children[i].content[j] = children[i].content[j] - 0.4;
						}else {
							//children[i].content[j]++;
							children[i].content[j] = children[i].content[j] + 0.4;
						}
					}
				}
			}
		}

		for(int a=0; a<children.length;a++) {
			children[a].fitness_value = (double) evaluation_.evaluate(children[a].content);
			children[a].comp_fit=children[a].fitness_value;
			evals++;
		}
		
		return children;
	}
		
	public double getMaxFitness(individual[] array) {
		double max = 0.0;
		for(int i=0;i<array.length;i++) {
			double cur = array[i].fitness_value;
			if(cur>max) {
				max = cur;
			}
		}
		return max;
	}
	
	public double getAvgFitness(individual[] array) {
		double sum = 0.0;
		for(int i=0;i<array.length;i++) {
			sum = sum+array[i].fitness_value;
		}
		return sum/array.length;
	}
	
	
	public individual[] adaptive_mutation(individual[] children){
		
		Random rand = new Random();
		double gaussian = 0.0;
		double real_scale = 0.5;
		if(schaffers) {
			real_scale = 0.5;
			double eval_fitness = getAvgFitness(original_population);
			if(eval_fitness>2) {
				real_scale = 1/(10*Math.log(eval_fitness));
			}
		}
		else if(bentCig)
		{
			real_scale = 0.5;
			double eval_fitness = getAvgFitness(original_population);
			if(eval_fitness>2) {
				real_scale = 1/(100*Math.log(eval_fitness));
			}
		}
		else
		{
			real_scale = 0.01;
			double eval_fitness = getAvgFitness(original_population);
			if(eval_fitness>3) {
				real_scale = 1/(10*Math.log(eval_fitness));
			}
		}
		
		for(int i=0;i<children.length;i++) {
			
			for(int j=0;j<children[0].dim;j++) {
				
				gaussian = rand.nextGaussian()*real_scale; 
				
				if(children[i].content[j]+gaussian>5)
				{
					children[i].content[j] = 5;
				}
				else if(children[i].content[j]+gaussian<-5)
				{
					children[i].content[j] = -5;					
				}
				else {
					children[i].content[j] = children[i].content[j]+gaussian; 
				}
			}
		}
		
		for(int a=0; a<children.length;a++) {
			children[a].fitness_value = (double) evaluation_.evaluate(children[a].content);
			children[a].comp_fit=children[a].fitness_value;
			evals++;
		}
		
		return children;
	}
	
	
	public individual[] gaussian_mutation(individual[] children, double scale){
		
		Random rand = new Random();
		double gaussian = 0.0;
		
		for(int i=0;i<children.length;i++) {
			
			for(int j=0;j<children[0].dim;j++) {
				
				gaussian = rand.nextGaussian()*scale; 
				
				if(children[i].content[j]+gaussian>5)
				{
					children[i].content[j] = 5;
				}
				else if(children[i].content[j]+gaussian<-5)
				{
					children[i].content[j] = -5;					
				}
				else {
					children[i].content[j] = children[i].content[j]+gaussian; 
				}
			}
		}
		
		for(int a=0; a<children.length;a++) {
			children[a].fitness_value = (double) evaluation_.evaluate(children[a].content);
			children[a].comp_fit=children[a].fitness_value;
			evals++;
		}
		
		return children;
	}
	
	//END MUTATION ALGORITHMS
	
	//SURVIVOR SELECTION ALGORITHMS//
	
	public individual[] fitness_sharing(individual[] pop_wChildren, double range) {
		
		int size = pop_wChildren.length;
		int dim = pop_wChildren[0].dim;
		double distance = 0.0;
		
		for(int i=0;i<size;i++) 
		{
			for(int j=0;j<size;j++) 
			{
				double sum = 0.0;
				for(int x=0;x<dim;x++) {
					double a = pop_wChildren[i].content[x];
					double b = pop_wChildren[j].content[x];
					double difference = Math.abs(a - b);
					sum = sum + difference;
				}
				distance = sum/10;
				if(distance<range) {
					pop_wChildren[i].distances.add(distance);
				}
			}
			double denom = 1;
			List<Double> dis_list =  pop_wChildren[i].distances;
			
			for(int t=0;t<dis_list.size();t++) 
			{
				denom = 1-dis_list.get(t)/range;
				//System.out.println(denom);
			}
			
			pop_wChildren[i].comp_fit = pop_wChildren[i].fitness_value/denom;
			
			if(Double.isInfinite(pop_wChildren[i].comp_fit)) {
				pop_wChildren[i].comp_fit = 0.0;
				System.out.println("Found it");
			}
			
			pop_wChildren[i].distances = new ArrayList<Double>();
			
		}
		return pop_wChildren;
	}
	
	//DONE
	public individual[] generational_selection(individual[] pop_wChildren){
		
		int size = original_population.length;
		//int dim = original_population[0].dim;
		individual[] new_generation = new individual [size];
		
		for(int i=0;i<size;i++) {
			new_generation[i] = pop_wChildren[size+i];
		}
		
		return new_generation;
	}
	
	//DONE
	public individual[] elitism(individual[] pop_wChildren, int best_n){
		
		Random rn = new Random();

		int size = original_population.length;
		//int dim = pop_wChildren[0].dim;
		individual[] new_generation = new individual [size];
		
		/*outdated start*/
		//copy fitness array to avoid pass by reference and
		//unwanted modifications in the original fitness array
		/*double[] fitness_old = Arrays.copyOf(fitness_array, population.length);

		//add children's fitness value to the fitness_old
		for(int j=fitness_array.length;j<fitness_old.length;j++) {
			if(evals<evaluations_limit_) {
    			double ev = (double) evaluation_.evaluate(population[j]);
    			fitness_old[j]= ev;
    			evals++;
			}
		}*/
		/*outdated end*/

		//get the list sorted
		pop_wChildren = fitnessSort(pop_wChildren);

		List<Integer> used = new ArrayList<Integer>();
		boolean skipped = true;
		
        for(int j=0;j<best_n;j++) {
        	new_generation[j] = pop_wChildren[j];
        	used.add(j);
        }
        
        int index = 0;
        for(int k=best_n;k<size;k++){
        	
        	while(skipped) {
	        	index = rn.nextInt((size-1 - best_n) + 1) + best_n;
	        	
	        	if(!used.contains(index)) {
	        		new_generation[k] = original_population[index];
	        		used.add(index);
	        		skipped = false;
	        	}
        	}
        	skipped = true;
        }
        
		return new_generation;
	}
	
	//DONE
	public individual[] genitor_selection(individual[] pop_wChildren){

		int size = original_population.length;
		//int dim = original_population[0].dim;
		individual [] new_generation = new individual [size];
		
		/*outdated start*/
		//copy fitness array to avoid pass by reference and
		//unwanted modifications in the original fitness array
		//double[] fitness_old = Arrays.copyOf(fitness_array, population.length);

		//add children's fitness value to the fitness_old
		//for(int j=fitness_array.length;j<fitness_old.length;j++) {
		//	if(evals<evaluations_limit_) {
    	//		double ev = (double) evaluation_.evaluate(population[j]);
    	//		fitness_old[j]= ev;
    	//		evals++;
		//	}
		//}
		/*outdated end*/
		
		//get best of all
        new_generation = selection(pop_wChildren, size);
		
		return new_generation;
	}
	
	//DONE
	
	public individual[] round_robin(individual[] pop_wChildren, int competition_num , int competitor_num) {

		Random rand = new Random();
		
		int size = original_population.length;
		//int dim = original_population[0].dim;
		individual[] new_generation = new individual [size];
		individual[] competitors = new individual[competitor_num];
		
		List<Integer> used = new ArrayList<Integer>();
		
		int count=0;
		//round
		while(count<competition_num) {
			//per individual
			for(int i=0;i<pop_wChildren.length;i++) {
				
				//add first guy into the competitor list
				competitors[0] = pop_wChildren[i];
				used.add(i);
				
				//populate competitor list
				for(int k=1;k<competitor_num;k++) {
					int index = rand.nextInt(pop_wChildren.length);
					if(!used.contains(index)) {
						competitors[k] = pop_wChildren[index];
						used.add(index);
					}
				}
				
				//BURASI YANLIS OLMUS BURAYI SAAPALIM//
			
				//FIGHT!
				competitors = fitnessSort(competitors);

				//distribute the win values
				for(int el=0; el<competitors.length;el++){
					competitors[el].wins=competitors[el].wins+competitors.length-el-1;
				}
			
			}
			count++;
		}
		
		//reset wins values
		for(int eleman=0; eleman<new_generation.length;eleman++){
			new_generation[eleman].wins=0;
		}
		return new_generation;
	}
	
	//END SURVIVOR SELECTION ALGORITHMS//
	
	public double[] populateFitnessArray() {
		double[] hele = new double[original_population.length];
		
		for(int i=0; i<hele.length;i++) {
			hele[i] = original_population[i].comp_fit;
		}
		return hele;
	}
	
	
	public void bentCigarOptimization()
	{
		// Run your algorithm here
		//int iterations = evaluations_limit_/10000;
		//for(int trial=0;trial<iterations;trial++) {
			
	        evals = 0;
	        int size = 50;
	        int dimension = 10;
	        double max_fitness = 0;
	        double best_fitness = 0;
	        double[] fitness_array = new double[size];
	        original_population = new individual[size];
	        int optimum_eval=0;
	        
	        //Remember to always give an even number
	        int parent_number = 50;
	        
	        // init population
	        initialization(size, dimension);
			//calculateFitness();
	
	    	//System.out.println("Starting...");
	        // calculate fitness
	        while(evals<evaluations_limit_){
	//        	/evaluations_limit_
	        	//System.out.println("evals:" + evals);
	            // Select parents        	
	        	//double [][] parents = selection(population, parent_number);
	        	//individual[] parents = rankBasedSelection(parent_number);
	        	//individual[] parents = tournamentSelection(parent_number, 50, 0.3);
	        	//System.out.println("parents chosen");

	            // Apply crossover / mutation operators
	        	individual[] children = fixed_uniform_cross_over();
	        	//System.out.println("children created");
	        	//children = gaussian_mutation(children, 0.01);
	        	children = adaptive_mutation(children);
	        		     

	            individual[] population_wChildren = Arrays.copyOf(original_population, size+children.length);
	            
	            for(int k=size;k<size+children.length;k++) {
	            	
	            	population_wChildren[k] = children[k-size];
	            }
	
	
	    		//genitor selection
	            original_population = selection(population_wChildren, size);
	            //original_population = elitism(population_wChildren, 5);
	            //original_population = round_robin(population_wChildren, 3, 5);
	        	//System.out.println("new population created");
	
	            fitness_array = populateFitnessArray();
	            best_fitness = getMaxValue(fitness_array);
	            if(max_fitness<best_fitness) {
	            	max_fitness = best_fitness;
	            	optimum_eval=evals;
	            	//System.out.println(optimum_eval);
	            }
	
	        	//System.out.print(".");
	        	
	            // Select survivors
	            	//log fitness values with stats****///
	        }
	        
	        //System.out.println("Bent Cigar Function, trial."+trial);
	        //trial++;
	        System.out.println("Best fitness: " + max_fitness);  
	        System.out.println("Best fitness found: "+optimum_eval);
	        //System.out.println(evals);
	        
	        //return best_fitness;

	    //}
	}
	
	public int indexOf(individual[] array, individual object) {
		for(int i=0;i<array.length;i++) {
			if(array[i]==object) {
				return i;
			}
		}
		return -1;
	}

	public void crowding(individual[] parents, individual[] children){
		
		int size = parents.length;
		int dim = parents[0].dim;
		List<Integer> used = new ArrayList<Integer>();
		boolean found = false;
		individual[] survivors = new individual[size];
		int count = 0;
		
		while(count<size) {
			for(int i=0; i<parents.length;i++) {
				
				double distance = 10;
				int index = 0;
				
				//iterate every children
				for(int j=0; j<children.length;j++) {
					
					double sum = 0.0;
					
					//calculate the distance
					for(int x=0;x<dim;x++) {
						double difference = Math.abs(parents[i].content[x]-children[j].content[x]);
						sum = sum + difference;
					}
					
					double cur_dis = sum/dim;
					
					//if this distance is smaller than the previously smallest
					//and this child is not used yet
					if(cur_dis<distance && !used.contains(j)) {
						distance = cur_dis;
						index = j;
					}
				}
	
				if(parents[i].fitness_value<children[index].fitness_value) {
					int original_place = indexOf(original_population, parents[i]);
					original_population[original_place] = children[index];
					survivors[count] =  children[index];
					used.add(index);
					count++;
				}
				else {
					survivors[count] =  parents[i];
					//used.add(index);
					count++;
				}
			}
		}
		//return survivors;
	}
	
	public void schaffersOptimization()
	{
		// Run your algorithm here
		//int iterations = evaluations_limit_/10000;
		//for(int trial=0;trial<iterations;trial++) {
			
	        evals = 0;
	        int size = 50;
	        int dimension = 10;
	        double max_fitness = 0;
	        double best_fitness = 0;
	        double[] fitness_array = new double[size];
	        original_population = new individual[size];
	        int optimum_eval=0;
	        
	        //Remember to always give an even number
	        int parent_number = 50;
	        
	        // init population
	        initialization(size, dimension);
			//calculateFitness();
	
	    	//System.out.println("Starting...");
	        // calculate fitness
	        while(evals<evaluations_limit_){
	//        	/evaluations_limit_
	        	//System.out.println("evals:" + evals);
	            // Select parents        	
	        	
	        	//double [][] parents = selection(population, parent_number);
	        	//individual[] parents = rankBasedSelection(parent_number);
	        	//individual[] parents = stochasticUniversalSampling(parent_number);
	        	
	        	//individual[] parents = tournamentSelection(parent_number, 50, 0.3);
	        	//individual[] parents = original_population;
	        	//System.out.println("parents chosen");
	        	
	            // Apply crossover / mutation operators
	        	individual[] children = fixed_cross_over(1);
	        	//System.out.println("children created");
	        	children = adaptive_mutation(children);
	        	//DON'T FORGET TO REMOVE PARENTS IN THE NEXT STEP THEN!!
	        	crowding(original_population, children);
	            
	            /*individual[] population_wChildren = Arrays.copyOf(original_population, size+children.length);
	            
	            for(int k=size;k<size+children.length;k++) {
	            	
	            	population_wChildren[k] = children[k-size];
	            }*/
	
	    		//genitor selection
	            //original_population = selection(population_wChildren, size);
	            //original_population = generational_selection(population_wChildren);
	            //original_population = elitism(population_wChildren, 5);
	            //original_population = round_robin(population_wChildren, 3, 5);
	        	//System.out.println("new population created");
	
	            fitness_array = populateFitnessArray();
	            best_fitness = getMaxValue(fitness_array);
	            if(max_fitness<best_fitness) {
	            	max_fitness = best_fitness;
	            	optimum_eval=evals;
	            	//System.out.println(optimum_eval);
	            }
	
	        	//System.out.print(".");
	        	
	            // Select survivors
	            	//log fitness values with stats****///
	        }
	        
	        //System.out.println("Schaffers function, trial."+trial);
	        //trial++;
	        System.out.println("Best fitness: " + max_fitness);  
	        System.out.println("Best fitness found: " + optimum_eval);
	        //System.out.println(evals);
	        
	        //return best_fitness;

	    //}
	}
	
	public void katsuuraOptimization()
	{
		// Run your algorithm here
		//int iterations = evaluations_limit_/10000;
		//for(int trial=0;trial<iterations;trial++) {
			
	        evals = 0;
	        int size = 100;
	        int dimension = 10;
	        double max_fitness = 0;
	        double best_fitness = 0;
	        double[] fitness_array = new double[size];
	        original_population = new individual[size];
	        int optimum_eval=0;
	        
	        //Remember to always give an even number
	        int parent_number = 5000;
	        
	        // init population
	        initialization(size, dimension);
	        //individual[] meh = fitnessSort(original_population);
	        //System.out.println(meh[0]);
			//calculateFitness();
	
	    	//System.out.println("Starting...");
	        // calculate fitness
	        while(evals<evaluations_limit_){
	//        	/evaluations_limit_
	        	System.out.println("evals:" + evals);
	            // Select parents        	
	        	//double [][] parents = selection(population, parent_number);
	        	//individual[] parents = rankBasedSelection(parent_number);
	        	//individual[] parents = tournamentSelection(parent_number, 50, 0.3);
	        	//System.out.println("parents chosen");
	        	
	            // Apply crossover / mutation operators
	        	//individual[] children = uniform_cross_over(parents);
	        	individual[] children = close_cross_over();
	        	//System.out.println("children created");
	        	//children = gaussian_mutation(children);
	        	children = adaptive_mutation(children);
	        	//crowding(original_population, children);
	        		     
	            
	            individual[] population_wChildren = Arrays.copyOf(original_population, size+children.length);
	            
	            for(int k=size;k<size+children.length;k++) {
	            	
	            	population_wChildren[k] = children[k-size];
	            }
	
	            population_wChildren = fitness_sharing(population_wChildren,1);
	    		//genitor selection
	            //original_population = selection(population_wChildren, size);
	            original_population = elitism(population_wChildren, 5);
	            //original_population = round_robin(population_wChildren, 3, 5);
	        	//System.out.println("new population created");
	
	            fitness_array = populateFitnessArray();
	            best_fitness = getMaxValue(fitness_array);
	            if(max_fitness<best_fitness) {
	            	max_fitness = best_fitness;
	            	optimum_eval=evals;
	            	//System.out.println(optimum_eval);
	            }
	
	        	//System.out.print(".");
	        	
	            // Select survivors
	            	//log fitness values with stats****///
	        }
	        
	        System.out.println("Katsuura function, trial."+trial);
	        trial++;
	        System.out.println("Best fitness: " + max_fitness);  
	        System.out.println("Best fitness found: "+ optimum_eval);
	        //System.out.println(evals);
	        
	        //return best_fitness;

	    //}
	}
	
	
	
	public void run()
	{		
		if(bentCig) {
			bentCigarOptimization();
		}
		else if(schaffers) {
			schaffersOptimization();
		}
		else if(katsuura) {
			katsuuraOptimization();
		}
	}
}
