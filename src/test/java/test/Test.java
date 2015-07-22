package test;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
public class Test {

	public static void main(String[] args) {
		 ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		 configurationBuilder.setJSONStoreEnabled(true);
		 configurationBuilder.setOAuthConsumerKey("96Xv75qXVCALSzNtOv2h8o2gV");
	        configurationBuilder.setOAuthConsumerSecret("BmABb2FGfYBuh1okfKtTtY50gOnMiDhyg0rFZl9yFHNjcRu68U");
	        configurationBuilder.setOAuthAccessToken("412585536-i8udOorXPJhn3Eh3UL8njf8228KhztoAyISD1Qyh");
	        configurationBuilder.setOAuthAccessTokenSecret("x9FoKE5rNxOLGSvJL9OlzAlOle2P5dsUtGDtYglNQ91Yy");

         Configuration configuration = configurationBuilder.build();
         TwitterStreamFactory streamFactory = new TwitterStreamFactory(configuration);
			TwitterStream twitterStream = streamFactory.getInstance();
	        StatusListener listener = new StatusListener() {
	            @Override
	            public void onStatus(Status status) {
	                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
	            }

	            @Override
	            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	            }

	            @Override
	            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
	            }

	            @Override
	            public void onScrubGeo(long userId, long upToStatusId) {
	                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	            }

	            @Override
	            public void onStallWarning(StallWarning warning) {
	                System.out.println("Got stall warning:" + warning);
	            }

	            @Override
	            public void onException(Exception ex) {
	                ex.printStackTrace();
	            }
	        };
	        twitterStream.addListener(listener);
	        twitterStream.sample();
		}

}
