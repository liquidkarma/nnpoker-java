package players;

import java.util.Random;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class NeuralNetwork{
	private double fitness = 0.0;

	private int numInput = 4;
	private int numHidden = 20;
	private int numOutput = 5;

	private double inWeights[][] = null;
	private double outWeights[][] = null;

	private static Random random = new Random(System.currentTimeMillis());

	public NeuralNetwork(int numInput, int numHidden, int numOutput){
		this.numInput = numInput;
		this.numHidden = numHidden;
		this.numOutput = numOutput;

		reset();
	}

	private void reset(){
		inWeights = new double[numInput][numHidden];
		outWeights = new double[numHidden][numOutput];

		randomNet();
	}

	public NeuralNetwork copy(){
		NeuralNetwork nn = new NeuralNetwork(numInput, numHidden, numOutput);

		double [][] weights = nn.getInWeights();
		for(int i = 0; i < numInput; i++)
			for(int j = 0; j < numHidden; j++)
				weights[i][j] = inWeights[i][j];

		weights = nn.getOutWeights();
		for(int i = 0; i < numHidden; i++)
			for(int j = 0; j < numOutput; j++)
				weights[i][j] = outWeights[i][j];

		return nn;
	}

	public double[][] getInWeights(){
		return inWeights;
	}

	public double[][] getOutWeights(){
		return outWeights;
	}

	public int getNumInput(){
		return numInput;
	}

	public int getNumHidden(){
		return numHidden;
	}

	public int getNumOutput(){
		return numOutput;
	}

	private void randomNet(){
		for(int i = 0; i < numInput; i++)
			for(int j = 0; j < numHidden; j++)
				inWeights[i][j] = random.nextGaussian();

		for(int i = 0; i < numHidden; i++)
			for(int j = 0; j < numOutput; j++)
				outWeights[i][j] = random.nextGaussian();
	}

	public void save(String fileName){
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(inWeights);
			oos.writeObject(outWeights);
			oos.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void load(String fileName){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			inWeights = (double[][])ois.readObject();
			outWeights = (double[][])ois.readObject();

			numInput = inWeights.length;
			numHidden = outWeights.length;
			numOutput = outWeights[0].length;

			ois.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private double sigmoid(double x){
		if(x > 15.0)
			return 1.0;
		else if(x < -15.0)
			return 0.0;
		else
			return (1.0 / (1.0 + Math.exp(-x)));
	}

	private double symmetricSigmoid(double x){
		if(x > 15.0)
			return 0.5;
		else if(x < -15.0)
			return -0.5;
		else
			return (1.0 / (1.0 + Math.exp(-x)) - 0.5);
	}

	public void activate(double[] inputs, double[] outputs){
		double hidden[] = new double[numHidden];
		activate(inputs, hidden, outputs);
	}

	public void activate(double[] inputs, double [] hidden, double[] outputs){
		for(int j = 0; j < numHidden; j++){
			hidden[j] = 0;
			for(int i = 0; i < numInput; i++)
				hidden[j] += inputs[i] * inWeights[i][j];
			hidden[j] = sigmoid(hidden[j]);
		}

		for(int j = 0; j < numOutput; j++){
			outputs[j] = 0;
			for(int i = 0; i < numHidden; i++)
				outputs[j] += hidden[i] * outWeights[i][j];
			outputs[j] = sigmoid(outputs[j]);
		}
	}

	public double getFitness(){
		return fitness;
	}

	public void setFitness(double fitness){
		this.fitness = fitness;
	}

	public static double sumSquaredError(double [] outputs, double [] targets){
		double error = 0.0;
		int numOutput = outputs.length;
		for(int i = 0; i < numOutput; i++){
			double diff = outputs[i] - targets[i];
			error += diff * diff;
		}

		return Math.sqrt(error);
	}

	public double evaluate(double [] inputs, double [] targets){
		double [] outputs = new double[numOutput];

		activate(inputs, outputs);

		fitness = sumSquaredError(outputs, targets);

		return fitness;
	}

	public double evaluate(double [][] inputs, double [][] targets){
		int numPatterns = inputs.length;

		fitness = 0;

		if(numPatterns > 0){
			double [] outputs = new double[numOutput];

			for(int i = 0; i < numPatterns; i++){
				activate(inputs[i], outputs);
				fitness += sumSquaredError(outputs, targets[i]);
			}

			fitness /= numPatterns;
		}

		return fitness;
	}
}

// vim:noet:ts=3
