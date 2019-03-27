import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class Bigram {
	public Set<String> samples;
	public HashMap<String, HashMap<String, Double>> numbers;
	public HashMap<String, Double> unigramNumbers;
	public final String START = ":S";	
	public Set<String> words; 
	public double vocab;
	public HashMap<Double, Double> numberOfBigramsWithCount;

	public Bigram(Set<String> samples) {
		this.samples = samples;
		this.numbers = new HashMap<String, HashMap<String, Double>>();
		this.unigramNumbers = new HashMap<String, Double>();
		this.words = new HashSet<String>();
		this.numberOfBigramsWithCount = new HashMap<Double, Double>();
	}

	public void modelling() {
		
		String regexp = "('?\\w+|\\p{Punct})";
		Pattern pattern = Pattern.compile(regexp);
		for (String sample : samples) {
			Matcher matcher = pattern.matcher(sample);
			String previousWord = START; 
			while (matcher.find()) {
				
				double unigramCount = 0.0;
				if (unigramNumbers.containsKey(previousWord)) {
					unigramCount = unigramNumbers.get(previousWord);
				}
				unigramNumbers.put(previousWord, unigramCount + 1.0);				
				String match = matcher.group();
				words.add(match);
				
				HashMap<String, Double> innerCounts;
				if (numbers.containsKey(previousWord)) {
					innerCounts = numbers.get(previousWord);
				} else {
					innerCounts = new HashMap<String, Double>();
					numbers.put(previousWord, innerCounts);
				}

				double count = 0.0;
				if (innerCounts.containsKey(match)) {
					count = innerCounts.get(match);					
					numberOfBigramsWithCount.put(count, numberOfBigramsWithCount.get(count) - 1.0);
				}
				innerCounts.put(match, count + 1.0);
				
				if (!numberOfBigramsWithCount.containsKey(count + 1.0)) {
					numberOfBigramsWithCount.put(count + 1.0, 1.0);
				} else {
					numberOfBigramsWithCount.put(count + 1.0, numberOfBigramsWithCount.get(count + 1.0) + 1.0);
				}
				
				previousWord = match;
			}
		}

		vocab = words.size();
	}

	public double number(String word1, String word2) {
		if (numbers.containsKey(word1) && numbers.get(word1).containsKey(word2)) {
			return numbers.get(word1).get(word2);
		}
		return 0.0;
	}

	public double numberOfUnigram(String word) {
		if (unigramNumbers.containsKey(word)) {
			return unigramNumbers.get(word);
		}
		return 0.0;
	}

	public double UnsmoothedBigramProb(String word1, String word2) {
		if (numbers.containsKey(word1)) {
			if (numbers.get(word1).containsKey(word2)) {
				return numbers.get(word1).get(word2) / unigramNumbers.get(word1);
			} else {
				return 0.0;
			}
		} else {
			return 0.0;
		}
	}

	public double LaplaceProb(String word1, String word2) {		
		return (number(word1, word2) + 1.0) / (numberOfUnigram(word1) + vocab);
	}

	public String generateEmail() {
		String sentence = " ";
		String currentWord = START;
		String nextWord = START;		
		while (!currentWord.equals(".") && sentence.length() <= 400) {
			Set<String> keySet = numbers.get(currentWord).keySet();			
			double rand = Math.random() * unigramNumbers.get(currentWord);
			Iterator<String> i = keySet.iterator();			
			while (i.hasNext() && rand >= 0) {
				nextWord = i.next();
				rand -= (double) numbers.get(currentWord).get(nextWord);
			}
			currentWord = nextWord;
			sentence += currentWord + " ";
		}
		return sentence;
	}

	public double perplexityFunction(Set<String> testSamples) {
		float product = 1;
		int wordCount = 0;
		Stack<Double> products = new Stack<Double>();
		String regexp = "('?\\w+|\\p{Punct})";
		Pattern pattern = Pattern.compile(regexp);
		
		for (String sample : testSamples) {
			Matcher matcher = pattern.matcher(sample);
			String previousWord = START;
			while (matcher.find()) {
				String match = matcher.group();

				products.push(LaplaceProb(previousWord, match));
				wordCount++;
				
				previousWord = match;
			}
		}		
		double power = 1.0 / wordCount;
		
		while (!products.empty()) {
			product *= Math.pow(products.pop(), power);
		}
		return 1 / product;
	}
}
