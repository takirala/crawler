import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		System.out.println("Enter url to be crawled : ");
		try (Scanner sc = new Scanner(System.in)) {
			Crawler crawler = new Crawler();
			String url = null;
			while (!(url = sc.nextLine()).equals("exit")) {
				if (url.isEmpty())
					url = "https://www.google.com/";
				crawler.crawl(url);
			}
		}
	}
}
