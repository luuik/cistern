package marmot.lemma.transducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import org.javatuples.Pair;

import marmot.lemma.Instance;
import marmot.lemma.LemmatizerTrainer;
import marmot.lemma.transducer.exceptions.NegativeContext;

public abstract class Transducer implements LemmatizerTrainer {
	
	// context values
	// See Cotterell et al. (2014) for more details
	// c1 = upper left hand context
	// c2 = upper right hand context
	// c3 = lower left hand context
	// c4 = lower right hand context
	protected int c1;
	protected int c2;
	protected int c3;
	protected int c4;
	
	// arrays for dynamic programming
	protected double[][] alphas;
	protected double[][] betas;
	
	// context to int assignment
	protected int[][] contexts;
	
	//data
	protected List<Instance> trainingData;
	protected List<Instance> devData;
	
	protected Set<Character> alphabet;
	
	public Transducer(Set<Character> alphabet, int c1, int c2, int c3, int c4) throws NegativeContext {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		
		if (this.c1 < 0 || this.c2 < 0 || this.c3 < 0 || this.c4 < 0) {
			throw new NegativeContext();
		}
		
		this.alphabet = alphabet;
	}
	
	
	@SuppressWarnings("unchecked")
	protected void extractContexts() {
		List<Set<Character>> cartesianProductArgs = new ArrayList<Set<Character>>();
		for (int i = 0; i < this.c1 + this.c2 + this.c3 + this.c4; ++i) {
			cartesianProductArgs.add(this.alphabet);
		}
				
		Sets.cartesianProduct((Set[]) cartesianProductArgs.toArray());
		
	}
	
	/**
	 *  zerosOut an array in the log semiring.
	 * @param array
	 */
	protected static void zeroOut(double[][] array) {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[0].length; ++j) {
				array[i][j] = Double.NEGATIVE_INFINITY;
			}
		}
	}
	
	/**
	 *  zerosOut an array in the log semiring.
	 * @param array
	 */
	protected static void zeroOut(double[][] array, int first, int second) {
		for (int i = 0; i < first; ++i) {
			for (int j = 0; j < second; ++j) {
				array[i][j] = Double.NEGATIVE_INFINITY;
			}
		}
	}
	

	protected Pair<int[][][],Integer> preextractContexts(List<Instance> instances, int c1, int c2, int c3, int c4) {
		int[][][] contexts = new int[instances.size()][][];
		
		String END_SYMBOL = "$";
		String BREAK_SYMBOL = "*****";
		
		//String upper = "abcd";
		//String lower = "efgh";
		
		Map<String,Integer> hash = new HashMap<String,Integer>();
		int counter = 0;
		int instanceI = 0;
		
		for (Instance instance : instances) {
			
			String upper = instance.getForm();
			String lower = instance.getLemma();
			
			contexts[instanceI] = new int[upper.length()][lower.length()];
			
			for (int i = 0; i < upper.length(); ++i) {

				int ul_limit = Math.max(0, i - c1);
				int ur_limit = Math.min(upper.length() - 1, i + c2);

				String ul = upper.substring(ul_limit, i);
				String ur = upper.substring(i, ur_limit);

				// pad
				while (ul.length() < c1) {
					ul = END_SYMBOL + ul;
				}

				while (ur.length() < c2) {
					ur = ur + END_SYMBOL;
				}

				for (int j = 0; j < lower.length(); ++j) {

					int ll_limit = Math.max(0, j - c3);
					int lr_limit = Math.min(lower.length() - 1, j + c4);

					String ll = lower.substring(ll_limit, j);
					String lr = lower.substring(j, lr_limit);

					// pad
					while (lr.length() < c3) {
						lr = lr + END_SYMBOL;
					}
					while (ll.length() < c4) {
						ll = END_SYMBOL + ll;
					}

					// hash the string
					String contextString = ul + BREAK_SYMBOL + ur
							+ BREAK_SYMBOL + ll + BREAK_SYMBOL + lr;

					/*
					System.out.println(i);
					System.out.println(j);
					System.out.println("C1: " + ul + ", C2: " + ur + ", C3: "
							+ ll + ", C4: " + lr);
					System.out.println();
					*/
					if (!hash.keySet().contains(contextString)) {
						hash.put(contextString, counter);
						++counter;
					}
					contexts[instanceI][i][j] = hash.get(contextString);

				}
			}
			instanceI += 1;
		}
		
		return new Pair<int[][][],Integer> (contexts,counter-1);
	}
	
	protected abstract void gradient(double[] gradient);
	protected abstract void gradient(double[] gradient, int instanceId);
	protected abstract double logLikelihood(int instanceId);
	protected abstract double logLikelihood();
	
}
