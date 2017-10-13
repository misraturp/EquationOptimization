import java.util.ArrayList;
import java.util.List;

public class individual {

	double [] content = new double[10];
	double fitness_value;
	double comp_fit;
	int dim = 10;
	double probability;
	int wins=0;
	boolean used = false;
	List<Double> distances = new ArrayList<Double>();

	public individual() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public individual(double[] content, double fitness_value) 
	{
		super();
		this.content = content;
		this.fitness_value = fitness_value;
		this.comp_fit = fitness_value;
	}

}
