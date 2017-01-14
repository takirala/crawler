import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Crawler {

	public enum AssetNodes {
		a("abs:href"), link("abs:href"), img("abs:src"), script("abs:src");

		String attribute;

		AssetNodes(String query) {
			this.attribute = query;

		}

		static boolean isAssetNode(String name) {
			for (AssetNodes node : AssetNodes.values()) {
				if (node.name().equals(name))
					return true;
			}
			return false;
		}

	}

	final List<String> relations;

	public Crawler() {
		relations = new ArrayList<String>();
		relations.add("publisher");
		relations.add("alternate");
		relations.add("canonical");
	}

	public void crawl(String url) {
		System.err.println("Crawling : " + url);
		try {
			Queue<String> urls = new LinkedList<String>();
			urls.add(url);
			URI uri = new URI(url);
			String hostname = uri.getHost();
			if (!hostname.startsWith("www."))
				hostname = "www." + hostname;

			Map<String, List<String>> result = new HashMap<String, List<String>>();
			while (!urls.isEmpty())
				populate(urls, result, hostname);
			System.out.println(convertToJSON(result));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String convertToJSON(Map<String, List<String>> result) {
		Gson builder = (new GsonBuilder()).setPrettyPrinting().create();

		JsonArray array = new JsonArray();
		for (Entry<String, List<String>> pair : result.entrySet()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("url", pair.getKey());
			obj.add("assets", builder.toJsonTree(pair.getValue()));
			array.add(obj);
		}

		return builder.toJson(array);
	}

	private void populate(Queue<String> urls, Map<String, List<String>> result, final String hostname)
			throws IOException {

		if (urls.isEmpty())
			return;
		// Get the document.
		String url = urls.remove();
		int initSize = urls.size();
		if (!result.containsKey(url)) {
			Document doc = null;
			try {
				doc = Jsoup.connect(url).followRedirects(true).get();
			} catch (Exception ex) {
				// nothing to handle. just a bad url. we can safely ignore.
				return;
			}
			List<String> assetList = new ArrayList<String>();
			Iterator<Element> it = doc.getAllElements().iterator();
			// iterate overall all links.
			int assetCtr = assetList.size();
			while (it.hasNext()) {
				Element e = it.next();
				traverseNode(e, hostname, urls, result, assetList);
			}
			System.out.println(
					url + " URLs size : " + (urls.size() - initSize) + " Assets : " + (assetList.size() - assetCtr));
			result.put(url, assetList);
		}
	}

	private void traverseNode(Element elem, String hostName, Queue<String> urls, Map<String, List<String>> result,
			List<String> assetList) {

		String nodeName = elem.nodeName();
		if (AssetNodes.isAssetNode(nodeName)) {

			AssetNodes node = AssetNodes.valueOf(nodeName);
			String asset = elem.attr(node.attribute);
			if (asset.trim().isEmpty())
				return;

			switch (node) {
			case script:
			case img:
				// img and script are always assets.
				assetList.add(asset);
				break;
			case a:
				// a is always another url.
				// if same domain, add to recursive call.
				if (!result.containsKey(asset)) {
					addURL(hostName, asset, urls);
				}
				break;
			case link:
				// link may be asset or a url.
				String relAttr = elem.attr("rel");

				// System.out.println("link " + relAttr + " -> " + asset);
				if (isLinkNotAsset(relAttr) && !result.containsKey(asset)) {
					// This is a recursive link. Not asset.
					addURL(hostName, asset, urls);
				} else {
					assetList.add(asset);
				}
				break;
			default:
				break;
			}
		}
	}

	boolean isLinkNotAsset(String relation) {
		return relations.contains(relation);
	}

	private void addURL(String baseURI, String asset, Queue<String> urls) {
		// Check if same domain.
		if (!asset.contains(baseURI))
			return;

		asset = expandURL(asset);
		urls.add(asset);
	}

	private String expandURL(String url) {

		if (url.startsWith("https://") && !url.startsWith("https://www.")) {
			return "https://www." + url.substring(8);
		}

		if (url.startsWith("http://") && !url.startsWith("http://www.")) {
			return "http://www." + url.substring(8);
		}

		return url;
	}
}
