import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter url to be crawled : ");

			Crawler crawler = new Crawler();
			String url = null;
			while (!(url = sc.nextLine()).equals("exit")) {
				if (url.isEmpty())
					url = "https://www.gocardless.com/";
				crawler.crawl(url);
			}
		}
	}

	private static void test() {

	}
}
