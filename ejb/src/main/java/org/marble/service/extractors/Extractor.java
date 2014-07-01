package org.marble.service.extractors;

import org.marble.model.Topic;

public abstract class Extractor {
    public abstract void searchAndLoadStatuses(Topic topic);
}
