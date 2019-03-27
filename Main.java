import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Main {

	public static void main(String[] args) {

		HashSet<String> trainSet = new HashSet<String>();
		HashSet<String> testSet = new HashSet<String>();
		Read object = new Read();
		trainSet = object.readEmails60(args[0]);
		testSet = object.readEmails40(args[0]);
		Unigram u = new Unigram(trainSet);
		u.modelling();
		Bigram b = new Bigram(trainSet);
		b.modelling();
	
		try {
			File file = new File(args[1]);
			FileWriter writer = new FileWriter(file);
			BufferedWriter write = new BufferedWriter(writer);			
			for (int i = 1; i <= 20; i++) {
				write.write("unigram sentence " + i + " : " + u.generateEmail());
				write.newLine();

			}

			for (int i = 1; i <= 20; i++) {
				write.write("bigram sentence " + i + " : " + b.generateEmail());
				write.newLine();

			}
			write.write("Perplexity of the test set bigram: " + b.perplexityFunction(testSet));
			write.newLine();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
