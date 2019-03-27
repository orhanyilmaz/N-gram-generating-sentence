import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Read {

	public HashSet<String> readEmails60(String path) {

		String line;
		String counter;
		try {
			HashSet<String> results = new HashSet<String>();
			BufferedReader readc = new BufferedReader(new FileReader(path));
			BufferedReader readr = new BufferedReader(new FileReader(path));
			line = readr.readLine();

			counter = readc.readLine();

			int i = 0, j = 0;
			while (counter != null) {
				counter = readc.readLine();
				j++;
			}

			while (i < 6 * j / 10) {
				results.add(line);
				line = readr.readLine();

				i++;
			}
			readr.close();
			readc.close();
			return results;

		} catch (IOException iox) {
			return null;
		}
	}

	public HashSet<String> readEmails40(String path) {

		String line;
		String counter;
		try {
			HashSet<String> results = new HashSet<String>();
			BufferedReader readc = new BufferedReader(new FileReader(path));
			BufferedReader readr = new BufferedReader(new FileReader(path));
			line = readr.readLine();

			counter = readc.readLine();

			int i = 0, j = 0;
			while (counter != null) {
				counter = readc.readLine();
				j++;
			}

			while (i < j) {
				if (i > j * 6 / 10) {
					results.add(line);
				}
				line = readr.readLine();

				i++;
			}

			readr.close();
			readc.close();
			return results;

		} catch (IOException iox) {
			return null;
		}
	}
}
