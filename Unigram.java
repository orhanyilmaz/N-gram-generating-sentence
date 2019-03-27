import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class Unigram {
	public Set<String> samples;
	public HashMap<String, Double> numbers;
	public double totalNumber;
	public final String START = ":S";
	public Set<String> words;
	public double vocab;
	public HashMap<Double, Double> numberOfUnigramsWithCount;

	public Unigram(Set<String> samples) {
		this.samples = samples;
		this.numbers = new HashMap<String, Double>();
		this.totalNumber = 0;
		this.words = new HashSet<String>();
		this.numberOfUnigramsWithCount = new HashMap<Double, Double>();
	}

	public void modelling() {

		String regexp = "('?\\w+|\\p{Punct})";
		Pattern pattern = Pattern.compile(regexp);
		for (String sample : samples) {
			Matcher matcher = pattern.matcher(sample);
			while (matcher.find()) {
				String match = matcher.group();

				words.add(match);

				double count = 0;
				if (numbers.containsKey(match)) {
					count = numbers.get(match);
					numberOfUnigramsWithCount.put(count, numberOfUnigramsWithCount.get(count) - 1);
				}
				numbers.put(match, count + 1);
				if (!numberOfUnigramsWithCount.containsKey(count + 1)) {
					numberOfUnigramsWithCount.put(count + 1, 1.0);
				} else {
					numberOfUnigramsWithCount.put(count + 1, numberOfUnigramsWithCount.get(count + 1) + 1);
				}

				totalNumber++;
			}
		}

		vocab = words.size();
	}

	public String generateEmail() {
		String sentence = "";
		String currentWord = START;
		Set<String> keySet = numbers.keySet();
		while (!currentWord.equals(".") && sentence.length() <= 400) {
			double rand = Math.random() * totalNumber;
			Iterator<String> i = keySet.iterator();
			while (i.hasNext() && rand >= 0) {
				currentWord = i.next();
				rand -= numbers.get(currentWord);
			}
			sentence += currentWord + " ";
			
		}
		return sentence;
	}

}
