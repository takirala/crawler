
public class Test {

	public static void main(String[] args) {
		test_invalid_url();
		test_with_redirects();
		test_subdomain();
	}

	private static void test_subdomain() {
		Crawler crawler = new Crawler();
		//Tested with a locally hosted site. Also tested with google. (mail.google.com is not crawled).
		crawler.crawl("https://www.google.com/");
	}

	private static void test_invalid_url() {
		Crawler crawler = new Crawler();
		// mail.google.com is not crawled.
		crawler.crawl("ftp://www.google.com/");
	}

	private static void test_with_redirects() {
		Crawler crawler = new Crawler();
		// Used a locally hosted site for testing.
		crawler.crawl("ftp://www.google.com/");
	}
	
	
}
