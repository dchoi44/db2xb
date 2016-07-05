import java.io.*;
import java.util.Hashtable;

public class DB2XB {
	public static void main(String[] args) throws IOException {
		Hashtable<String, String> hash_p = new Hashtable<String, String>();
		Hashtable<String, String> hash_o = new Hashtable<String, String>();

		// if (args.length != 0){
		// for(int i = 0; i < args.length ; i++){
		//
		// }
		// }

		makedict_o(hash_o);
		makedict_p(hash_p);

		convert(hash_o, hash_p);
	}

	public static void convert(Hashtable<String, String> hash_o, Hashtable<String, String> hash_p) throws IOException {
		File dir = new File("./input");
		File[] files = dir.listFiles();
		String pre_o, pre_p;
		pre_o = "http://xb.saltlux.com/resource/";
		pre_p = "http://xb.saltlux.com/schema/property/";
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				PrintWriter pw = new PrintWriter("./output/c_" + files[i].getName());
				BufferedReader br = new BufferedReader(new FileReader("./input/" + files[i].getName()));
				int negs = 0, negp = 0, nego = 0, tot = 0;
				while (true) {
					String line, S = null, P = null, O = null;
					line = br.readLine();
					if (line == null)
						break;
					tot += 1;
					String[] words = line.split("\t");
					
					if (hash_o.containsKey(words[0].replace("<", "").replace(">", "").split("resource/")[1]))
						S = "<" + pre_o + hash_o.get(words[0].replace("<", "").replace(">", "").split("resource/")[1]) + ">";
					else{
						negs += 1;
						continue;
					}
					
					if (hash_p.containsKey(words[1].replace("<", "").replace(">", "").split("ontology/")[1]))
						P = "<" + pre_p + hash_p.get(words[1].replace("<", "").replace(">", "").split("ontology/")[1]) + ">";
					else{
						negp += 1;
						continue;
					}
					
					if (words[2].replace("<", "").replace(">", "").split("resource/").length == 2){
						if(hash_o.containsKey(words[2].replace("<", "").replace(">", "").replace(".","").trim().split("resource/")[1]))
							O = "<" + pre_o + hash_o.get(words[2].replace("<", "").replace(">", "").replace(".","").trim().split("resource/")[1]) + ">";
						else{
							nego += 1;
							continue;
						}
					}
					else
						O = words[2];
					
					pw.println(S + "\t" + P + "\t" + O + "\t.");
				}
				br.close();
				pw.close();
				System.out.println(files[i].getName()+": Done, " + (negs+negp+nego) + "/" + tot + " lines(" + (negs + negp + nego)*100/(float)tot + "%) had been neglected.");
				System.out.println(negs + " subjects, " + negp + " properties, " + nego + " objects are unmapped.");
			}
			else {
				System.out.println("No sub-directory allowed. Ignoring it..");
			}
		}
	}

	public static void makedict_o(Hashtable<String, String> hash_o) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./mapping/xb2kw.csv"));
		String line;
		line = br.readLine();

		while (true) {
			line = br.readLine();
			if (line == null)
				break;

			String[] words = line.split(",");
			String snd_itm = words[0].replace("\"", "").replace("xbr:", "");
			String fst_itm = words[1].replace("\"", "").replace("https://ko.wikipedia.org/wiki/", "");
			hash_o.put(fst_itm, snd_itm);

		}
		br.close();
	}

	public static void makedict_p(Hashtable<String, String> hash_p) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./mapping/m_dbo2xbo.csv"));
		while (true) {
			String line;
			line = br.readLine();
			if (line == null)
				break;

			while (true) {
				if (line.length() == line.replace(",", "").length()) {
					line = br.readLine();
				} else
					break;
			}

			if (line.substring(line.indexOf(',') + 1).charAt(0) == ',') {
				// no xbo property case
				int comma_cnt = 3;

				while (true) {
					comma_cnt -= line.length() - line.replace(",", "").length();
					if (comma_cnt <= 0)
						break;
					line = br.readLine();
				}

			} else {
				String[] words = line.split(",");
				String snd_itm = words[0];
				String fst_itm = words[1].replace("\"", "");
				hash_p.put(fst_itm, snd_itm);

				if (words[1].contains("\"")) {
					// more than one items
					while (true) {
						line = br.readLine();
						words = line.split(",");
						fst_itm = words[0].replace("\"", "");
						hash_p.put(fst_itm, snd_itm);
						if (words[0].contains("\""))
							break;
					}
					int comma_cnt = 2;
					while (true) {
						comma_cnt -= line.length() - line.replace(",", "").length();
						if (comma_cnt <= 0)
							break;
						line = br.readLine();
					}
				} else {
					int comma_cnt = 3;
					while (true) {
						comma_cnt -= line.length() - line.replace(",", "").length();
						if (comma_cnt <= 0)
							break;
						line = br.readLine();
					}
				}
			}
		}
		br.close();
	}
}
