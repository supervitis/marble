package org.marble.commons.executor.streaming;

import java.util.ArrayList;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
//TODO: LISTENER DE TEST. AQUI TODAVIA HAY QUE IMPLEMENTAR TODA LA FUNCIONALIDAD PARA CADA STATUS
public class TwitterStreamingListener implements StatusListener{private String keyword;

private ArrayList<Status> statuses;

public TwitterStreamingListener(){
	statuses = new ArrayList<Status>();
}

public void setKeyword(String keyword){
	this.keyword = keyword;
}
public void onStatus(Status status) {
	System.out.println(keyword);
	if(status.getText().toLowerCase().contains(keyword)){
		statuses.add(status);
		System.out.println(status.getText());
	}
	
}

public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

}

public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

}

public void onScrubGeo(long userId, long upToStatusId) {
   
}

public void onStallWarning(StallWarning warning) {

}

public void onException(Exception ex) {
	
}
}