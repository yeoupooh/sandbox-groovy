/**
 * Created by yeoupooh on 16. 3. 20.
 */
import edu.uci.ics.crawler4j.crawler.*
import edu.uci.ics.crawler4j.fetcher.*
import edu.uci.ics.crawler4j.robotstxt.*

@Grapes([
        @Grab(group = 'org.slf4j', module = 'slf4j-api', version = '1.7.12'),
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.1.3'),
        @Grab(group = 'ch.qos.logback', module = 'logback-core', version = '1.1.3'),
        @Grab(group = 'edu.uci.ics', module = 'crawler4j', version = '4.2')
])

class BasicCrawler extends WebCrawler {

}

/*
 * crawlStorageFolder is a folder where intermediate crawl data is
 * stored.
 */
def crawlStorageFolder = '.'

/*
 * numberOfCrawlers shows the number of concurrent threads that should
 * be initiated for crawling.
 */
int numberOfCrawlers = 1

CrawlConfig config = new CrawlConfig();
config.setCrawlStorageFolder(crawlStorageFolder);

/*
 * Instantiate the controller for this crawl.
 */
PageFetcher pageFetcher = new PageFetcher(config);
RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

/*
 * For each crawl, you need to add some seed urls. These are the first
 * URLs that are fetched and then the crawler starts following links
 * which are found in these pages
 */
controller.addSeed("http://www.ics.uci.edu/");
controller.addSeed("http://www.ics.uci.edu/~lopes/");
controller.addSeed("http://www.ics.uci.edu/~welling/");

/*
 * Start the crawl. This is a blocking operation, meaning that your code
 * will reach the line after this only when crawling is finished.
 */
controller.start(BasicCrawler.class, numberOfCrawlers);
